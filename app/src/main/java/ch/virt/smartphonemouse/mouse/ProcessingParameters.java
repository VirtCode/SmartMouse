package ch.virt.smartphonemouse.mouse;

public class ProcessingParameters {

    private int activeGravityWidth = 10;
    private int activeNoiseWidth = 5;
    private float activeAccelerationThreshold = 0.03f;
    private float activeRotationThreshold = 0.01f;

    private int activeThresholdDropoff = 50;

    private int gravityInactiveWidth = 2000;

    private int sensitivity = 15000;

    public int getActiveGravityWidth() {
        return activeGravityWidth;
    }

    public int getActiveNoiseWidth() {
        return activeNoiseWidth;
    }

    public float getActiveAccelerationThreshold() {
        return activeAccelerationThreshold;
    }

    public float getActiveRotationThreshold() {
        return activeRotationThreshold;
    }

    public int getActiveThresholdDropoff() {
        return activeThresholdDropoff;
    }

    public int getGravityInactiveWidth() {
        return gravityInactiveWidth;
    }

    public int getSensitivity() {
        return sensitivity;
    }
}
