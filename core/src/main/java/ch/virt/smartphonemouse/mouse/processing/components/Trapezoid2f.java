package ch.virt.smartphonemouse.mouse.processing.components;

import ch.virt.smartphonemouse.mouse.processing.math.Vec2f;

/**
 * This class is used to calculate a number of following trapezoids. Here with a Vec2 as a value
 */
public class Trapezoid2f {

    private Vec2f last = new Vec2f();

    /**
     * Calculates the current trapezoid
     * @param delta delta time to the last value
     * @param next current value
     * @return trapezoid
     */
    public Vec2f trapezoid(float delta, Vec2f next) {
        Vec2f result = last.mean(next).multiply(delta);
        last = next;
        return result;
    }

    /**
     * Resets the last value
     */
    public void reset() {
        last = new Vec2f();
    }

}
