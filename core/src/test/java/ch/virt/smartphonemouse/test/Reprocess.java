package ch.virt.smartphonemouse.test;

import ch.virt.smartphonemouse.mouse.processing.Processing;
import ch.virt.smartphonemouse.mouse.processing.implement.ProcessingParameters;
import ch.virt.smartphonemouse.test.framework.DataCsv;
import ch.virt.smartphonemouse.test.framework.DebugCsv;
import ch.virt.smartphonemouse.test.framework.LocalParameters;

import java.io.File;
import java.io.IOException;

/**
 * Example of how to reprocess captured data locally
 */
public class Reprocess {

    public static void main(String[] args) throws IOException {
        String target = "gu4O"; // supply your transmission id here
        ProcessingParameters params = LocalParameters.read(new File("test/parameters.json")); // place your exported params there

        DataCsv input = new DataCsv(new File("test/data/data-" + target + ".csv"));

        DebugCsv output = new DebugCsv();
        Processing.registerDebugColumns(output);

        // create your processing and run the data through it
        Processing processing = new Processing(output, params);

        output.start(new File("test/data/data-" + target + "-test.csv"));

        float last = 0;
        for (DataCsv.Data data : input) {
            float delta = data.time - last;
            last = data.time;

            processing.next(data.time, delta, data.acceleration, data.angularVelocity);
        }

        output.end();
    }
}
