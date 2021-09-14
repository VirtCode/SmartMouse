package ch.virt.smartphonemouse.mouse;

import ch.virt.smartphonemouse.mouse.elements.Freezer;
import ch.virt.smartphonemouse.mouse.elements.Integration;
import ch.virt.smartphonemouse.mouse.elements.LowPassFilter;
import ch.virt.smartphonemouse.mouse.elements.NoiseCancelling;
import ch.virt.smartphonemouse.mouse.elements.PersistentIntegration;
import ch.virt.smartphonemouse.mouse.elements.Scaler;
import ch.virt.smartphonemouse.mouse.elements.Sensitivity;
import ch.virt.smartphonemouse.mouse.elements.SignCache;

/**
 * This class handles all calculation to get from the raw sensor data to the distance output.
 * In order to do the calculation it uses various different components.
 */
public class Pipeline {

    private int sampleRate;
    private PipelineConfig config;

    private LowPassFilter lowPass;
    private Freezer freezer;
    private NoiseCancelling noise;
    private PersistentIntegration velocityIntegration;
    private Integration distanceIntegration;
    private SignCache cache;
    private Scaler scaler;
    private Sensitivity sensitivity;

    private boolean debugging;
    private float[] debuggingValues;

    /**
     * Creates a signal processing pipeline.
     *
     * @param sampleRate sample rate at which the new samples will be provided
     * @param config     configuration for all different components of this pipeline
     */
    public Pipeline(int sampleRate, PipelineConfig config) {
        this.config = config;
        this.sampleRate = sampleRate;
        create();
    }

    /**
     * Initializes all components.
     */
    private void create() {

        velocityIntegration = new PersistentIntegration();
        distanceIntegration = new Integration();

        lowPass = new LowPassFilter(config.getLowPassOrder(), sampleRate, config.getLowPassCutoff());

        freezer = new Freezer(config.getFreezerFreezingThreshold(), config.getFreezerUnfreezingThreshold(), config.getFreezerUnfreezingSamples());

        noise = new NoiseCancelling(config.getNoiseCancellingThreshold(), config.getNoiseFinalSamples(), velocityIntegration);

        cache = new SignCache(config.getCacheMinimalDuration(), config.getCacheMaximalDuration(), config.getCacheReleasingThreshold(), velocityIntegration, distanceIntegration);

        scaler = new Scaler(config.isScalerEnabled(), config.getScalerPower(), config.getScalerSplit());

        sensitivity = new Sensitivity(config.getSensitivityFactor());

        debuggingValues = new float[12];
    }

    /**
     * Calculates the current distance delta from a new acceleration sample
     *
     * @param delta      time passed since the last sample (in seconds)
     * @param unfiltered new acceleration sample from the accelerometer
     * @return new distance delta
     */
    public float nextForDistance(float delta, float unfiltered) {
        int i = 0;
        if (debugging) debuggingValues[i++] = unfiltered;

        float subtract = (float) lowPass.filter(unfiltered); // Low pass for subtraction base
        if (debugging) debuggingValues[i++] = subtract;
        subtract = freezer.next(subtract, unfiltered); // Freeze if necessary
        if (debugging) debuggingValues[i++] = subtract;

        float acceleration = unfiltered - subtract; // Subtract the Low Passed and frozen
        if (debugging) debuggingValues[i++] = acceleration;
        acceleration = noise.cancel(acceleration); // Cancel the acceleration if too low
        if (debugging) debuggingValues[i++] = acceleration;

        float velocity = velocityIntegration.integrate(delta, acceleration); // Integrate velocity
        if (debugging) debuggingValues[i++] = velocity;
        velocity = cache.velocity(velocity, acceleration); // Do first caching action
        if (debugging) debuggingValues[i++] = velocity;
        velocity = noise.velocity(velocity);
        if (debugging) debuggingValues[i++] = velocity;
        velocity = scaler.scale(velocity); // Scale velocity
        if (debugging) debuggingValues[i++] = velocity;

        float distance = distanceIntegration.integrate(delta, velocity); // Integrate distance
        if (debugging) debuggingValues[i++] = distance;
        distance = cache.distance(distance, delta); // Do second caching action
        if (debugging) debuggingValues[i++] = distance;
        distance = sensitivity.scale(distance); // Scale distance to its sensitivity
        if (debugging) debuggingValues[i++] = distance;

        return distance;
    }

    /**
     * Enables debugging for this pipeline.
     * This will place the values in between the acceleration and distance into an array for every sample.
     */
    public void enableDebugging() {
        debugging = true;
    }

    /**
     * Returns all values in between the acceleration and distance in an array.
     * Make sure to enable debugging to get content in this array.
     *
     * @return values in between
     */
    public float[] getDebuggingValues() {
        return debuggingValues;
    }

    /**
     * Resets the pipeline to its initial state.
     */
    public void reset() {
        lowPass.reset();
        freezer.reset();
        noise.reset();
        velocityIntegration.reset();
        cache.reset();
        distanceIntegration.reset();
    }
}
