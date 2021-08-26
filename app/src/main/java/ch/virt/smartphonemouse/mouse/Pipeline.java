package ch.virt.smartphonemouse.mouse;

import ch.virt.smartphonemouse.mouse.elements.Freezer;
import ch.virt.smartphonemouse.mouse.elements.Integration;
import ch.virt.smartphonemouse.mouse.elements.LowPassFilter;
import ch.virt.smartphonemouse.mouse.elements.NoiseCancelling;
import ch.virt.smartphonemouse.mouse.elements.PersistentIntegration;
import ch.virt.smartphonemouse.mouse.elements.Scaler;
import ch.virt.smartphonemouse.mouse.elements.Sensitivity;
import ch.virt.smartphonemouse.mouse.elements.SignCache;

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

    public Pipeline(int sampleRate, PipelineConfig config) {
        this.config = config;
        this.sampleRate = sampleRate;
        create();
    }

    public void create(){

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

    public float nextForDistance(float delta, float unfiltered){
        int i = 0;
        if (debugging) debuggingValues[i++] = unfiltered;

        float subtract = (float) lowPass.filter(unfiltered); // Low pass for subtraction base
        if (debugging) debuggingValues[i++] = subtract;
        subtract = freezer.next(subtract, unfiltered); // Freeze if necessary
        if (debugging) debuggingValues[i++] = subtract;

        float acceleration = unfiltered - subtract; // Subtract the Low Passed and frozen
        if (debugging) debuggingValues[i++] = acceleration;
        acceleration = noise.cancel(acceleration); // Cancel the acceleration if too low
        if (debugging) debuggingValues[i++] = subtract;

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

    public void enableDebugging(){
        debugging = true;
    }

    public float[] getDebuggingValues() {
        return debuggingValues;
    }

    public void reset(){
        velocityIntegration.reset();
        distanceIntegration.reset();
        lowPass.reset();
    }

    public void clear(){

    }

}
