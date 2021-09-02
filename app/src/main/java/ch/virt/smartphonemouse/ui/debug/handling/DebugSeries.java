package ch.virt.smartphonemouse.ui.debug.handling;

import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.List;

/**
 * This class holds a single series in the debugging chart.
 */
public class DebugSeries {

    private final int color;
    private final String name;

    private int averageAmount;
    private boolean visible;

    private int currentAverageIndex;
    private float currentAverage;

    private List<Float> samples;
    private LineGraphSeries<DataPoint> dataSet;
    private int entryIndex;

    /**
     * Creates a series.
     *
     * @param color         color of that series
     * @param name          name of that series
     * @param averageAmount amount of samples required for one point on the diagram
     */
    public DebugSeries(int color, String name, int averageAmount) {
        this.color = color;
        this.name = name;
        this.averageAmount = averageAmount;
        this.visible = true;

        samples = new ArrayList<>();
        dataSet = new LineGraphSeries<>();

        dataSet.setColor(color | 0xff000000); // Add alpha values
    }

    /**
     * Adds a new sample to the series.
     *
     * @param sample new sample
     */
    void newData(float sample) {
        samples.add(sample);

        currentAverage += sample;
        currentAverageIndex++;
        if (currentAverageIndex == averageAmount) {
            dataSet.appendData(new DataPoint(entryIndex++, currentAverage / averageAmount), true, 10000);
            currentAverageIndex = 0;
            currentAverage = 0;
        }
    }

    /**
     * Clears the series.
     */
    void clear() {
        samples.clear();
        dataSet.resetData(new DataPoint[0]);

        entryIndex = 0;
        currentAverageIndex = 0;
        currentAverage = 0;
    }

    /**
     * Sets how many samples are required for one datapoint on the chart.
     *
     * @param amount amount of samples
     */
    void setAverageAmount(int amount) {
        averageAmount = amount;
        clear();
    }

    /**
     * Returns the data set, one can add to a chart.
     *
     * @return dataset to be added
     */
    public LineGraphSeries<DataPoint> getDataSet() {
        return dataSet;
    }

    /**
     * Returns all samples shown for this series.
     *
     * @return list of samples
     */
    public List<Float> getSamples() {
        return samples;
    }

    /**
     * Returns the color of this series.
     *
     * @return color as an integer
     */
    public int getColor() {
        return color;
    }

    /**
     * Returns the name of this series.
     *
     * @return name of the series
     */
    public String getName() {
        return name;
    }

    /**
     * Returns whether the series is currently visible on the chart.
     *
     * @return is visible
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * Sets whether the series should be visible.
     *
     * @param visible should be visible
     */
    void setVisible(boolean visible) {
        this.visible = visible;
    }
}
