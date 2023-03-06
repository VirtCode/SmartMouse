package ch.virt.smartphonemouse.ui.debug.handling;

import android.content.SharedPreferences;
import android.view.View;
import ch.virt.smartphonemouse.ui.debug.OnDoubleClickListener;
import com.jjoe64.graphview.GraphView;

import java.util.ArrayList;
import java.util.List;

/**
 * This class handles the chart on the debug fragment.
 */
public class DebugChartHandler {

    private static final int[] COLORS = {0xf44336, 0x9c27b0, 0x673ab7, 0x3f51b5, 0x2196f3, 0x009688, 0x4caf50, 0x8bc34a, 0xcddc39, 0xffeb3b, 0xffc107, 0xff9800, 0xff5722, 0x795548, 0x9e9e9e, 0x607d8b, 0x03a9f4, 0x00bcd4, 0xe91e63};

    private final GraphView chart;
    private final List<DebugSeries> series;
    private final SharedPreferences preferences;

    private int highestIndex;
    private int colorIndex;

    private int averageAmount;
    private int currentAverageAmount;

    private List<Long> timeStamps;

    /**
     * Creates the chart handler.
     *
     * @param chart       chart to handle
     * @param preferences preferences to read certain config entries from
     */
    public DebugChartHandler(GraphView chart, SharedPreferences preferences) {
        timeStamps = new ArrayList<>();
        series = new ArrayList<>();
        this.preferences = preferences;

        this.chart = chart;

        averageAmount = preferences.getInt("debugChartAverageAmount", 20);

        styleChart();
    }

    /**
     * Styles the chart in order to look good.
     */
    private void styleChart() {
        chart.getViewport().setYAxisBoundsManual(true);
        chart.getViewport().setMinY(-10);
        chart.getViewport().setMaxY(10);

        chart.getViewport().setXAxisBoundsManual(true);
        chart.getViewport().setMinX(0);
        chart.getViewport().setMaxX(100);

        chart.getViewport().setScalable(true);
        chart.getViewport().setScalableY(true);

        chart.setOnClickListener(new OnDoubleClickListener() {
            @Override
            public void onDoubleClick(View v) {
                chart.getViewport().setYAxisBoundsManual(true);
                chart.getViewport().setMinY(-10);
                chart.getViewport().setMaxY(10);

                chart.getViewport().setXAxisBoundsManual(true);
                chart.getViewport().setMinX(0);
                chart.getViewport().setMaxX(100);
            }
        });
    }

    /**
     * Adds a series to the graph.
     *
     * @param name  name of that series
     * @param index source index of that data
     */
    public void addSeries(String name, int index) {
        if (index > highestIndex) highestIndex = index;

        DebugSeries series = new DebugSeries(selectColor(), name, averageAmount);
        chart.addSeries(series.getDataSet());
        this.series.add(index, series);
    }

    /**
     * Adds a new line of samples to the graph.
     *
     * @param timestamp timestamp of the samples
     * @param data      debugging data from the pipeline
     */
    public void newData(long timestamp, float[] data) {
        timeStamps.add(timestamp);

        for (int i = 0; i < data.length; i++) {
            if (i >= series.size()) break;
            DebugSeries series = this.series.get(i);
            if (series != null) series.newData(data[i]);
        }

        currentAverageAmount++;
        if (currentAverageAmount == averageAmount) {
            currentAverageAmount = 0;
        }
    }

    /**
     * Selects a colour for the next series. The colours are selected from the material colors.
     *
     * @return selected color as an integer
     * @see DebugChartHandler#COLORS
     */
    private int selectColor() {
        if (colorIndex == COLORS.length) colorIndex = 0;
        return COLORS[colorIndex++];
    }

    /**
     * Sets the amount of samples needed to form one point on the chart.
     *
     * @param amount amount of samples
     */
    public void setAverageAmount(int amount) {
        if (amount == averageAmount) return;
        this.averageAmount = amount;

        currentAverageAmount = 0;
        timeStamps.clear();

        for (DebugSeries series : this.series) {
            series.setAverageAmount(averageAmount);
        }

        preferences.edit().putInt("debugChartAverageAmount", amount).apply();
    }

    /**
     * Clears the chart.
     */
    public void clear() {
        timeStamps.clear();

        currentAverageAmount = 0;

        for (DebugSeries series : this.series) {
            series.clear();
        }
    }

    /**
     * Sets the visibility of a series.
     *
     * @param series     series to set visibility for
     * @param visibility visibility of that series
     */
    public void setSeriesVisibility(DebugSeries series, boolean visibility) {
        if (series.isVisible() != visibility) {
            if (visibility) chart.addSeries(series.getDataSet());
            else chart.removeSeries(series.getDataSet());

            series.setVisible(visibility);
        }
    }

    /**
     * Returns the list of timestamps present on the diagram. This method is used for exporting the shown data.
     *
     * @return list of timestamps
     */
    public List<Long> getTimeStamps() {
        return timeStamps;
    }

    /**
     * Returns all series of the diagram.
     *
     * @return list of series
     */
    public List<DebugSeries> getSeries() {
        return series;
    }

    /**
     * Returns how many samples are used for one point on the chart.
     *
     * @return amount
     */
    public int getAverageAmount() {
        return averageAmount;
    }
}
