package ch.virt.smartphonemouse.ui.settings.custom;

import android.content.Context;
import android.util.AttributeSet;
import androidx.preference.SeekBarPreference;
import ch.virt.smartphonemouse.R;

/**
 * This is a Seek Bar preference that stores a float.
 */
public class SeekFloatPreference extends SeekBarPreference {

    private float maximum;
    private float minimum;
    private int steps;

    /**
     * Creates a preference.
     *
     * @param context      context for the preference to be in
     * @param attrs        attributes
     * @param defStyleAttr style attributes
     * @param defStyleRes  style resources
     */
    public SeekFloatPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        // Set a ludicrous min, max and amount of steps, because all data will be fitted to it for some reason
        // FIXME: Investigate this and fix it
        setSteps(10000000);
        setMaximum(50000);
        setMinimum(-50000);

    }

    /**
     * Creates a preference.
     *
     * @param context      context for the preference to be in
     * @param attrs        attributes
     * @param defStyleAttr style attributes
     */
    public SeekFloatPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    /**
     * Creates a preference.
     *
     * @param context context for the preference to be in
     * @param attrs   attributes
     */
    public SeekFloatPreference(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.seekBarPreferenceStyle);
    }



    /**
     * Creates a preference.
     *
     * @param context context for the preference to be in
     */
    public SeekFloatPreference(Context context) {
        this(context, null);
    }


    /**
     * Returns the maximum value of the preference.
     *
     * @return maximum value
     */
    public float getMaximum() {
        return maximum;
    }

    /**
     * Sets the maximum value of the preference.
     *
     * @param maximum maximum value
     */
    public void setMaximum(float maximum) {
        this.maximum = maximum;
    }

    /**
     * Returns the minimum value of the preference.
     *
     * @return minimum value
     */
    public float getMinimum() {
        return minimum;
    }

    /**
     * Sets the minimum value of the preference.
     *
     * @param minimum minimum value
     */
    public void setMinimum(float minimum) {
        this.minimum = minimum;
    }

    /**
     * Returns how many steps are present on the progress bar.
     *
     * @return steps on the bar
     */
    public int getSteps() {
        return steps;
    }

    /**
     * Sets the steps present on the progress bar.
     *
     * @param steps steps on the bar
     */
    public void setSteps(int steps) {
        this.steps = steps;

        setMin(0);
        setMax(steps);
    }

    /**
     * Returns the real value that the preference holds. Use this instead of getValue.
     *
     * @return value
     */
    public float getRealValue() {
        return (getValue() / ((float) steps) * (maximum - minimum) + minimum);
    }

    /**
     * Updates the preference to the values stored in the storage.
     */
    public void update() {
        onSetInitialValue(null);
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
}
