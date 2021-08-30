package ch.virt.smartphonemouse.mouse.elements;

/**
 * This component of the signal processing is used to scale the signal by a fixed factor.
 * The factor of this scaling is made up of a base and a power, because (i think) humans do only feel exponential increase.
 */
public class Sensitivity {

    private final int base;
    private final float power;

    private float factor;

    /**
     * Creates a sensitivity component.
     *
     * @param base  base of the factor
     * @param power power of the factor
     */
    public Sensitivity(int base, float power) {
        this.base = base;
        this.power = power;

        factor = (float) Math.pow(base, power); // Factor can be pre calculated since the base and power do not change
    }

    /**
     * Creates a sensitivity component with the base of two.
     *
     * @param power power of the factor
     */
    public Sensitivity(float power) {
        this(2, power); // Two, so one unit is double
    }

    /**
     * Scales the value by the factor.
     *
     * @param value value to be scaled
     * @return scaled value
     */
    public float scale(float value) {
        return value * factor;
    }
}
