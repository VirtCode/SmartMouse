package ch.virt.smartphonemouse.mouse.elements;

public class Freezer {

    private final float freezingThreshold;
    private final float unfreezingThreshold;
    private final int unfreezingSamples;

    private boolean frozen;
    private int unfreezing;
    private float subtract;

    public Freezer(float freezingThreshold, float unfreezingThreshold, int unfreezingSamples) {
        this.freezingThreshold = freezingThreshold;
        this.unfreezingThreshold = unfreezingThreshold;
        this.unfreezingSamples = unfreezingSamples;
    }

    public float next(float lowPassed, float acceleration){
        if (frozen){

            if (Math.abs(acceleration - lowPassed) < unfreezingThreshold) unfreezing++; // Count up if it may be unfrozen
            else unfreezing = 0;

            if (unfreezing == unfreezingSamples){ // Unfreeze if enough samples in a row
                frozen = false;
                subtract = lowPassed;
            }

        }else {
            subtract = lowPassed;

            if (Math.abs(acceleration - subtract) > freezingThreshold){ // Freeze if below threshold
                frozen = true;
                unfreezing = 0;
            }
        }

        return subtract;
    }
}
