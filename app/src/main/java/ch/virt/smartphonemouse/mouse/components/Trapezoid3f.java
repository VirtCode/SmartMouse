package ch.virt.smartphonemouse.mouse.components;

import ch.virt.smartphonemouse.mouse.math.Vec3f;

public class Trapezoid3f {

    private Vec3f last;

    public Vec3f trapezoid(float delta, Vec3f next) {
        if (last == null) {
            last = next;
            return next;
        }

        Vec3f result = last.mean(next).multiply(delta);
        last = next;
        return result;
    }

}
