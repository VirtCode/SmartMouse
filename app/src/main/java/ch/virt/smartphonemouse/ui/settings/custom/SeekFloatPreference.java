package ch.virt.smartphonemouse.ui.settings.custom;

import android.content.Context;
import android.util.AttributeSet;

import androidx.preference.Preference;
import androidx.preference.SeekBarPreference;

import ch.virt.smartphonemouse.R;

public class SeekFloatPreference extends SeekBarPreference {

    private float maximum;
    private float minimum;
    private int steps;

    public SeekFloatPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        setSteps(20000);
        setMaximum(100);
        setMinimum(-100);

    }

    public SeekFloatPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public SeekFloatPreference(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.seekBarPreferenceStyle);
    }

    public SeekFloatPreference(Context context) {
        this(context, null);
    }

    public float getMaximum() {
        return maximum;
    }

    public void setMaximum(float maximum) {
        this.maximum = maximum;
    }

    public float getMinimum() {
        return minimum;
    }

    public void setMinimum(float minimum) {
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

    public float getRealValue(){
        return (getValue() / ((float) steps) * (maximum - minimum) + minimum);
    }

    @Override
    protected boolean persistInt(int value) {
        // Have to use persistence methods, because otherwise, the variables are not accessible enough
        return super.persistFloat(value / ((float) steps) * (maximum - minimum) + minimum);
    }

    @Override
    protected int getPersistedInt(int defaultReturnValue) {
        // Have to use persistence methods, because otherwise, the variables are not accessible enough
        return (int) ((super.getPersistedFloat(minimum) - minimum) / (maximum - minimum) * steps);
    }

    public void update(){
        onSetInitialValue(null);
    }

}
