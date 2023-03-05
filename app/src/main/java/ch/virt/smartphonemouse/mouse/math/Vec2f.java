package ch.virt.smartphonemouse.mouse.math;

public class Vec2f {
    public float x, y;

    public Vec2f(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vec2f() {}

    public float abs() {
        return (float) Math.sqrt(x*x + y*y);
    }

    public Vec2f copy() {
        return new Vec2f(x, y);
    }

    public Vec2f add(Vec2f other) {
        x += other.x;
        y += other.y;

        return this;
    }

    public Vec2f subtract(Vec2f other) {
        x -= other.x;
        y -= other.y;

        return this;
    }

    public Vec2f multiply(float factor) {
        x *= factor;
        y *= factor;

        return this;
    }

    public Vec2f divide(float divisor) {
        x /= divisor;
        y /= divisor;

        return this;
    }

    public Vec2f mean(Vec2f second) {
        return this.copy().add(second).divide(2);
    }

    public Vec2f rotate(float rotation) {
        float c = (float) Math.cos(rotation);
        float s = (float) Math.sin(rotation);

        float newX = c * x + (-s) * y;
        float newY = s * x + c * y;

        this.x = newX;
        this.y = newY;

        return this;
    }
}
