package ch.virt.smartphonemouse.mouse.elements;

/**
 * This class handles basic integration that is not persistent and can is used for the signal processing.
 */
public class Integration {

    private float last;

    /**
     * Integrates with the newly given sample.
     *
     * @param delta time delta since the last sample
     * @param value new sample
     * @return integrated delta
     */
    public float integrate(float delta, float value) {
        return ((last + (last = value)) / 2) * delta;
    }

    /**
     * Resets previously stored variables.
     */
    public void reset() {
        last = 0;
    }
}
