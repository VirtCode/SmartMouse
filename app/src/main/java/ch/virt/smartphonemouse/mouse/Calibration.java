package ch.virt.smartphonemouse.mouse;

import android.util.Log;
import ch.virt.smartphonemouse.mouse.processing.components.WindowAverage;
import ch.virt.smartphonemouse.mouse.processing.math.Vec3f;

import java.util.ArrayList;
import java.util.List;

public class Calibration {

    private static final String TAG = "Calibration";

    public static final int STATE_START = 0;
    public static final int STATE_SAMPLING = 1;
    public static final int STATE_NOISE = 3;
    public static final int STATE_END = 4;


    int state = STATE_START;
    private StateListener listener;

    boolean started = false;
    float startTime = 0;

    private int samples;

    private List<Float> accelerationNoise;
    private List<Float> rotationNoise;
    private WindowAverage gravityAverage;
    private WindowAverage noiseAverage;

    private final Parameters params;

    private float durationSampling;
    private float durationNoise;

    public Calibration(StateListener listener, Parameters params) {
        this.listener = listener;
        this.params = params;
    }

    public void setListener(StateListener listener) {
        this.listener = listener;
    }

    public void startCalibration() {
        Log.d(TAG, "Starting Sampling Rate Calibration");
        samples = 0;
        this.durationSampling = params.getCalibrationSamplingTime();

        updateState(STATE_SAMPLING);
    }

    private void startNoise() {
        Log.d(TAG, "Starting Noise Level Calibration");

        float samplingRate = samples / durationSampling;
        params.calibrateSamplingRate(samplingRate);

        accelerationNoise = new ArrayList<>();
        rotationNoise = new ArrayList<>();

        gravityAverage = new WindowAverage(params.getLengthWindowGravity());
        noiseAverage = new WindowAverage(params.getLengthWindowNoise());

        this.durationNoise = params.getCalibrationNoiseTime();

        updateState(STATE_NOISE);
    }

    private void endCalibration() {
        Log.d(TAG, "Ending Calibration");

        params.calibrateNoiseLevels(accelerationNoise, rotationNoise);
        params.setCalibrated(true);
        updateState(STATE_END);
    }

    public void data(float time, Vec3f acceleration, Vec3f angularVelocity) {
        if (!started) {
            startTime = time;
            started = true;
        }

        if (state == STATE_SAMPLING) {

            if (time - startTime > durationSampling) {
                startNoise();
            }

            samples++;


        } else if (state == STATE_NOISE) {

            if (time - startTime > durationNoise) {
                endCalibration();
            }

            float acc = acceleration.xy().abs();

            // Remove gravity or rather lower frequencies
            float gravity = gravityAverage.avg(acc);
            acc -= gravity;
            acc = Math.abs(acc);

            // Remove noise
            acc = noiseAverage.avg(acc);

            // Calculate the rotation activation
            float rot = Math.abs(angularVelocity.z);

            accelerationNoise.add(acc);
            rotationNoise.add(rot);
        }
    }

    private void updateState(int state) {
        this.state = state;
        started = false;

        listener.update(state);
    }

    public interface StateListener {
        void update(int state);
    }

}
