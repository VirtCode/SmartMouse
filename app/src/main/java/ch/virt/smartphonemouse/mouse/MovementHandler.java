package ch.virt.smartphonemouse.mouse;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import androidx.preference.PreferenceManager;

/**
 * This class handles and calculates the movement of the mouse
 */
public class MovementHandler implements SensorEventListener {

    private static final float NANO_FULL_FACTOR = 1e-9f;

    public static final int SENSOR_TYPE = Sensor.TYPE_ACCELEROMETER;
    public static final int SAMPLING_RATE = SensorManager.SENSOR_DELAY_FASTEST;

    private SensorManager manager;
    private Sensor sensor;

    private Context context;
    private MouseInputs inputs;

    private boolean registered;

    private long lastSample = 0;
    private Pipeline xLine, yLine;

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
        create();
    }

    /**
     * Creates the signal processing pipelines.
     */
    public void create() {
        int sampleRate = PreferenceManager.getDefaultSharedPreferences(context).getInt("communicationTransmissionRate", 200);

        xLine = new Pipeline(sampleRate, new PipelineConfig(PreferenceManager.getDefaultSharedPreferences(context)));
        yLine = new Pipeline(sampleRate, new PipelineConfig(PreferenceManager.getDefaultSharedPreferences(context)));
    }

    /**
     * Fetches the sensor from the context.
     */
    private void fetchSensor() {
        manager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensor = manager.getDefaultSensor(SENSOR_TYPE);
    }

    /**
     * Registers the sensor for this handler.
     */
    public void register() {
        if (registered) return;
        manager.registerListener(this, sensor, SAMPLING_RATE);

        lastSample = 0;
        registered = true;
    }

    /**
     * Unregisters the sensor for this handler.
     */
    public void unregister() {
        if (!registered) return;
        manager.unregisterListener(this, sensor);

        registered = false;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (!registered) return; // Ignore Samples when the listener is not registered
        if (lastSample == 0) { // Ignore First sample, because there is no delta
            lastSample = event.timestamp;
            return;
        }

        float delta = (event.timestamp - lastSample) * NANO_FULL_FACTOR; // Delta in Seconds

        // Calculate and Submit values
        inputs.changeXPosition(xLine.nextForDistance(delta, event.values[0]));
        inputs.changeYPosition(-yLine.nextForDistance(delta, event.values[1]));

        lastSample = event.timestamp;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}
