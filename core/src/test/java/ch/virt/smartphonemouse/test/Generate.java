package ch.virt.smartphonemouse.test;

import ch.virt.smartphonemouse.mouse.processing.math.Vec2f;
import ch.virt.smartphonemouse.mouse.processing.math.Vec3f;
import ch.virt.smartphonemouse.test.framework.DebugCsv;

import java.io.File;
import java.io.IOException;

/**
 * The usefulness and significance of these tests / signals here may be questioned, as I generate it with the same API used for the processing later.
 * So errors in this API might lead to wrong generated Signals, which the processing might revert.
 * Anyhow, it was good enough for the small experiments I used that for.
 */
public class Generate {

    public static void main(String[] args) throws IOException {
        generateSignalZTest(500, 1, 1, "ztst");
        generateSignal2d(500, 1, 1, 1, "2tst");
    }

    /**
     * Generates a rotation signal. It is composed of three stages:
     * - Stage pre: acceleration vector is steady and points towards x
     * - Stage rot: acceleration vector rotates around z-axis, while there is some additional acceleration on the x axis (half a sine)
     * - Stage post: acceleration vector is steady as in state pre
     * @param rate sampling rate to generate
     * @param pre duration for pre state
     * @param rot duration for rot state
     * @param post duration for post state
     */
    public static void generateSignal2d(int rate, float pre, float rot, float post, String id) throws IOException {
        DebugCsv output = new DebugCsv();
        output.registerColumn("time", Float.class);
        output.registerColumn("acceleration", Vec3f.class);
        output.registerColumn("angular-velocity", Vec3f.class);

        output.start(new File("test/data/data-" + id + ".csv"));

        Vec3f acceleration = new Vec3f(1, 0, 0);

        float delta = 1f / rate;
        int prePhase = (int) (pre * rate);
        int rotPhase = (int) (rot * rate);
        int postPhase = (int) (post * rate);

        float time = 0;

        // pre
        for (int i = 0; i < prePhase; i++) {
            output.stageFloat(time);
            output.stageVec3f(acceleration);
            output.stageVec3f(new Vec3f());

            output.commit();
            time += delta;
        }

        // rot
        float current = 0;
        float step = (float) (Math.PI * 2 / rotPhase);

        for (int i = 0; i < rotPhase; i++) {
            output.stageFloat(time);

            Vec2f rotated = acceleration.xy().rotate(current += step);
            output.stageVec3f(new Vec3f((float) (rotated.x + Math.sin(current)), rotated.y, 0));
            output.stageVec3f(new Vec3f(0, 0, step / delta));

            output.commit();
            time += delta;
        }

        // post
        for (int i = 0; i < postPhase; i++) {
            output.stageFloat(time);
            output.stageVec3f(acceleration);
            output.stageVec3f(new Vec3f());

            output.commit();
            time += delta;
        }

        output.end();
    }

    /**
     * Generates a 90° rotation around the z axis, along with a gravity vector of 1m/s² towards xz.
     * Does NOT correct gravity for rotation. This is purely to see a rotation being done correctly.
     * @param rate sampling rate to generate
     * @param pre duration of no rotation phase, just the gravity vector.
     * @param rot duration of the rotation phase.
     * @param id id to save it as
     */
    public static void generateSignalZTest(int rate, float pre, float rot, String id) throws IOException {
        DebugCsv output = new DebugCsv();
        output.registerColumn("time", Float.class);
        output.registerColumn("acceleration", Vec3f.class);
        output.registerColumn("angular-velocity", Vec3f.class);

        output.start(new File("test/data/data-" + id + ".csv"));

        float delta = 1f / rate;
        int prePhase = (int) (pre * rate);
        int rotPhase = (int) (rot * rate);

        Vec3f gravity = new Vec3f((float) (1 / Math.sqrt(2)), 0f, (float) (1 / Math.sqrt(2)));

        float time = 0;
        for (int i = 0; i < prePhase; i++) {
            output.stageFloat(time);
            output.stageVec3f(gravity);
            output.stageVec3f(new Vec3f());
            output.commit();

            time += delta;
        }

        float rotation = (float) (Math.PI / 2  / rotPhase);
        for (int i = 0; i < rotPhase; i++) {
            output.stageFloat(time);
            output.stageVec3f(gravity);
            output.stageVec3f(new Vec3f(0, 0, rotation / delta));
            output.commit();

            time += delta;
        }

        output.end();
    }
}
