package ch.virt.smartphonemouse.ui.debug.handling;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;

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
    private LineDataSet dataSet;
    private int entryIndex;


    public DebugSeries(int color, String name, int averageAmount) {
        this.color = color;
        this.name = name;
        this.averageAmount = averageAmount;
        this.visible = true;

        samples = new ArrayList<>();
        dataSet = new LineDataSet(new ArrayList<Entry>(), name);

        dataSet.setDrawCircles(false);
        dataSet.setDrawValues(false);
        dataSet.setColor(color, 255);
    }

    void newData(float sample){
        samples.add(sample);

        currentAverage += sample;
        currentAverageIndex++;
        if (currentAverageIndex == averageAmount) {
            dataSet.addEntry(new Entry(entryIndex++, currentAverage / averageAmount));
            currentAverageIndex = 0;
        }
    }

    void clear(){
        samples.clear();
        dataSet.clear();

        entryIndex = 0;
        currentAverageIndex = 0;
        currentAverage = 0;
    }

    void setAverageAmount(int amount){
        averageAmount = amount;
        clear();
    }

    public LineDataSet getDataSet() {
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
