package ch.virt.smartphonemouse.ui.settings.custom;

import android.content.Context;
import android.util.AttributeSet;

import androidx.preference.SeekBarPreference;

import ch.virt.smartphonemouse.R;

/**
 * This preference is a seek bar preference that does seek for integers but is
 */
public class SeekIntegerPreference extends SeekBarPreference {

    private int maximum;
    private int minimum;
    private int steps;

    /**
     * Creates a preference.
     *
     * @param context      context for the preference to be in
     * @param attrs        attributes
     * @param defStyleAttr style attributes
     * @param defStyleRes  style resources
     */
    public SeekIntegerPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        setSteps(20000);
        setMaximum(100);
        setMinimum(-100);

    }

    /**
     * Creates a preference.
     *
     * @param context      context for the preference to be in
     * @param attrs        attributes
     * @param defStyleAttr style attributes
     */
    public SeekIntegerPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    /**
     * Creates a preference.
     *
     * @param context context for the preference to be in
     * @param attrs   attributes
     */
    public SeekIntegerPreference(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.seekBarPreferenceStyle);
    }


    /**
     * Creates a preference.
     *
     * @param context context for the preference to be in
     */
    public SeekIntegerPreference(Context context) {
        this(context, null);
    }

    /**
     * Returns the maximum value of the preference.
     *
     * @return maximum value
     */
    public int getMaximum() {
        return maximum;
    }

    /**
     * Sets the maximum value of the preference.
     *
     * @param maximum maximum value.
     */
    public void setMaximum(int maximum) {
        this.maximum = maximum;
    }

    /**
     * Returns the minimum value of the preference.
     *
     * @return minimum value
     */
    public int getMinimum() {
        return minimum;
    }

    /**
     * Sets the minimum value of the reference
     *
     * @param minimum minimum value
     */
    public void setMinimum(int minimum) {
        this.minimum = minimum;
    }

    /**
     * Returns the steps the seek bar can be in.
     *
     * @return amount of steps
     */
    public int getSteps() {
        return steps;
    }

    /**
     * Sets the steps the seek bar can be positioned in.
     *
     * @param steps amount of steps
     */
    public void setSteps(int steps) {
        this.steps = steps;

        setMin(0);
        setMax(steps);
    }

    /**
     * Returns the real value of the preference. Use this instead of realValue.
     *
     * @return value of the preference
     */
    public int getRealValue() {
        return (int) (getValue() / ((float) steps) * (maximum - minimum) + minimum);
    }

    /**
     * Updates the preference to the stored value.
     */
    public void update() {
        onSetInitialValue(null);
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
