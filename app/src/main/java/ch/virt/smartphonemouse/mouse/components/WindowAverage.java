package ch.virt.smartphonemouse.mouse.components;

public class WindowAverage {

    private int index;
    private float[] elements;

    public WindowAverage(int length) {
        this.elements = new float[length];
        index = 0;
    }

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



}
