package ch.virt.smartphonemouse.mouse.processing.components;

import ch.virt.smartphonemouse.mouse.processing.math.Vec3f;

/**
 * This class represents an average which operates in a window, remembering a given value of past samples to do the averaging with.
 * Here with a Vec3f as a value.
 */
public class WindowAverage3f {

    private int index;
    private Vec3f[] elements;

    /**
     * @param length length of the window in values
     */
    public WindowAverage3f(int length) {
        this.elements = new Vec3f[length];
        index = 0;
    }

    /**
     * Takes the next sample and calculates the current average
     * @param next next sample
     * @return current average, including next sample
     */
    public Vec3f avg(Vec3f next) {

        elements[index % elements.length] = next;
        index++;


        Vec3f total = new Vec3f();
        int amount = Math.min(elements.length, index);
        for (int i = 0; i < amount; i++) {
            total.add(elements[i]);
        }

        return total.divide(amount);
    }

    /**
     * Resets the average
     */
    public void reset() {
        elements = new Vec3f[elements.length];
        index = 0;
    }
}
