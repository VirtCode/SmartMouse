package ch.virt.smartphonemouse.customization;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;

import java.util.Timer;
import java.util.TimerTask;

import ch.virt.smartphonemouse.helper.Listener;
import ch.virt.smartphonemouse.helper.MainContext;
import ch.virt.smartphonemouse.mouse.MovementHandler;

public class SamplingRateCalibrator implements SensorEventListener {

    private static final int TEST_LENGTH = 5000;
    private static final float NANO_FULL_FACTOR = 1e-9f;

    private SensorManager manager;
    private Sensor sensor;
    private boolean registered;

    private boolean running;
    private long lastTime;
    private long delays;
    private int amount;

    private final MainContext main;

    public SamplingRateCalibrator(MainContext main) {
        this.main = main;

        fetchSensor();
    }

    public void fetchSensor(){
        manager = (SensorManager) main.getContext().getSystemService(Context.SENSOR_SERVICE);
        sensor = manager.getDefaultSensor(MovementHandler.SENSOR_TYPE);
    }

    public void register(){
        if (registered) return;
        manager.registerListener(this, sensor, MovementHandler.SAMPLING_RATE);

        registered = true;
    }

    public void unregister(){
        if (!registered) return;
        manager.unregisterListener(this, sensor);

        registered = false;
    }

    public void calibrate(Listener doneListener){
        register();
        prepareTest();
        running = true;

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                running = false;
                unregister();

                finishTest();

                doneListener.called();
            }
        }, TEST_LENGTH);

    }

    public void prepareTest(){
        lastTime = 0;
        delays = 0;
        amount = 0;
    }

    public void finishTest(){
        long averageDelay = delays / amount;
        float averageDelaySecond = averageDelay * NANO_FULL_FACTOR;
        
        int samplesPerSecond = Math.round(1f / averageDelaySecond);

        SharedPreferences.Editor edit = main.getPreferences().edit();
        edit.putInt("movementSamplingRealRate", samplesPerSecond);
        edit.apply();

    }

    public int getTestLength(){
        return TEST_LENGTH;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (!running) return;
        if (lastTime == 0) {
            lastTime = event.timestamp;
            return;
        }

        long delay = event.timestamp - lastTime;

        amount++;
        delays += delay;

        lastTime = event.timestamp;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
