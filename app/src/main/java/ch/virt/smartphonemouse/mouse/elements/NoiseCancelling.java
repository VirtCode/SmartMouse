package ch.virt.smartphonemouse.mouse.elements;

public class NoiseCancelling {

    private final float cancellingThreshold;
    private final int finalCancellingSamples;

    private final Integration velocityIntegration;

    private int noise;

    public NoiseCancelling(float cancellingThreshold, int finalCancellingSamples, Integration velocityIntegration) {
        this.cancellingThreshold = cancellingThreshold;
        this.finalCancellingSamples = finalCancellingSamples;
        this.velocityIntegration = velocityIntegration;
    }

    public float cancel(float value){

        if (Math.abs(value) <= cancellingThreshold) { // Below Threshold
            noise++;
            return 0;
        }else noise = 0;

        return value;
    }

    public float velocity(float value){
        if (noise > finalCancellingSamples){
            velocityIntegration.reset();
            return 0; // Cancel if much noise
        }
        return value;
    }

}
