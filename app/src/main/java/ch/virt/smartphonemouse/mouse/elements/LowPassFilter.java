package ch.virt.smartphonemouse.mouse.elements;

import uk.me.berndporr.iirj.Bessel;

public class LowPassFilter extends Bessel {

    public LowPassFilter(int order, int sampleRate, float cutoff) {
        super();
        lowPass(order, sampleRate, cutoff);
    }
}
