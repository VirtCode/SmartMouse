package ch.virt.smartphonemouse.mouse;

import android.content.SharedPreferences;

/**
 * This class loads and contains all configuration data needed to configure a single pipeline.
 */
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
    private boolean scalerEnabled;
    private int scalerPower;
    private float scalerSplit;

    // Sensitivity
    private float sensitivityFactor;

    /**
     * Loads all config entries into this class.
     *
     * @param preferences preferences to load from.
     */
    public PipelineConfig(SharedPreferences preferences) {
        lowPassOrder = preferences.getInt("movementLowPassOrder", 1);
        lowPassCutoff = preferences.getFloat("movementLowPassCutoff", 0.1f);

        freezerFreezingThreshold = preferences.getFloat("movementFreezerFreezingThreshold", 0.1f);
        freezerUnfreezingThreshold = preferences.getFloat("movementFreezerUnfreezingThreshold", 0.04f);
        freezerUnfreezingSamples = preferences.getInt("movementFreezerUnfreezingSamples", 10);

        noiseCancellingThreshold = preferences.getFloat("movementNoiseThreshold", 0.04f);
        noiseFinalSamples = preferences.getInt("movementNoiseResetSamples", 20);

        cacheMinimalDuration = preferences.getInt("movementCacheDurationMinimal", 5);
        cacheMaximalDuration = preferences.getInt("movementCacheDurationMaximal", 10);
        cacheReleasingThreshold = preferences.getFloat("movementCacheReleaseThreshold", 0.05f);

        scalerEnabled = preferences.getBoolean("movementScaleEnable", true);
        scalerPower = preferences.getInt("movementScalePower", 2);
        scalerSplit = preferences.getFloat("movementScaleSplit", 0.1f);

        sensitivityFactor = preferences.getFloat("movementSensitivity", 13);
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

    public boolean isScalerEnabled() {
        return scalerEnabled;
    }

    public float getSensitivityFactor() {
        return sensitivityFactor;
    }
}
