package ch.virt.smartphonemouse.test.framework;

import ch.virt.smartphonemouse.mouse.processing.implement.ProcessingParameters;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class LocalParameters {

    /**
     * Reads parameters from a local file
     * Parameters in the right format can be exported from the app in the debugging settings
     * @param file file to read from
     * @return read parameters
     * @throws IOException when file does not exist
     */
    public static ProcessingParameters read(File file) throws IOException {
        return new Gson().fromJson(new FileReader(file), ProcessingParameters.class);
    }
}
