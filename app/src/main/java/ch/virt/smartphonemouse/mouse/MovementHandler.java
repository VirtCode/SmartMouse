package ch.virt.smartphonemouse.mouse;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import ch.virt.smartphonemouse.mouse.math.Vec2f;
import ch.virt.smartphonemouse.mouse.math.Vec3f;
import ch.virt.smartphonemouse.transmission.DebugTransmitter;

/**
 * This class handles and calculates the movement of the mouse
 */
public class MovementHandler implements SensorEventListener {

    private static final float NANO_FULL_FACTOR = 1e-9f;

    public static final int SENSOR_TYPE_ACCELEROMETER = Sensor.TYPE_ACCELEROMETER;
    public static final int SENSOR_TYPE_GYROSCOPE = Sensor.TYPE_GYROSCOPE;
    public static final int SAMPLING_RATE = SensorManager.SENSOR_DELAY_FASTEST;

    private SensorManager manager;
    private Sensor accelerometer;
    private Sensor gyroscope;

    private boolean registered;


    private Vec3f gyroSample = new Vec3f(); // TODO: Make this a buffer to accommodate for vastly different sampling rates
    private long lastTime = 0;
    private long firstTime = 0;
    private Processing processing;

    private final Context context;
    private final MouseInputs inputs;

    /**
     * Creates a movement handler.
     *
     * @param context context to get the sensor from
     * @param inputs  inputs to send the movement to
     */
    public MovementHandler(Context context, MouseInputs inputs) {
        this.context = context;
        this.inputs = inputs;

        fetchSensor();
    }

    /**
     * Creates the signal processing pipelines.
     */
    public void create(DebugTransmitter debug) {
        processing = new Processing(debug, new ProcessingParameters());
    }

    /**
     * Fetches the sensor from the context.
     */
    private void fetchSensor() {
        manager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        accelerometer = manager.getDefaultSensor(SENSOR_TYPE_ACCELEROMETER);
        gyroscope = manager.getDefaultSensor(SENSOR_TYPE_GYROSCOPE);
    }

    /**
     * Registers the sensor for this handler.
     */
    public void register() {
        if (registered) return;
        manager.registerListener(this, accelerometer, SAMPLING_RATE);
        manager.registerListener(this, gyroscope, SAMPLING_RATE);

        lastTime = 0;
        firstTime = 0;
        registered = true;
    }

    /**
     * Unregisters the sensor for this handler.
     */
    public void unregister() {
        if (!registered) return;
        manager.unregisterListener(this, accelerometer);
        manager.unregisterListener(this, gyroscope);

        registered = false;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (!registered) return; // Ignore Samples when the listener is not registered

        if (event.sensor.getType() == SENSOR_TYPE_ACCELEROMETER) {

            if (firstTime == 0) { // Ignore First sample, because there is no delta
                lastTime = event.timestamp;
                firstTime = event.timestamp;
                return;
            }

            float delta = (event.timestamp - lastTime) * NANO_FULL_FACTOR;
            float time = (event.timestamp - firstTime) * NANO_FULL_FACTOR;
            Vec3f acceleration = new Vec3f(event.values[0], event.values[1], event.values[2]);

            Vec2f distance = processing.next(time, delta, acceleration, gyroSample);
            inputs.changeXPosition(distance.x);
            inputs.changeYPosition(-distance.y);

            lastTime = event.timestamp;

        } else if (event.sensor.getType() == SENSOR_TYPE_GYROSCOPE) {
            // Here we assume that the samples arrive in chronological order (which is crucial anyway), so we will always have the latest sample in this variable
            this.gyroSample = new Vec3f(event.values[0], event.values[1], event.values[2]);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}
