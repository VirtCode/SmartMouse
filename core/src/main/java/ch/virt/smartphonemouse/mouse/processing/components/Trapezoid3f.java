package ch.virt.smartphonemouse.mouse.processing.components;

import ch.virt.smartphonemouse.mouse.processing.math.Vec3f;

/**
 * This class is used to calculate a number of following trapezoids. Here with a Vec3 as a value
 */
public class Trapezoid3f {

    private Vec3f last = new Vec3f();

    /**
     * Calculates the current trapezoid
     * @param delta delta time to the last value
     * @param next current value
     * @return trapezoid
     */
    public Vec3f trapezoid(float delta, Vec3f next) {
        Vec3f result = last.mean(next).multiply(delta);
        last = next;
        return result;
    }

    /**
     * Resets the last value
     */
    public void reset() {
        last = new Vec3f();
    }

}
