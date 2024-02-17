package ch.virt.smartphonemouse.mouse.processing.math;

import org.joml.Quaternionf;
import org.joml.Vector3f;

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
     * Normalizes this vector
     */
    public Vec3f normalize() {
        float magnitude = this.abs();

        this.x /= magnitude;
        this.y /= magnitude;
        this.z /= magnitude;

        return this;
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

        // construct quaternion
        // see https://developer.android.com/reference/android/hardware/SensorEvent.html#sensor.type_gyroscope:
        float magnitude = rotation.abs();
        rotation.normalize();

        float theta = magnitude / 2;
        float sinTheta = (float) Math.sin(theta);
        float cosTheta = (float) Math.cos(theta);

        // we use joml now
        Quaternionf q = new Quaternionf(
                sinTheta * rotation.x,
                sinTheta * rotation.y,
                sinTheta * rotation.z,
                cosTheta
        );

        Vector3f vector = new Vector3f(this.x , this.y, this.z);
        q.transform(vector);

        this.x = vector.x;
        this.y = vector.y;
        this.z = vector.z;

        return this;
    }
}
