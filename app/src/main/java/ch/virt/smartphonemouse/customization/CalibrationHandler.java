package ch.virt.smartphonemouse.customization;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import androidx.preference.PreferenceManager;
import ch.virt.smartphonemouse.mouse.Calibration;
import ch.virt.smartphonemouse.mouse.MovementHandler;
import ch.virt.smartphonemouse.mouse.Parameters;
import ch.virt.smartphonemouse.mouse.math.Vec3f;

/**
 * This class is used to measure and save the sampling rate of the inbuilt accelerometer.
 */
public class CalibrationHandler implements SensorEventListener {

    private static final float NANO_FULL_FACTOR = 1e-9f;

    private SensorManager manager;
    private Sensor accelerometer;
    private Sensor gyroscope;

    private boolean begun = false;
    private long firstTime = 0;
    private Vec3f gyroSample = new Vec3f();
    private boolean registered;

    private final Context context;
    private final Calibration calibration;

    /**
     * Creates the calibrator.
     *
     * @param context context to use
     */
    public CalibrationHandler(Context context) {
        this.context = context;

        calibration = new Calibration((state) -> {}, new Parameters(PreferenceManager.getDefaultSharedPreferences(context)));
        fetchSensor();
    }

    /**
     * Fetches the sensor from the system.
     */
    private void fetchSensor() {
        manager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        accelerometer = manager.getDefaultSensor(MovementHandler.SENSOR_TYPE_ACCELEROMETER);
        gyroscope = manager.getDefaultSensor(MovementHandler.SENSOR_TYPE_GYROSCOPE);
    }

    /**
     * Registers itself as a listener.
     */
    private void register() {
        if (registered) return;
        manager.registerListener(this, accelerometer, MovementHandler.SAMPLING_RATE);
        manager.registerListener(this, gyroscope, MovementHandler.SAMPLING_RATE);

        registered = true;
    }

    /**
     * Unregisters itself as a listener.
     */
    private void unregister() {
        if (!registered) return;
        manager.unregisterListener(this, accelerometer);
        manager.unregisterListener(this, gyroscope);

        registered = false;
    }

    /**
     * Starts the measuring process.
     *
     * @param doneListener listener that is executed once the process has finished
     */
    public void calibrate(Calibration.StateListener doneListener) {
        calibration.setListener((state) -> {
            if (state == Calibration.STATE_END) unregister();

            doneListener.update(state);
        });

        begun = false;
        calibration.startCalibration();

        register();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == MovementHandler.SENSOR_TYPE_ACCELEROMETER) {

            if (!begun) {
                begun = true;
                firstTime = event.timestamp;
            }


            float time = (event.timestamp - firstTime) * NANO_FULL_FACTOR;
            Vec3f acceleration = new Vec3f(event.values[0], event.values[1], event.values[2]);

            calibration.data(time, acceleration, gyroSample);

        } else if (event.sensor.getType() == MovementHandler.SENSOR_TYPE_GYROSCOPE) {
            this.gyroSample = new Vec3f(event.values[0], event.values[1], event.values[2]);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}
}
