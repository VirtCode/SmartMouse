package ch.virt.smartphonemouse.mouse;

import android.content.SharedPreferences;
import android.util.Log;

import java.util.List;

public class Parameters {

    private static final String TAG = "Parameters";

    private static final float CALIBRATION_SAMPLING = 5;
    private static final float CALIBRATION_NOISE = 10;

    private static final float NOISE_RATIO_ACCELERATION = 1f;
    private static final float NOISE_RATIO_ROTATION = 1f;
    private static final float NOISE_FACTOR_ACCELERATION = 1.5f;
    private static final float NOISE_FACTOR_ROTATION = 1.5f;

    private static final float DURATION_THRESHOLD = 0.1f;
    private static final float DURATION_WINDOW_GRAVITY = 0.02f;
    private static final float DURATION_WINDOW_NOISE = 0.01f;
    private static final float DURATION_GRAVITY = 2f;

    private static final float SENSITIVITY = 15000f;

    // Disable this part of the processing because it doesn't work as it should for some reason
    private static final boolean ENABLE_GRAVITY_ROTATION = false;

    private final SharedPreferences prefs;

    public Parameters(SharedPreferences preferences) {
        this.prefs = preferences;
    }

    public void reset() {
        SharedPreferences.Editor edit = prefs.edit();

        edit.putBoolean("movementCalibrated", false);

        edit.putFloat("movementSensitivity", SENSITIVITY);

        edit.putFloat("movementCalibrationSampling", CALIBRATION_SAMPLING);
        edit.putFloat("movementCalibrationNoise", CALIBRATION_NOISE);

        edit.putFloat("movementNoiseRatioAcceleration", NOISE_RATIO_ACCELERATION);
        edit.putFloat("movementNoiseRatioRotation", NOISE_RATIO_ROTATION);
        edit.putFloat("movementNoiseFactorAcceleration", NOISE_FACTOR_ACCELERATION);
        edit.putFloat("movementNoiseFactorRotation", NOISE_FACTOR_ROTATION);

        edit.putFloat("movementDurationThreshold", DURATION_THRESHOLD);
        edit.putFloat("movementDurationWindowGravity", DURATION_WINDOW_GRAVITY);
        edit.putFloat("movementDurationWindowNoise", DURATION_WINDOW_NOISE);
        edit.putFloat("movementDurationGravity", DURATION_GRAVITY);

        edit.putBoolean("movementEnableGravityRotation", ENABLE_GRAVITY_ROTATION);

        edit.apply();
    }

    public void setCalibrated(boolean calibrated) {
        prefs.edit().putBoolean("movementCalibrated", calibrated).apply();
    }

    public boolean isCalibrated() {
        return prefs.getBoolean("movementCalibrated", false);
    }

    public float getCalibrationNoiseTime() {
        return prefs.getFloat("movementCalibrationNoise", CALIBRATION_NOISE);
    }

    public float getCalibrationSamplingTime() {
        return prefs.getFloat("movementCalibrationSampling", CALIBRATION_SAMPLING);
    }

    public void calibrateSamplingRate(float samplingRate) {
        SharedPreferences.Editor edit = prefs.edit();

        Log.d(TAG, "calibrateSamplingRate: " + getSensitivity());

        edit.putInt("movementLengthWindowGravity", Math.round(samplingRate * prefs.getFloat("movementDurationWindowGravity", DURATION_WINDOW_GRAVITY)));
        edit.putInt("movementLengthWindowNoise", Math.round(samplingRate * prefs.getFloat("movementDurationWindowNoise", DURATION_WINDOW_NOISE)));
        edit.putInt("movementLengthThreshold", Math.round(samplingRate * prefs.getFloat("movementDurationThreshold", DURATION_THRESHOLD)));
        edit.putInt("movementLengthGravity", Math.round(samplingRate * prefs.getFloat("movementDurationGravity", DURATION_GRAVITY)));

        edit.apply();
    }
    
    public void calibrateNoiseLevels(List<Float> accelerationNoise, List<Float> rotationNoise) {
        
        // Sort arrays and get at ratio (same as removing top X% and getting the largest)
        accelerationNoise.sort(Float::compareTo);
        rotationNoise.sort(Float::compareTo);
        
        float accelerationSample = accelerationNoise.get((int) ((accelerationNoise.size() - 1) * prefs.getFloat("movementNoiseRatioAcceleration", NOISE_RATIO_ACCELERATION)));
        float rotationSample = rotationNoise.get((int) ((rotationNoise.size() - 1) * prefs.getFloat("movementNoiseRatioRotation", NOISE_RATIO_ROTATION)));
        
        // Multiply factors
        accelerationSample *= prefs.getFloat("movementNoiseFactorAcceleration", NOISE_FACTOR_ACCELERATION);
        rotationSample *= prefs.getFloat("movementNoiseFactorRotation", NOISE_FACTOR_ROTATION);

        // Persist
        SharedPreferences.Editor edit = prefs.edit();

        Log.i(TAG, "Calibration finished - acc: " + accelerationSample + " rot: " + rotationSample);

        edit.putFloat("movementThresholdAcceleration", accelerationSample);
        edit.putFloat("movementThresholdRotation", rotationSample);
        
        edit.apply();
    }

    public int getLengthWindowGravity() {
        return prefs.getInt("movementLengthWindowGravity", 10);
    }

    public int getLengthWindowNoise() {
        return prefs.getInt("movementLengthWindowNoise", 5);
    }

    public int getLengthThreshold() {
        return prefs.getInt("movementLengthThreshold", 50);
    }

    public int getLengthGravity() {
        return prefs.getInt("movementLengthGravity", 1000);
    }

    public float getSensitivity() {
        return prefs.getFloat("movementSensitivity", SENSITIVITY);
    }

    public float getThresholdAcceleration() {
        return prefs.getFloat("movementThresholdAcceleration", 0.03f);
    }

    public float getThresholdRotation() {
        return prefs.getFloat("movementThresholdRotation", 0.01f);
    }

    public boolean getEnableGravityRotation() {
        return prefs.getBoolean("movementEnableGravityRotation", ENABLE_GRAVITY_ROTATION);
    }

}
