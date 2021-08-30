package ch.virt.smartphonemouse.mouse.elements;

/**
 * The sign cache component caches the velocity after the sign switches. If quickly after that, the acceleration rises drastically, the cached values get used again.
 * This component is used to remove invalid movements that occur after a sign change.
 */
public class SignCache {

    private final int minimalDuration;
    private final int maximalDuration;
    private final float releasingThreshold;

    private final Integration velocityIntegration;
    private final Integration distanceIntegration;

    private boolean caching;
    private boolean latelyChanged;
    private float lastValue;
    private int duration;
    private float cachedDistance;

    /**
     * Creates a sign cache.
     *
     * @param minimalDuration     minimal duration the signal gets cached
     * @param maximalDuration     maximal duration the signal gets cached
     * @param releasingThreshold  how high the acceleration has to be in order for the values in the cache to get used again
     * @param velocityIntegration integration for the velocity that may be cleared
     * @param distanceIntegration integration for the distance so the distance loss can be compensated
     */
    public SignCache(int minimalDuration, int maximalDuration, float releasingThreshold, Integration velocityIntegration, Integration distanceIntegration) {
        this.minimalDuration = minimalDuration;
        this.maximalDuration = maximalDuration;
        this.releasingThreshold = releasingThreshold;
        this.velocityIntegration = velocityIntegration;
        this.distanceIntegration = distanceIntegration;
    }

    /**
     * Potentially caches the velocity.
     *
     * @param value        current velocity value
     * @param acceleration current acceleration value
     * @return possibly canceled velocity
     */
    public float velocity(float value, float acceleration) {
        if (value != 0 && lastValue != 0 && (lastValue > 0 != value > 0)) { // If sign switched start caching
            caching = true;
            duration = 0;
            cachedDistance = 0;
        }

        lastValue = value;

        if (caching) {

            if (duration >= minimalDuration) {

                if (Math.abs(acceleration) >= releasingThreshold) { // If releasing soon enough, just return to normal
                    caching = false;
                    latelyChanged = true;
                    return value;
                }

                if (duration >= maximalDuration) { // If not released, reset velocity
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

    /**
     * Potentially caches the moved distance.
     *
     * @param value current distance value
     * @param delta time delta since the last value
     * @return possibly cached distance
     */
    public float distance(float value, float delta) {
        if (caching) { // If caching, add values up

            cachedDistance += distanceIntegration.integrate(delta, lastValue);

            return 0;
        } else if (latelyChanged) { // If changed to non caching, release distance Cache
            latelyChanged = false;

            return value + cachedDistance;

        } else return value;
    }
}
