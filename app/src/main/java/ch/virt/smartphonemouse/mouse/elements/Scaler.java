package ch.virt.smartphonemouse.mouse.elements;

/**
 * This class is a component for the signal processing that scales the signal.
 * The signal gets scaled so that low values get even lower, and high values even higher.
 */
public class Scaler {

    private final boolean enabled;
    private final int power;
    private final float split;

    /**
     * Creates a scaler.
     *
     * @param enabled whether the scaler is enabled
     * @param power   the power of the function that is used for scaling
     * @param split   values over this value get higher, values lower than it get lower
     */
    public Scaler(boolean enabled, int power, float split) {
        this.enabled = enabled;
        this.power = power;
        this.split = split;
    }

    /**
     * Scales a value with the function.
     *
     * @param value value that is scaled
     * @return the scaled value
     */
    public float scale(float value) {
        if (!enabled) return value;

        return (float) (Math.pow(Math.abs(value), power) * (split / Math.pow(split, power))) * (value > 0 ? 1 : -1);
    }
}
