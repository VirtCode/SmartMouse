package ch.virt.smartphonemouse.test.framework;

import ch.virt.smartphonemouse.mouse.processing.math.Vec3f;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Reads the data from a csv file
 */
public class DataCsv implements Iterable<DataCsv.Data> {

    List<Data> data;

    public DataCsv(File file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        data = new ArrayList<>();

        int time = 0, accX = 0, accY = 0, accZ = 0, rotX = 0, rotY = 0, rotZ = 0;
        String[] header = reader.readLine().split(String.valueOf(DebugCsv.DELIMITER));

        for (int i = 0; i < header.length; i++) {
            switch (header[i]) {
                case "time": time = i; break;
                case "acceleration-x": accX = i; break;
                case "acceleration-y": accY = i; break;
                case "acceleration-z": accZ = i; break;
                case "angular-velocity-x": rotX = i; break;
                case "angular-velocity-y": rotY = i; break;
                case "angular-velocity-z": rotZ = i; break;
            }
        }

        data = new ArrayList<>();

        for (String line : reader.lines().toList()) {
            String[] values = line.split(String.valueOf(DebugCsv.DELIMITER));

            data.add(new Data(
                    Float.parseFloat(values[time]),
                    new Vec3f(
                            Float.parseFloat(values[accX]),
                            Float.parseFloat(values[accY]),
                            Float.parseFloat(values[accZ])
                    ),
                    new Vec3f(
                            Float.parseFloat(values[rotX]),
                            Float.parseFloat(values[rotY]),
                            Float.parseFloat(values[rotZ])
                    )
            ));
        }
    }

    @Override
    public Iterator<Data> iterator() {
        return this.data.iterator();
    }

    public static class Data {
        public Data(float time, Vec3f acceleration, Vec3f angularVelocity) {
            this.time = time;
            this.acceleration = acceleration;
            this.angularVelocity = angularVelocity;
        }

        public float time;
        public Vec3f acceleration;
        public Vec3f angularVelocity;
    }
}
