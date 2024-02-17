package ch.virt.smartphonemouse.mouse.processing.implement;

import com.google.gson.annotations.Expose;

public class ProcessingParameters {

    public ProcessingParameters(int lengthWindowGravity, int lengthWindowNoise, int lengthThreshold, int lengthGravity, float sensitivity, float thresholdAcceleration, float thresholdRotation, boolean gravityRotation) {
        this.lengthWindowGravity = lengthWindowGravity;
        this.lengthWindowNoise = lengthWindowNoise;
        this.lengthThreshold = lengthThreshold;
        this.lengthGravity = lengthGravity;
        this.sensitivity = sensitivity;
        this.thresholdAcceleration = thresholdAcceleration;
        this.thresholdRotation = thresholdRotation;
        this.gravityRotation = gravityRotation;
    }

    /**
     * Length in samples of the average window used for eliminating gravity in the activation detection
     */
    public final int lengthWindowGravity;

    /**
     * Length in samples of the average window used to remove noise in the activation detection
     */
    public final int lengthWindowNoise;

    /**
     * Number of samples to be under the threshold to declare the device as inactive
     */
    public final int lengthThreshold;

    /**
     * Length in samples of the window used to determine the gravity vector when the device is inactive
     */
    public final int lengthGravity;

    /**
     * Factor to multiply the resulting distance (in m/s) by before to sending to the computer
     */
    public final float sensitivity;

    /**
     * Threshold for the acceleration to surpass for the device to be active
     */
    public final float thresholdAcceleration;

    /**
     * Threshold for the angular velocity to surpass for the device to be active
     */
    public final float thresholdRotation;

    /**
     * Whether to perform experimental gravity rotation
     */
    public final boolean gravityRotation;
}
