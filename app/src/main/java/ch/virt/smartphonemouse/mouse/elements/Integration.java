package ch.virt.smartphonemouse.mouse.elements;

public class Integration {

    private float last;

    public float integrate(float delta, float value){
        return ((last + (last = value)) / 2) * delta;
    }

    public void reset(){
        last = 0;
    }
}
