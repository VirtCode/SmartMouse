package ch.virt.smartphonemouse.mouse.elements;

import uk.me.berndporr.iirj.Bessel;

/**
 * This class wraps a bessel filter from the iirj library as a low pass filter and provides it for the signal processing.
 */
public class LowPassFilter extends Bessel {

    /**
     * Creates a low pass filter.
     *
     * @param order      order of the filter
     * @param sampleRate sample rate with which samples are expected
     * @param cutoff     cutoff frequency of the filter
     */
    public LowPassFilter(int order, int sampleRate, float cutoff) {
        super();
        lowPass(order, sampleRate, cutoff);
    }
}
