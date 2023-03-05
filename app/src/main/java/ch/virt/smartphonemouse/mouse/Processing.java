package ch.virt.smartphonemouse.mouse;

import ch.virt.smartphonemouse.mouse.components.*;
import ch.virt.smartphonemouse.mouse.math.Vec2f;
import ch.virt.smartphonemouse.mouse.math.Vec3f;

public class Processing {

    public Processing() {
        rotationDeltaTrapezoid = new Trapezoid3f();

        activeGravityAverage = new WindowAverage(10);
        activeNoiseAverage = new WindowAverage(5);
        activeThreshold = new MultiThreshold(50, 0.03f, 0.01f);

        gravityInactiveAverage = new WindowAverage3f(2000);
        gravityCurrent = new Vec3f();

        distanceVelocityTrapezoid = new Trapezoid2f();
        distanceDistanceTrapezoid = new Trapezoid2f();
        distanceVelocity = new Vec2f();
    }

    Trapezoid3f rotationDeltaTrapezoid;

    public Vec2f next(float delta, Vec3f acceleration, Vec3f angularVelocity) {
        Vec3f rotationDelta = rotationDeltaTrapezoid.trapezoid(delta, angularVelocity);
        boolean active = this.active(acceleration, angularVelocity);

        Vec2f linearAcceleration = gravity(active, acceleration, rotationDelta);

        return distance(delta, active, linearAcceleration, rotationDelta);

    }

    private WindowAverage activeGravityAverage;
    private WindowAverage activeNoiseAverage;

    private MultiThreshold activeThreshold;

    public boolean active(Vec3f acceleration, Vec3f angularVelocity) {

        // Calculate the acceleration activation
        float acc = acceleration.xy().abs();
        acc -= activeGravityAverage.avg(acc); // Remove gravity or rather lower frequencies
        acc = Math.abs(acc);

        acc = activeNoiseAverage.avg(acc); // Remove noise

        // Calculate the rotation activation
        float rot = Math.abs(angularVelocity.z);

        // Do the threshold
        return activeThreshold.active(acc, rot);
    }

    private WindowAverage3f gravityInactiveAverage;
    private Vec3f gravityCurrent;

    public Vec2f gravity(boolean active, Vec3f acceleration, Vec3f rotationDelta) {

        if (active) {
                gravityInactiveAverage.reset();

                gravityCurrent.rotate(rotationDelta);

        } else {
            gravityCurrent = gravityInactiveAverage.avg(acceleration);
        }

        return acceleration.xy().subtract(gravityCurrent.xy());
    }

    private Trapezoid2f distanceVelocityTrapezoid;
    private Vec2f distanceVelocity;

    private Trapezoid2f distanceDistanceTrapezoid;

    public Vec2f distance(float delta, boolean active, Vec2f linearAcceleration, Vec3f rotationDelta) {

        if (!active) linearAcceleration = new Vec2f();
        distanceVelocity.rotate(-rotationDelta.z);

        distanceVelocity.add(distanceVelocityTrapezoid.trapezoid(delta, linearAcceleration));

        if (!active) distanceVelocity = new Vec2f();

        return distanceDistanceTrapezoid.trapezoid(delta, distanceVelocity).multiply(10000); // quick and dirty factor -> 10 pixel per mm?
    }

}
