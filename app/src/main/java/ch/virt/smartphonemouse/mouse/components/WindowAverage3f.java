package ch.virt.smartphonemouse.mouse.components;

import ch.virt.smartphonemouse.mouse.math.Vec3f;

public class WindowAverage3f {

    private int index;
    private Vec3f[] elements;

    public WindowAverage3f(int length) {
        this.elements = new Vec3f[length];
        index = 0;
    }

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

    public void reset() {
        elements = new Vec3f[elements.length];
        index = 0;
    }



}
