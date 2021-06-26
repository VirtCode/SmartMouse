package ch.virt.smartphonemouse.mouse.elements;

public class SignCache {

    private final int minimalDuration;
    private final int maximalDuration;
    private final float releasingThreshold;

    private final Integration velocityIntegration;
    private final Integration distanceIntegration;

    public SignCache(int minimalDuration, int maximalDuration, float releasingThreshold, Integration velocityIntegration, Integration distanceIntegration) {
        this.minimalDuration = minimalDuration;
        this.maximalDuration = maximalDuration;
        this.releasingThreshold = releasingThreshold;
        this.velocityIntegration = velocityIntegration;
        this.distanceIntegration = distanceIntegration;
    }

    private boolean caching;
    private boolean latelyChanged;
    private float lastValue;
    private int duration;
    private float cachedDistance;

    public float velocity(float value, float acceleration){
        if (value != 0 && lastValue != 0 && (lastValue > 0 != value > 0)){ // If sign switched start caching
            caching = true;
            duration = 0;
            cachedDistance = 0;
        }

        lastValue = value;

        if (caching){

            if (duration >= minimalDuration) {

                if (Math.abs(acceleration) >= releasingThreshold){ // If releasing soon enough, just return to normal
                    caching = false;
                    latelyChanged = true;
                    return value;
                }

                if (duration >= maximalDuration){ // If not released, reset velocity
                    velocityIntegration.reset();
                    caching = false;
                    return 0;
                }
            }

            duration++;
            return 0;
        }

        return value;
    }

    public float distance(float value, float delta){
        if (caching){ // If caching, add values up

            cachedDistance += distanceIntegration.integrate(delta, lastValue);

            return 0;
        }else if (latelyChanged) { // If changed to non caching, release distance Cache
            latelyChanged = false;

            return value + cachedDistance;

        }else return value;
    }

}
