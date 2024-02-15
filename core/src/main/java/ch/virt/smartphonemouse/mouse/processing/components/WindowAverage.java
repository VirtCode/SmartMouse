package ch.virt.smartphonemouse.mouse.processing.components;

/**
 * This class represents an average which operates in a window, remembering a given value of past samples to do the averaging with.
 */
public class WindowAverage {

    private int index;
    private float[] elements;

    /**
     * @param length length of the window in values
     */
    public WindowAverage(int length) {
        this.elements = new float[length];
        index = 0;
    }

    /**
     * Takes the next sample and calculates the current average
     * @param next next sample
     * @return current average, including next sample
     */
    public float avg(float next) {
        elements[index % elements.length] = next;
        index++;

        float total = 0f;
        int amount = Math.min(elements.length, index);
        for (int i = 0; i < amount; i++) {
            total += elements[i];
        }

        return total / amount;
    }

    /**
     * Resets the average
     */
    public void reset() {
        elements = new float[elements.length];
        index = 0;
    }
}
