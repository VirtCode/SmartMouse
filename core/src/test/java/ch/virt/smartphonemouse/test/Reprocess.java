package ch.virt.smartphonemouse.test;

import ch.virt.smartphonemouse.mouse.processing.Processing;
import ch.virt.smartphonemouse.mouse.processing.implement.ProcessingParameters;
import ch.virt.smartphonemouse.test.example.GravityRotation;
import ch.virt.smartphonemouse.test.framework.DataCsv;
import ch.virt.smartphonemouse.test.framework.DebugCsv;
import ch.virt.smartphonemouse.test.framework.LocalParameters;

import java.io.File;
import java.io.IOException;

/**
 * # How to use
 * This is an example implementation which reprocesses a captured signal from the app locally on your computer, allowing
 * quick iteration of the algorithm and reproducibility of results.
 *
 * To use this, you should first obtain a capture. Download SensorServer from my GitHub and run it on your computer. Go
 * into the App on your smartphone, to debugging in the settings, enable debugging transmission. Make sure your Computer
 * and the phone are in the same network and enter the IP of your Computer in the app. RESTART THE APP NOW.
 *
 * You can now use the app as normal, and each time you switch into the mouse view, a new capture is made and directly
 * sent to your computer (look at the console of SensorServer). Make now sure that you edit the path in this file to
 * point to the directory where you ran SensorServer. Change the target transmission id to your preferred capture.
 *
 * You'll also want to export your phone's calibrated parameters. To do so, go to the debugging setting and select the
 * export option. Paste the copied parameters JSON where this program reads it from. Note that this option is currently
 * only available when building from the dev branch.
 *
 * You can now run this file and look at its result (under -test csv of the same name as the original). It is
 * recommended to use some data analysis tool to graph the data.
 */
public class Reprocess {

    public static void main(String[] args) throws IOException {
        String target = "RZXO"; // supply your transmission id here
        ProcessingParameters params = LocalParameters.read(new File("test/parameters.json")); // place your exported params there

        DataCsv input = new DataCsv(new File("test/data/data-" + target + ".csv"));

        DebugCsv output = new DebugCsv();

        // create your processing and run the data through it

        // Use the processing from the app:
        // Processing processing = new Processing(output, params);
        // Processing.registerDebugColumns(output);

        // Use the minimal gravity rotation example on the current signal:
        GravityRotation processing = new GravityRotation(output, params.lengthGravity);

        output.start(new File("test/data/data-" + target + "-test.csv"));

        float last = 0;
        for (DataCsv.Data data : input) {
            float delta = data.time - last;
            last = data.time;

            // Use the processing from the app:
            // processing.next(data.time, delta, data.acceleration, data.angularVelocity);

            // Use the minimal gravity rotation example on the current signal:
            processing.next(data.time, delta, data.acceleration.toJOML(), data.angularVelocity.toJOML(), data.active);
        }

        output.end();
    }
}
