package ch.virt.smartphonemouse.mouse.math;

/**
 * This class represents a vector consisting of two floats.
 */
public class Vec2f {
    public float x, y;

    public Vec2f() {}
    public Vec2f(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Creates a copy of the vector
     */
    public Vec2f copy() {
        return new Vec2f(x, y);
    }

    /**
     * Returns the absolute value, aka. length of the vector
     */
    public float abs() {
        return (float) Math.sqrt(x*x + y*y);
    }

    /**
     * Adds another vector to this one
     */
    public Vec2f add(Vec2f other) {
        x += other.x;
        y += other.y;

        return this;
    }

    /**
     * Adds another vector to this one
     */
    public Vec2f subtract(Vec2f other) {
        x -= other.x;
        y -= other.y;

        return this;
    }

    /**
     * Multiplies or scales this vector by an amount
     */
    public Vec2f multiply(float factor) {
        x *= factor;
        y *= factor;

        return this;
    }

    /**
     * Divides this vector by a given amount
     */
    public Vec2f divide(float divisor) {
        x /= divisor;
        y /= divisor;

        return this;
    }

    /**
     * Calculates the average between this and a given other vector. Not affecting this instance.
     */
    public Vec2f mean(Vec2f second) {
        return this.copy().add(second).divide(2);
    }

    /**
     * Rotates this vector by a given rotation
     * @param rotation rotation in radians
     */
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
