package ch.virt.smartphonemouse.mouse;

import ch.virt.smartphonemouse.mouse.elements.Freezer;
import ch.virt.smartphonemouse.mouse.elements.Integration;
import ch.virt.smartphonemouse.mouse.elements.LowPassFilter;
import ch.virt.smartphonemouse.mouse.elements.NoiseCancelling;
import ch.virt.smartphonemouse.mouse.elements.PersistentIntegration;
import ch.virt.smartphonemouse.mouse.elements.Scaler;
import ch.virt.smartphonemouse.mouse.elements.SignCache;

public class Pipeline {

    private PipelineConfig config;

    private LowPassFilter lowPass;
    private Freezer freezer;
    private NoiseCancelling noise;
    private PersistentIntegration velocityIntegration;
    private Integration distanceIntegration;
    private SignCache cache;
    private Scaler scaler;

    public Pipeline(PipelineConfig config) {
        this.config = config;
    }

    public void create(){

        velocityIntegration = new PersistentIntegration();
        distanceIntegration = new Integration();

        lowPass = new LowPassFilter(config.getLowPassOrder(), 500, config.getLowPassCutoff());

        freezer = new Freezer(config.getFreezerFreezingThreshold(), config.getFreezerUnfreezingThreshold(), config.getFreezerUnfreezingSamples());

        noise = new NoiseCancelling(config.getNoiseCancellingThreshold(), config.getNoiseFinalSamples(), velocityIntegration);

        cache = new SignCache(config.getCacheMinimalDuration(), config.getCacheMaximalDuration(), config.getCacheReleasingThreshold(), velocityIntegration, distanceIntegration);

        scaler = new Scaler(config.getScalerPower(), config.getScalerSplit());
    }

    public float nextForDistance(float delta, float unfiltered){

        float subtract = (float) lowPass.filter(unfiltered); // Low pass for subtraction base
        subtract = freezer.next(subtract, unfiltered); // Freeze if necessary

        float acceleration = unfiltered - subtract; // Subtract the Low Passed and frozen
        acceleration = noise.cancel(acceleration); // Cancel the acceleration if too low

        float velocity = velocityIntegration.integrate(delta, acceleration); // Integrate velocity
        velocity = cache.velocity(velocity, acceleration); // Do first caching action
        velocity = scaler.scale(velocity); // Scale velocity

        float distance = distanceIntegration.integrate(delta, velocity); // Integrate distance
        distance = cache.distance(distance, delta); // Do second caching action

        return distance;
    }

    public void reset(){
        velocityIntegration.reset();
        distanceIntegration.reset();
        lowPass.reset();
    }

    public void clear(){

    }

}
