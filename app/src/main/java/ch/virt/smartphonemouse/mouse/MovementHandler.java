package ch.virt.smartphonemouse.mouse;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import ch.virt.smartphonemouse.helper.MainContext;

public class MovementHandler implements SensorEventListener {

    private static final float NANO_FULL_FACTOR = 1e-6f;

    private static final int SENSOR_TYPE = Sensor.TYPE_ACCELEROMETER;
    private static final int SAMPLING_RATE = SensorManager.SENSOR_DELAY_FASTEST;

    private SensorManager manager;
    private Sensor sensor;

    private MainContext main;
    private MouseInputs inputs;

    private boolean registered;

    private long lastSample = 0;
    private Pipeline xLine, yLine;

    public void fetchSensor(){
        manager = (SensorManager) main.getContext().getSystemService(Context.SENSOR_SERVICE);
        sensor = manager.getDefaultSensor(SENSOR_TYPE);
    }

    public void register(){
        if (registered) return;
        manager.registerListener(this, sensor, SAMPLING_RATE);

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

        // Calculate and Submit values
        inputs.changeXPosition(xLine.nextForDistance(delta, event.values[0]));
        inputs.changeYPosition(yLine.nextForDistance(delta, event.values[1]));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) { }
}
