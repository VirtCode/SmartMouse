package ch.virt.smartphonemouse.mouse;

public class PipelineConfig {

    // Acceleration Low Pass
    private int lowPassOrder;
    private float lowPassCutoff;

    // Acceleration Freezer
    private float freezerFreezingThreshold;
    private float freezerUnfreezingThreshold;
    private int freezerUnfreezingSamples;

    // Noise Cancelling
    private float noiseCancellingThreshold;
    private int noiseFinalSamples;

    // Sign Cache
    private int cacheMinimalDuration;
    private int cacheMaximalDuration;
    private float cacheReleasingThreshold;

    // Scaler
    private int scalerPower;
    private int scalerSplit;

    public int getLowPassOrder() {
        return lowPassOrder;
    }

    public float getLowPassCutoff() {
        return lowPassCutoff;
    }

    public float getFreezerFreezingThreshold() {
        return freezerFreezingThreshold;
    }

    public float getFreezerUnfreezingThreshold() {
        return freezerUnfreezingThreshold;
    }

    public int getFreezerUnfreezingSamples() {
        return freezerUnfreezingSamples;
    }

    public float getNoiseCancellingThreshold() {
        return noiseCancellingThreshold;
    }

    public int getNoiseFinalSamples() {
        return noiseFinalSamples;
    }

    public int getCacheMinimalDuration() {
        return cacheMinimalDuration;
    }

    public int getCacheMaximalDuration() {
        return cacheMaximalDuration;
    }

    public float getCacheReleasingThreshold() {
        return cacheReleasingThreshold;
    }

    public int getScalerPower() {
        return scalerPower;
    }

    public int getScalerSplit() {
        return scalerSplit;
    }
}
