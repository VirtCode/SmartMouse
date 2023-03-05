package ch.virt.smartphonemouse.mouse.math;

public class Vec3f {
    public float x, y, z;

    public Vec3f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vec3f() {}

    public Vec2f xy() {
        return new Vec2f(x, y);
    }

    public Vec2f yz() {
        return new Vec2f(y, z);
    }

    public Vec2f xz() {
        return new Vec2f(x, z);
    }

    public float abs() {
        return (float) Math.sqrt(x*x + y*y + z*z);
    }

    public Vec3f copy() {
        return new Vec3f(x, y, z);
    }

    public Vec3f add(Vec3f other) {
        x += other.x;
        y += other.y;
        z += other.z;

        return this;
    }

    public Vec3f subtract(Vec3f other) {
        x -= other.x;
        y -= other.y;
        z -= other.z;

        return this;
    }

    public Vec3f multiply(float factor) {
        x *= factor;
        y *= factor;
        z *= factor;

        return this;
    }

    public Vec3f divide(float divisor) {
        x /= divisor;
        y /= divisor;
        z /= divisor;

        return this;
    }

    public Vec3f mean(Vec3f second) {
        return this.copy().add(second).divide(2);
    }

    public Vec3f rotate(Vec3f rotation) {

        // Calculate sines and cosines that are used for optimization
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
