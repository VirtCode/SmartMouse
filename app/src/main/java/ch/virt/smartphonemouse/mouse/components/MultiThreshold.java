package ch.virt.smartphonemouse.mouse.components;

public class MultiThreshold {

    private int dropoff;
    private float firstThreshold, secondThreshold;

    private int lastActive;

    public MultiThreshold(int dropoff, float firstThreshold, float secondThreshold) {
        this.dropoff = dropoff;
        this.firstThreshold = firstThreshold;
        this.secondThreshold = secondThreshold;

        this.lastActive = dropoff;
    }

    public boolean active(float first, float second) {
        if (first > firstThreshold || second > secondThreshold) lastActive = 0;
        else lastActive++;

        return lastActive <= dropoff;
    }


}
