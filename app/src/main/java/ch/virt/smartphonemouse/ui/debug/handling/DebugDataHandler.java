package ch.virt.smartphonemouse.ui.debug.handling;

import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import ch.virt.smartphonemouse.mouse.MovementHandler;
import ch.virt.smartphonemouse.mouse.Pipeline;
import ch.virt.smartphonemouse.mouse.PipelineConfig;

public class DebugDataHandler implements SensorEventListener {

    private static final float NANO_FULL_FACTOR = 1e-9f;

    DebugChartHandler chart;
    SharedPreferences preferences;

    private long lastSample = 0;
    private boolean registered;
    private Pipeline pipeline;

    private SensorManager manager;
    private Sensor sensor;

    private int axis;

    public DebugDataHandler(SensorManager manager, DebugChartHandler chart, SharedPreferences preferences) {
        this.manager = manager;
        this.chart = chart;
        this.preferences = preferences;

        axis = preferences.getInt("debugChartAxis", 0);

        create(preferences);
        fetchSensor();
    }

    public void create(SharedPreferences preferences){
        pipeline = new Pipeline(preferences.getInt("communicationTransmissionRate", 200), new PipelineConfig(preferences));
        pipeline.enableDebugging();
    }

    public void fetchSensor(){
        sensor = manager.getDefaultSensor(MovementHandler.SENSOR_TYPE);
    }

    public void register(){
        if (registered) return;
        manager.registerListener(this, sensor, MovementHandler.SAMPLING_RATE);

        lastSample = 0;
        registered = true;
    }

    public void unregister(){
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

        pipeline.nextForDistance(delta, event.values[axis]);
        chart.newData(event.timestamp, pipeline.getDebuggingValues());

        lastSample = event.timestamp;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public boolean isRegistered() {
        return registered;
    }

    public int getAxis() {
        return axis;
    }

    public void setAxis(int axis) {
        this.axis = axis;

        preferences.edit().putInt("debugChartAxis", axis).apply();
    }

    public void renew(){
        pipeline.reset();
    }
}
