package ch.virt.smartphonemouse.mouse.processing;

import ch.virt.smartphonemouse.mouse.processing.components.*;
import ch.virt.smartphonemouse.mouse.processing.implement.DebugInterface;
import ch.virt.smartphonemouse.mouse.processing.implement.ProcessingParameters;
import ch.virt.smartphonemouse.mouse.processing.math.Vec2f;
import ch.virt.smartphonemouse.mouse.processing.math.Vec3f;

public class Processing {

    private Trapezoid3f rotationDeltaTrapezoid;

    private WindowAverage activeGravityAverage;
    private WindowAverage activeNoiseAverage;
    private OrThreshold activeThreshold;
    private boolean lastActive;

    private WindowAverage3f gravityInactiveAverage;
    private Vec3f gravityCurrent;

    private Trapezoid2f distanceVelocityTrapezoid;
    private Vec2f distanceVelocity;
    private Trapezoid2f distanceDistanceTrapezoid;
    private float sensitivity;


    // Gravity Rotation is disabled by default because it currently does not work as expected
    private boolean enableGravityRotation;


    private final DebugInterface debug;

    public Processing(DebugInterface debug, ProcessingParameters parameters) {
        this.debug = debug;

        // Create and configure components
        rotationDeltaTrapezoid = new Trapezoid3f();

        activeGravityAverage = new WindowAverage(parameters.getLengthWindowGravity());
        activeNoiseAverage = new WindowAverage(parameters.getLengthWindowNoise());
        activeThreshold = new OrThreshold(parameters.getLengthThreshold(), parameters.getThresholdAcceleration(), parameters.getThresholdRotation());

        gravityInactiveAverage = new WindowAverage3f(parameters.getLengthGravity());
        gravityCurrent = new Vec3f();

        distanceVelocityTrapezoid = new Trapezoid2f();
        distanceDistanceTrapezoid = new Trapezoid2f();
        distanceVelocity = new Vec2f();
        sensitivity = parameters.getSensitivity();

        enableGravityRotation = parameters.getEnableGravityRotation();
    }

    public static void registerDebugColumns(DebugInterface debug) {
        debug.registerColumn("time", Float.class);
        debug.registerColumn("acceleration", Vec3f.class);
        debug.registerColumn("angular-velocity", Vec3f.class);
        debug.registerColumn("active-acc-abs", Float.class);
        debug.registerColumn("active-acc-grav", Float.class);
        debug.registerColumn("active-acc", Float.class);
        debug.registerColumn("active-rot", Float.class);
        debug.registerColumn("active", Boolean.class);
        debug.registerColumn("gravity", Vec3f.class);
        debug.registerColumn("acceleration-linear", Vec2f.class);
        debug.registerColumn("velocity", Vec2f.class);
        debug.registerColumn("distance", Vec2f.class);
    }

    public Vec2f next(float time, float delta, Vec3f acceleration, Vec3f angularVelocity) {
        // Stage debug values
        debug.stageFloat(time);
        debug.stageVec3f(acceleration);
        debug.stageVec3f(angularVelocity);

        // Integrate rotation to distance, since that is used more often
        Vec3f rotationDelta = rotationDeltaTrapezoid.trapezoid(delta, angularVelocity);

        boolean active = active(acceleration, angularVelocity);
        debug.stageBoolean(active);

        Vec2f linearAcceleration = gravity(active, acceleration, rotationDelta);
        debug.stageVec2f(linearAcceleration);

        Vec2f distance = distance(delta, active, linearAcceleration, rotationDelta);
        debug.stageVec2f(distanceVelocity); // Do this here because it did not fit into the method
        debug.stageVec2f(distance);

        // Handle active changes "globally" for optimization
        lastActive = active;

        debug.commit();
        return distance;
    }

    public boolean active(Vec3f acceleration, Vec3f angularVelocity) {

        // Calculate the acceleration activation
        float acc = acceleration.xy().abs();
        debug.stageFloat(acc);

        // Remove gravity or rather lower frequencies
        float gravity = activeGravityAverage.avg(acc);
        debug.stageFloat(gravity);
        acc -= gravity;
        acc = Math.abs(acc);

        // Remove noise
        acc = activeNoiseAverage.avg(acc);
        debug.stageFloat(acc);

        // Calculate the rotation activation
        float rot = Math.abs(angularVelocity.z);
        debug.stageFloat(rot);

        // Do the threshold
        return activeThreshold.active(acc, rot);
    }

    public Vec2f gravity(boolean active, Vec3f acceleration, Vec3f rotationDelta) {

        // Differentiate between the user being active or not
        if (active) {

            // Reset average for next phase
            if (!lastActive) {
                gravityInactiveAverage.reset();
            }

            // Rotate current gravity
            if (enableGravityRotation) gravityCurrent.rotate(rotationDelta.copy().negative());

        } else {
            // Just calculate the average of the samples
            gravityCurrent = gravityInactiveAverage.avg(acceleration);
        }

        debug.stageVec3f(gravityCurrent);

        // Subtract the gravity
        return acceleration.xy().subtract(gravityCurrent.xy());
    }

    public Vec2f distance(float delta, boolean active, Vec2f linearAcceleration, Vec3f rotationDelta) {

        // Only calculate if it is active for optimization
        if (active){

            // Counter-rotate the velocity
            distanceVelocity.rotate(-rotationDelta.z);

            // Integrate to distance
            distanceVelocity.add(distanceVelocityTrapezoid.trapezoid(delta, linearAcceleration));
            return distanceDistanceTrapezoid.trapezoid(delta, distanceVelocity).multiply(sensitivity);

        } else {

            // Reset stuff
            if (lastActive) {
                distanceVelocity = new Vec2f();

                // Clean the trapezoids because they contain a last value
                distanceVelocityTrapezoid.reset();
                distanceDistanceTrapezoid.reset();
            }

            return new Vec2f();
        }
    }

}
