package ch.virt.smartphonemouse.mouse.processing.components;

/**
 * This class represents a threshold which operates based on two values.
 * It uses the values in an OR fashion, activating when one or both values surpass their threshold.
 * It also takes a time to drop off, after the last active value pair.
 */
public class OrThreshold {

    private final int dropoff;
    private final float firstThreshold, secondThreshold;

    private int lastActive;

    /**
     * @param dropoff amount of samples it takes from the last active sample to be inactive
     * @param firstThreshold threshold the first value has to surpass
     * @param secondThreshold threshold the second value has to surpass
     */
    public OrThreshold(int dropoff, float firstThreshold, float secondThreshold) {
        this.dropoff = dropoff;
        this.firstThreshold = firstThreshold;
        this.secondThreshold = secondThreshold;

        this.lastActive = dropoff;
    }

    /**
     * Takes two new values and determines whether the threshold is activated
     * @param first first value
     * @param second second value
     * @return whether the threshold is active
     */
    public boolean active(float first, float second) {
        if (first > firstThreshold || second > secondThreshold) lastActive = 0;
        else lastActive++;

        return lastActive <= dropoff;
    }


}
