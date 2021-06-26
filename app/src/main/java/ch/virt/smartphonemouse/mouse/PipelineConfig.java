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
    private float scalerSplit;

    public PipelineConfig() {
        this.lowPassOrder = 1;
        this.lowPassCutoff = 0.1f;
        this.freezerFreezingThreshold = 0.1f;
        this.freezerUnfreezingThreshold = 0.04f;
        this.freezerUnfreezingSamples = 10;
        this.noiseCancellingThreshold = 0.04f;
        this.noiseFinalSamples = 20;
        this.cacheMinimalDuration = 5;
        this.cacheMaximalDuration = 10;
        this.cacheReleasingThreshold = 0.05f;
        this.scalerPower = 2;
        this.scalerSplit = 0.1f;
    }

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

    public float getScalerSplit() {
        return scalerSplit;
    }
}
