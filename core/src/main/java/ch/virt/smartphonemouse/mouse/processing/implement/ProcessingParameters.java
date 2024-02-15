package ch.virt.smartphonemouse.mouse.processing.implement;

public interface ProcessingParameters {

    int getLengthWindowGravity();

    int getLengthWindowNoise();

    int getLengthThreshold();

    int getLengthGravity();

    float getSensitivity();

    float getThresholdAcceleration();

    float getThresholdRotation();

    boolean getEnableGravityRotation();

}
