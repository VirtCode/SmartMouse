package ch.virt.smartphonemouse.customization;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import androidx.preference.PreferenceManager;

import java.util.Timer;
import java.util.TimerTask;

import ch.virt.smartphonemouse.mouse.MovementHandler;

/**
 * This class is used to measure and save the sampling rate of the inbuilt accelerometer.
 */
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

    private final Context context;

    /**
     * Creates the calibrator.
     *
     * @param context context to use
     */
    public SamplingRateCalibrator(Context context) {
        this.context = context;

        fetchSensor();
    }

    /**
     * Fetches the sensor from the system.
     */
    private void fetchSensor() {
        manager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensor = manager.getDefaultSensor(MovementHandler.SENSOR_TYPE);
    }

    /**
     * Registers itself as a listener.
     */
    private void register() {
        if (registered) return;
        manager.registerListener(this, sensor, MovementHandler.SAMPLING_RATE);

        registered = true;
    }

    /**
     * Unregisters itself as a listener.
     */
    private void unregister() {
        if (!registered) return;
        manager.unregisterListener(this, sensor);

        registered = false;
    }

    /**
     * Starts the measuring process.
     *
     * @param doneListener listener that is executed once the process has finished
     */
    public void calibrate(DoneListener doneListener) {
        register();
        prepareTest();
        running = true;

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                running = false;
                unregister();

                int rate = finishTest();

                doneListener.done(rate);
            }
        }, TEST_LENGTH);

    }

    /**
     * Initializes the variables for the process.
     */
    private void prepareTest() {
        lastTime = 0;
        delays = 0;
        amount = 0;
    }

    /**
     * Finishes the measuring process by processing the results and saving it into the preferences.
     */
    private int finishTest() {
        long averageDelay = delays / amount;
        float averageDelaySecond = averageDelay * NANO_FULL_FACTOR;

        int samplesPerSecond = Math.round(1f / averageDelaySecond);

        SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(context).edit();
        edit.putBoolean("movementSamplingCalibrated", true);
        edit.putInt("movementSamplingRealRate", samplesPerSecond);
        edit.apply();

        return samplesPerSecond;
    }

    /**
     * Returns how long the measuring process approximately will go.
     *
     * @return length of the process in milliseconds
     */
    public int getTestLength() {
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

    /**
     * This interface is a listener used for when the measuring process is done.
     */
    public interface DoneListener {

        /**
         * Called when the process is done.
         *
         * @param samplingRate sampling rate that was measured
         */
        void done(int samplingRate);
    }
}
