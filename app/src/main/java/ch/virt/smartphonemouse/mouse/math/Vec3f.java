package ch.virt.smartphonemouse.mouse.math;

/**
 * This class represents a three-dimensional vector, based on floats
 */
public class Vec3f {
    public float x, y, z;

    public Vec3f() {}
    public Vec3f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Returns the x and y axes as a 2d vector
     */
    public Vec2f xy() {
        return new Vec2f(x, y);
    }

    /**
     * Returns the y and z axes as a 2d vector
     */
    public Vec2f yz() {
        return new Vec2f(y, z);
    }

    /**
     * Returns the x and z axes as a 2d vector
     */
    public Vec2f xz() {
        return new Vec2f(x, z);
    }

    /**
     * Returns the absolute value or length of the vector
     */
    public float abs() {
        return (float) Math.sqrt(x*x + y*y + z*z);
    }

    /**
     * Creates a copy of this vector
     */
    public Vec3f copy() {
        return new Vec3f(x, y, z);
    }

    /**
     * Adds another vector to this one
     */
    public Vec3f add(Vec3f other) {
        x += other.x;
        y += other.y;
        z += other.z;

        return this;
    }

    /**
     * Adds another vector to this one
     */
    public Vec3f subtract(Vec3f other) {
        x -= other.x;
        y -= other.y;
        z -= other.z;

        return this;
    }

    /**
     * Multiplies or scales this vector by an amount
     */
    public Vec3f multiply(float factor) {
        x *= factor;
        y *= factor;
        z *= factor;

        return this;
    }

    /**
     * Divides this vector by a given amount
     */
    public Vec3f divide(float divisor) {
        x /= divisor;
        y /= divisor;
        z /= divisor;

        return this;
    }

    /**
     * Negates this vector
     */
    public Vec3f negative() {
        return this.multiply(-1f);
    }

    /**
     * Calculates the average between this and a given other vector. Not affecting this instance.
     */
    public Vec3f mean(Vec3f second) {
        return this.copy().add(second).divide(2);
    }

    /**
     * Rotates this vector by all three axis by using euler angles in a xyz fashion
     * @param rotation vector containing the rotation for each axis as radians
     */
    public Vec3f rotate(Vec3f rotation) {

        // Calculate sines and cosines that are used (for optimization)
        float sa = (float) Math.sin(rotation.x);
        float ca = (float) Math.cos(rotation.x);
        float sb = (float) Math.sin(rotation.y);
        float cb = (float) Math.cos(rotation.y);
        float sc = (float) Math.sin(rotation.z);
        float cc = (float) Math.cos(rotation.z);

        // Apply the rotation (matrix used: xyz)
        float newX = cb * cc * x + cb * (-sc) * y + sb * z;
        float newY = ((-sa) * (-sb) * cb + ca * sc) * x + ((-sa) * (-sb) * (-sc) + ca * cc) * y + (-sa) * cb * z;
        float newZ = (ca * (-sb) * cc + sa * sc) * x + (ca * (-sb) * (-sc) + sa * cc) * y + ca * cb * z;

        this.x = newX;
        this.y = newY;
        this.z = newZ;

        return this;
    }
}
