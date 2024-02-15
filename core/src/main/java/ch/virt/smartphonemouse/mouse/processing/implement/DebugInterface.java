package ch.virt.smartphonemouse.mouse.processing.implement;

import ch.virt.smartphonemouse.mouse.processing.math.Vec2f;
import ch.virt.smartphonemouse.mouse.processing.math.Vec3f;

public interface DebugInterface {

    /**
     * Registers a column for transmission
     * @param name column name
     * @param type data type of column
     */
    void registerColumn(String name, Class<?> type);

    /**
     * Stages a 3d float vector
     */
    void stageVec3f(Vec3f data);

    /**
     * Stages a 2d float vector
     */
    void stageVec2f(Vec2f data);

    /**
     * Stages a float
     */
    void stageFloat(float data);

    /**
     * Stages a double
     */
    void stageDouble(double data);

    /**
     * Stages an integer
     */
    void stageInteger(int data);

    /**
     * Stages a long
     */
    void stageLong(long data);

    /**
     * Stages a boolean
     */
    void stageBoolean(boolean data);

    /**
     * Commits current staged data and transmits it to the server
     */
    void commit();
}
