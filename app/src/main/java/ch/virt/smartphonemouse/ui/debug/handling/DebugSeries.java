package ch.virt.smartphonemouse.ui.debug.handling;

import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.List;

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


    public DebugSeries(int color, String name, int averageAmount) {
        this.color = color;
        this.name = name;
        this.averageAmount = averageAmount;
        this.visible = true;

        samples = new ArrayList<>();
        dataSet = new LineGraphSeries<>();

        dataSet.setColor(color | 0xff000000);
    }

    void newData(float sample){
        samples.add(sample);

        currentAverage += sample;
        currentAverageIndex++;
        if (currentAverageIndex == averageAmount) {
            dataSet.appendData(new DataPoint(entryIndex++, currentAverage / averageAmount), true, 1000);
            currentAverageIndex = 0;
            currentAverage = 0;
        }
    }

    void clear(){
        samples.clear();
        dataSet.resetData(new DataPoint[0]);

        entryIndex = 0;
        currentAverageIndex = 0;
        currentAverage = 0;
    }

    void setAverageAmount(int amount){
        averageAmount = amount;
        clear();
    }

    public LineGraphSeries<DataPoint> getDataSet() {
        return dataSet;
    }

    public List<Float> getSamples() {
        return samples;
    }

    public int getColor() {
        return color;
    }

    public String getName() {
        return name;
    }

    boolean isVisible() {
        return visible;
    }

    void setVisible(boolean visible) {
        this.visible = visible;
    }
}
