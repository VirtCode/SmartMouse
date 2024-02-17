package ch.virt.smartphonemouse.test.framework;

import ch.virt.smartphonemouse.mouse.processing.implement.DebugInterface;
import ch.virt.smartphonemouse.mouse.processing.math.Vec2f;
import ch.virt.smartphonemouse.mouse.processing.math.Vec3f;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A naive implementation of a debug interface which stores the values to a CSV file
 */
public class DebugCsv implements DebugInterface {

    public static final char DELIMITER = ',';
    public static final char LINES = '\n';

    List<String> columns;

    FileWriter current;
    List<Object> currentLine;

    public DebugCsv() {
        this.current = null;
        this.columns = new ArrayList<>();
    }

    /**
     * Starts a capture
     * @param file file to write the capture to
     * @throws IOException if something goes wrong with the files
     */
    public void start(File file) throws IOException {
        if (current != null) end();

        System.out.println("Starting debug capture");
        this.current = new FileWriter(file);

        boolean first = true;
        for (String s : columns) {
            if (!first) current.append(DELIMITER);
            else first = false;

            current.append(s);
        }
        current.append(LINES);

        current.flush();

        this.currentLine = new ArrayList<>(columns.size());
    }

    /**
     * Ends a capture
     * @throws IOException if something goes wrong with the files
     */
    public void end() throws IOException {
        this.current.flush();
        this.current.close();

        this.current = null;
    }

    @Override
    public void registerColumn(String name, Class<?> type) {
        // Vector Types
        if (Vec2f.class.equals(type)) {
            columns.add(name + "-x");
            columns.add(name + "-y");
        } else if (Vec3f.class.equals(type)) {
            columns.add(name + "-x");
            columns.add(name + "-y");
            columns.add(name + "-z");
        }

        else if (Float.class.equals(type)) columns.add(name);
        else if (Double.class.equals(type)) columns.add(name);
        else if (Integer.class.equals(type)) columns.add(name);
        else if (Long.class.equals(type)) columns.add(name);
        else if (Boolean.class.equals(type)) columns.add(name);

        else throw new IllegalArgumentException("unexpected column type");
    }

    @Override
    public void stageVec3f(Vec3f data) {
        currentLine.add(data.x);
        currentLine.add(data.y);
        currentLine.add(data.z);
    }

    @Override
    public void stageVec2f(Vec2f data) {
        currentLine.add(data.x);
        currentLine.add(data.y);
    }

    @Override
    public void stageFloat(float data) {
        currentLine.add(data);
    }

    @Override
    public void stageDouble(double data) {
        currentLine.add(data);
    }

    @Override
    public void stageInteger(int data) {
        currentLine.add(data);
    }

    @Override
    public void stageLong(long data) {
        currentLine.add(data);
    }

    @Override
    public void stageBoolean(boolean data) {
        currentLine.add(data);
    }

    @Override
    public void commit() {
        // if not opened, this will be null
        if (current == null) return;

        boolean first = true;

        try {
            for (Object o : currentLine) {
                if (!first) current.append(DELIMITER);
                else first = false;

                current.append(o.toString());
            }

            current.append(LINES);
        } catch (Exception e) {
            throw new RuntimeException("Failed to store to csv file");
        }

        currentLine.clear();
    }
}
