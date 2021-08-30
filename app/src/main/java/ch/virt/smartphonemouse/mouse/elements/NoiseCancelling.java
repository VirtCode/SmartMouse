package ch.virt.smartphonemouse.mouse.elements;

/**
 * This class holds the noise cancelling component for the signal processing.
 * The noise cancelling removes all amplitudes below a certain value. It also does support clearing the velocity if there has been only noise for a certain amount of time.
 */
public class NoiseCancelling {

    private final float cancellingThreshold;
    private final int finalCancellingSamples;

    private final Integration velocityIntegration;

    private int noise;

    /**
     * Creates a noise cancelling component
     *
     * @param cancellingThreshold    all signal below this threshold is regarded as noise
     * @param finalCancellingSamples how many samples in a row must be noise in order to reset the velocity
     * @param velocityIntegration    integration of the velocity that may be reset
     */
    public NoiseCancelling(float cancellingThreshold, int finalCancellingSamples, Integration velocityIntegration) {
        this.cancellingThreshold = cancellingThreshold;
        this.finalCancellingSamples = finalCancellingSamples;
        this.velocityIntegration = velocityIntegration;
    }

    /**
     * Process the values that may be cancelled, presumably the acceleration
     *
     * @param value current acceleration value
     * @return possibly canceled value
     */
    public float cancel(float value) {

        if (Math.abs(value) <= cancellingThreshold) { // Below Threshold
            noise++;
            return 0;
        } else noise = 0;

        return value;
    }

    /**
     * Process the velocity to potentially reset it
     *
     * @param value current velocity value
     * @return possibly reset velocity
     */
    public float velocity(float value) {
        if (noise > finalCancellingSamples) {
            velocityIntegration.reset();
            return 0; // Cancel if much noise
        }
        return value;
    }
}
