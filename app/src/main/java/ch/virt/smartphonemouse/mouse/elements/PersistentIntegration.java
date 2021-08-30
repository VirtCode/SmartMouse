package ch.virt.smartphonemouse.mouse.elements;

/**
 * This class handles integration that is persistent (just like normal integration)
 */
public class PersistentIntegration extends Integration {

    private float whole;

    @Override
    public float integrate(float delta, float value) {

        whole += super.integrate(delta, value);
        return whole;

    }

    @Override
    public void reset() {
        super.reset();

        whole = 0;
    }
}
