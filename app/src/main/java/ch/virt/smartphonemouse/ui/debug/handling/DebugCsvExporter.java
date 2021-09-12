package ch.virt.smartphonemouse.ui.debug.handling;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * This class is used to export the content of the chart as a csv file.
 */
public class DebugCsvExporter {

    private final DebugChartHandler chart;
    private final Context context;

    /**
     * Creates the exporter.
     *
     * @param chart   chart to export
     * @param context context to share with
     */
    public DebugCsvExporter(DebugChartHandler chart, Context context) {
        this.chart = chart;
        this.context = context;
    }

    /**
     * Exports the chart as a csv file.
     *
     * @throws IOException thrown if failed to write file to disk for cache
     */
    public void exportCsv() throws IOException {
        String csv = createCsv();
        File file = cacheCsv(csv);
        shareCsv(file);
    }

    /**
     * Creates a string with the diagram data in csv format.
     *
     * @return string with csv data
     */
    public String createCsv() {
        StringBuilder s = new StringBuilder();

        // Write Header
        s.append("Timestamp").append(';');
        for (DebugSeries series : chart.getSeries()) {
            s.append(series.getName()).append(';');
        }
        s.append('\n');

        // Write Body
        int length = chart.getTimeStamps().size();
        for (int i = 0; i < length; i++) {
            s.append(chart.getTimeStamps().get(i)).append(';');
            for (DebugSeries series : chart.getSeries()) {
                s.append(series.getSamples().get(i)).append(';');
            }
            s.append('\n');
        }

        return s.toString();
    }

    /**
     * Writes the csv data to a cache file so it can be shared.
     *
     * @param s string with csv data
     * @return file that it was written to
     * @throws IOException thrown if failed to write file
     */
    public File cacheCsv(String s) throws IOException {
        new File(context.getExternalFilesDir(null), "exports").mkdir();
        File file = new File(context.getExternalFilesDir(null), "exports/export.csv");

        FileWriter writer = new FileWriter(file);
        writer.write(s);
        writer.flush();
        writer.close();

        return file;
    }

    /**
     * Shares the csv file through the android share dialog.
     *
     * @param f cache file to share
     */
    public void shareCsv(File f) {
        Uri u = FileProvider.getUriForFile(context, "ch.virt.fileprovider", f);

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/*");
        intent.putExtra(Intent.EXTRA_STREAM, u);
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(Intent.createChooser(intent, "Export file to "));
    }

}
