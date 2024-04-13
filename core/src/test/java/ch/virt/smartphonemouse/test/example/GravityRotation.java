package ch.virt.smartphonemouse.test.example;

import ch.virt.smartphonemouse.mouse.processing.implement.DebugInterface;
import ch.virt.smartphonemouse.mouse.processing.math.Vec3f;
import org.joml.Math;
import org.joml.Quaternionf;
import org.joml.Vector3f;

/**
 * # What?
 * This is a minimal example to reproduce the gravity rotation errors I am experiencing. I am trying to achieve the
 * following:
 * During periods of no movement, I collect the acceleration to estimate a gravity vector. Then, when activity is
 * detected, this vector should be rotated using the data of the gyroscope to estimate the gravity during the movement.
 *
 * This does however not work as expected. The gravity is corrected as expected, but just not enough to be satisfactory.
 * I believe that there must be something I am missing, as for some movements, this works perfectly, but other movements
 * produce entirely incorrect results. Let me elaborate.
 *
 * # Setup
 * I have conducted many measurements capturing different movements with multiple smartphone brands and models. I was
 * not able to reproduce the issue with a generated signal though. Here is how to produce a movement which produces
 * massive errors:
 * - Create an even slope of about 5-10°.
 * - Place the phone on the slope with the screen facing towards the ceiling.
 * - Start a transmission and wait a couple of seconds.
 * - Rotate the phone by about 90° and wait a couple of seconds.
 * - End the transmission.
 *
 * In this movement, we have a still period at the beginning, where this algorithm will capture the current gravity
 * vector. Then, during the rotation, the gravity vector captured by the sensor will change significantly, needing to be
 * rotated by the gyroscope to not introduce tremendous errors.
 *
 * When running the just captured movement through this processing here, and plot the results, we see that our rotated
 * gravity estimate does not match the expected gravity vector by a significant error. Yes, I know that I cannot expect
 * this to be accurate, but I think the errors are exceptionally large in this case. Compare this to other captures,
 * e.g. changing the slope steepness, and you'll see the difference in the errors.
 *
 * Here's some other observations I've made. The error seems to be proportional to the amount being rotated, as to be
 * expected. What throws me off though, is that the errors are reproducibly bigger (3x) when the Z-Axis (refer to
 * android sensors for the axes) points towards gravity, than with the X or Y axis. This is really weird and unexpected.
 *
 * # Conclusions
 * I am reasonably sure that this error does not stem from an implementation error on my side, as it behaves as
 * expected with generated signals. What throws me off is that the magnitude of the errors are inconsistent across the
 * different axes. This tells me that there must be one of the following things:
 * - There is some preprocessing happening that android / the manufacturer does that results in these symptoms.
 * - There is a physical phenomenon that I did not account for here.
 * - Errors of this magnitude are to be expected, rendering this technique useless.
 *
 * I don't think that this technique is entirely unfeasible. In some cases, it works extraordinarily well. Drift is also
 * not the main issue here I imagine, as we only use the rotation over the relatively short active periods.
 *
 * So that's the current state of affairs. I have done so much testing, looking at tons of captures (over 60), and am
 * now out of ideas. So if you are still reading this, thank you I guess. If you happen know anything about sensor
 * fusion, gyroscope errors or general physics, don't hesitate to let me know if you want more context or know of
 * something that could help here.
 *
 * (Most of the testing and the captures described here were made on a Samsung Galaxy S20)
 */
public class GravityRotation {

    private DebugInterface debug;

    int position = 0;
    Vector3f[] gravitySamples;

    Vector3f gravity;

    /**
     * Creates the class and registers the debug columns
     *
     * @param debug         debug interface to output to
     * @param lengthGravity length of the gravity average to use
     */
    public GravityRotation(DebugInterface debug, int lengthGravity) {
        gravitySamples = new Vector3f[lengthGravity];
        this.debug = debug;

        debug.registerColumn("time", Float.class);
        debug.registerColumn("acceleration", Vec3f.class);
        debug.registerColumn("angular-velocity", Vec3f.class);
        debug.registerColumn("active", Boolean.class);
        debug.registerColumn("gravity", Vec3f.class);
    }

    public void next(float time, float delta, Vector3f acceleration, Vector3f angularVelocity, boolean active) {

        // Stage debug values
        debug.stageFloat(time);
        debug.stageVec3f(new Vec3f(acceleration.x, acceleration.y, acceleration.z));
        debug.stageVec3f(new Vec3f(angularVelocity.x, angularVelocity.y, angularVelocity.z));
        debug.stageBoolean(active);

        if (active) {
            position = 0; // reset gravity average

            // integrate rotation
            Vector3f angularDistance = angularVelocity.mul(delta, new Vector3f());

            // construct quaternion
            Quaternionf q = new Quaternionf();
            q.rotateXYZ(angularDistance.x, angularDistance.y, angularDistance.z);
            q.conjugate(); // invert rotation

            // rotate gravity
            gravity.rotate(q);

        } else {
            // add sample to average
            gravitySamples[position++ % gravitySamples.length] = acceleration;

            // calculate average
            gravity = new Vector3f();
            for (int i = 0; i < Math.min(position, gravitySamples.length); i++)
                gravity.add(gravitySamples[i]);

            gravity.div(Math.min(position, gravitySamples.length));
        }

        debug.stageVec3f(new Vec3f(gravity.x, gravity.y, gravity.z));
        debug.commit();
    }

}
