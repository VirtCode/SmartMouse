package ch.virt.smartphonemouse.mouse.elements;

/**
 * This class holds the freezer component of the signal processing pipeline.
 * The freezer ensures that the subtracted gravity gets occasionally frozen so that it is not affected by the movement of the smartphone.
 */
public class Freezer {

    private final float freezingThreshold;
    private final float unfreezingThreshold;
    private final int unfreezingSamples;

    private boolean frozen;
    private int unfreezing;
    private float subtract;

    /**
     * Creates a freezer component.
     *
     * @param freezingThreshold   how much acceleration is required for the freezer to kick in
     * @param unfreezingThreshold how less acceleration is required for the freezing process to end
     * @param unfreezingSamples   how many samples in a row must be below said threshold
     */
    public Freezer(float freezingThreshold, float unfreezingThreshold, int unfreezingSamples) {
        this.freezingThreshold = freezingThreshold;
        this.unfreezingThreshold = unfreezingThreshold;
        this.unfreezingSamples = unfreezingSamples;
    }

    /**
     * Calculates the next sample
     *
     * @param lowPassed    acceleration after the low pass filter (gravity)
     * @param acceleration unfiltered acceleration
     * @return new gravity value, that may be frozen
     */
    public float next(float lowPassed, float acceleration) {
        if (frozen) {

            if (Math.abs(acceleration - lowPassed) < unfreezingThreshold)
                unfreezing++; // Count up if it may be unfrozen
            else unfreezing = 0;

            if (unfreezing == unfreezingSamples) { // Unfreeze if enough samples in a row
                frozen = false;
                subtract = lowPassed;
            }

        } else {
            subtract = lowPassed;

            if (Math.abs(acceleration - subtract) > freezingThreshold) { // Freeze if below threshold
                frozen = true;
                unfreezing = 0;
            }
        }

        return subtract;
    }

    /**
     * Resets the current state of the freezer.
     */
    public void reset(){
        frozen = false;
        unfreezing = 0;
        subtract = 0;
    }
}
