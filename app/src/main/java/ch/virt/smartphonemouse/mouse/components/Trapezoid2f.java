package ch.virt.smartphonemouse.mouse.components;

import ch.virt.smartphonemouse.mouse.math.Vec2f;

public class Trapezoid2f {

    private Vec2f last;

    public Vec2f trapezoid(float delta, Vec2f next) {
        if (last == null) {
            last = next;
            return next;
        }

        Vec2f result = last.mean(next).multiply(delta);
        last = next;
        return result;
    }

}
