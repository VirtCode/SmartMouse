package ch.virt.smartphonemouse.ui.settings.custom;

import android.content.Context;
import android.util.AttributeSet;

import androidx.preference.Preference;
import androidx.preference.SeekBarPreference;

import ch.virt.smartphonemouse.R;

public class SeekIntegerPreference extends SeekBarPreference {

    private int maximum;
    private int minimum;
    private int steps;

    public SeekIntegerPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        setSteps(100);
        setMaximum(100);
        setMinimum(0);

    }

    public SeekIntegerPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public SeekIntegerPreference(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.seekBarPreferenceStyle);
    }

    public SeekIntegerPreference(Context context) {
        this(context, null);
    }

    public int getMaximum() {
        return maximum;
    }

    public void setMaximum(int maximum) {
        this.maximum = maximum;
    }

    public int getMinimum() {
        return minimum;
    }

    public void setMinimum(int minimum) {
        this.minimum = minimum;
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;

        setMin(0);
        setMax(steps);
    }

    public int getRealValue(){
        return (int) (getValue() / ((float) steps) * (maximum - minimum) + minimum);
    }

    @Override
    protected boolean persistInt(int value) {
        // Have to use persistence methods, because otherwise, the variables are not accessible enough
        return super.persistInt((int) (value / ((float) steps) * (maximum - minimum) + minimum));
    }

    @Override
    protected int getPersistedInt(int defaultReturnValue) {
        // Have to use persistence methods, because otherwise, the variables are not accessible enough
        return (int) ((super.getPersistedInt(minimum) - minimum) / ((float) (maximum - minimum)) * steps);
    }
}
