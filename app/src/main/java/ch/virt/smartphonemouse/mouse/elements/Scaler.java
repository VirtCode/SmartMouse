package ch.virt.smartphonemouse.mouse.elements;

public class Scaler {

    private final boolean enabled;
    private final int power;
    private final float split;

    public Scaler(boolean enabled, int power, float split) {
        this.enabled = enabled;
        this.power = power;
        this.split = split;
    }

    public float scale(float value){
        if (!enabled) return value;

        return (float) (Math.pow(Math.abs(value), power) * (split / Math.pow(split, power))) * (value > 0 ? 1 : -1);
    }

}
