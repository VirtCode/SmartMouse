package ch.virt.smartphonemouse.ui.debug.handling;

import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import ch.virt.smartphonemouse.mouse.MovementHandler;
//import ch.virt.smartphonemouse.mouse.Pipeline;
//import ch.virt.smartphonemouse.mouse.PipelineConfig;

/**
 * This class does handle the data from the accelerometer and does provide it to the chart.
 */
public class DebugDataHandler implements SensorEventListener {

    private static final float NANO_FULL_FACTOR = 1e-9f;

    private final DebugChartHandler chart;
    private final SharedPreferences preferences;

    private long lastSample = 0;
    private boolean registered;
//    private Pipeline pipeline;

    private final SensorManager manager;
    private Sensor sensor;

    private int axis;

    /**
     * Create a debug data handler.
     *
     * @param manager     sensor manager to get the sensor from
     * @param chart       chart to provide data to
     * @param preferences preferences to fetch specific config
     */
    public DebugDataHandler(SensorManager manager, DebugChartHandler chart, SharedPreferences preferences) {
        this.manager = manager;
        this.chart = chart;
        this.preferences = preferences;

        axis = preferences.getInt("debugChartAxis", 0);

        create(preferences);
        fetchSensor();
    }

    /**
     * Creates the pipeline used for processing.
     *
     * @param preferences preferences to get pipeline config from
     */
    public void create(SharedPreferences preferences) {
//        pipeline = new Pipeline(preferences.getInt("communicationTransmissionRate", 200), new PipelineConfig(preferences));
//        pipeline.enableDebugging();
    }

    /**
     * Fetches the sensor from the sensor manager.
     */
    private void fetchSensor() {
        sensor = manager.getDefaultSensor(MovementHandler.SENSOR_TYPE_ACCELEROMETER);
    }

    /**
     * Registers this handler as a handler for the accelerometer.
     */
    public void register() {
        if (registered) return;
        manager.registerListener(this, sensor, MovementHandler.SAMPLING_RATE);

        lastSample = 0;
        registered = true;
    }

    /**
     * Unregisters this handler as a handler for the accelerometer.
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

//        pipeline.nextForDistance(delta, event.values[axis]);
//        chart.newData(event.timestamp, pipeline.getDebuggingValues());

        lastSample = event.timestamp;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    /**
     * Returns whether the handler is currently registered.
     *
     * @return is currently registered
     */
    public boolean isRegistered() {
        return registered;
    }

    /**
     * Returns the current axis data is used from.
     *
     * @return current axis, 0 = x, 1 = y, 2 = z
     */
    public int getAxis() {
        return axis;
    }

    /**
     * Sets the axis data is used form.
     *
     * @param axis current axis, 0 = x, 1 = y, 2 = z
     */
    public void setAxis(int axis) {
        this.axis = axis;

        preferences.edit().putInt("debugChartAxis", axis).apply();
    }

    /**
     * Resets the algorithm to its initial state.
     */
    public void renew() {
//        pipeline.reset();
    }
}
