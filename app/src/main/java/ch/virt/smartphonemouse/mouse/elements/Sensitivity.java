package ch.virt.smartphonemouse.mouse.elements;

public class Sensitivity {

    private final int base;
    private final float power;

    public Sensitivity(int base, float power) {
        this.base = base;
        this.power = power;
    }

    public Sensitivity(float power) {
        this.power = power;
        base = 2; // Two, so one unit is double

        System.out.println("Power " + Math.pow(base, power));
    }

    public float scale(float value){
        return (float) (value * Math.pow(base, power));
    }
}
