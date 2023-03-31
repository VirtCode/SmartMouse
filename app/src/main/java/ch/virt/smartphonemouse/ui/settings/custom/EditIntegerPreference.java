package ch.virt.smartphonemouse.ui.settings.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.InputType;
import android.util.AttributeSet;

import androidx.preference.EditTextPreference;

import ch.virt.smartphonemouse.R;

/**
 * This is a preference that opens a dialog for the user to set a integer value.
 */
public class EditIntegerPreference extends EditTextPreference {

    private boolean showValueAsDescription;
    private String valueUnit;
    private int minimumValue = 0;
    private int maximumValue = 100000;

    private int value;

    /**
     * Creates a preference.
     *
     * @param context      context for the preference to be in
     * @param attrs        attributes
     * @param defStyleAttr style attributes
     * @param defStyleRes  style resources
     */
    public EditIntegerPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        final TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.EditIntegerPreference, defStyleAttr, defStyleRes);
        showValueAsDescription = a.getBoolean(R.styleable.EditIntegerPreference_showValueAsSummary, false);
        boolean signed = a.getBoolean(R.styleable.EditFloatPreference_signed, false);
        valueUnit = a.getString(R.styleable.EditIntegerPreference_valueUnit);
        a.recycle();

        if (valueUnit == null) valueUnit = "";
        if (signed) minimumValue = -maximumValue;

        if (showValueAsDescription)
            setSummaryProvider(preference -> EditIntegerPreference.this.getSummary());

        setOnBindEditTextListener(editText -> editText.setInputType(InputType.TYPE_CLASS_NUMBER | (signed ? InputType.TYPE_NUMBER_FLAG_SIGNED : 0x00)));
    }

    /**
     * Creates a preference.
     *
     * @param context      context for the preference to be in
     * @param attrs        attributes
     * @param defStyleAttr style attributes
     */
    public EditIntegerPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    /**
     * Creates a preference.
     *
     * @param context context for the preference to be in
     * @param attrs   attributes
     */
    public EditIntegerPreference(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.editTextPreferenceStyle);
    }

    /**
     * Creates a preference.
     *
     * @param context context for the preference to be in
     */
    public EditIntegerPreference(Context context) {
        this(context, null);
    }


    /**
     * Whether the value should be shown as description.
     *
     * @return is shown
     */
    public boolean isShowValueAsDescription() {
        return showValueAsDescription;
    }

    /**
     * Sets whether the value should be shown as the description of the preference.
     *
     * @param showValueAsDescription sets whether it should be shown as a description
     */
    public void setShowValueAsDescription(boolean showValueAsDescription) {
        this.showValueAsDescription = showValueAsDescription;
    }

    /**
     * Returns the unit of the value of the preference.
     *
     * @return value unit
     */
    public String getValueUnit() {
        return valueUnit;
    }

    /**
     * Sets the unit of the value of the preference.
     *
     * @param valueUnit unit of the value
     */
    public void setValueUnit(String valueUnit) {
        this.valueUnit = valueUnit;
    }

    /**
     * Returns the value of the preference.
     *
     * @return value of the preference
     */
    public int getValue() {
        return value;
    }

    /**
     * Sets the value of the preference.
     *
     * @param value value of the preference
     */
    public void setValue(int value) {
        final boolean wasBlocking = shouldDisableDependents();

        this.value = Math.min(Math.max(value, minimumValue), maximumValue);

        persistInt(value);

        final boolean isBlocking = shouldDisableDependents();
        if (isBlocking != wasBlocking) {
            notifyDependencyChange(isBlocking);
        }

        notifyChanged();
    }

    /**
     * Returns the minimum value of the preference.
     *
     * @return minimum value
     */
    public int getMinimumValue() {
        return minimumValue;
    }

    /**
     * Sets the minimum value that may be entered to the preference.
     *
     * @param minimumValue minimum value
     */
    public void setMinimumValue(int minimumValue) {
        this.minimumValue = minimumValue;
    }

    /**
     * Returns the maximum value of the preference.
     *
     * @return maximum value
     */
    public int getMaximumValue() {
        return maximumValue;
    }

    /**
     * Sets the maximum value that may be entered to the preference.
     *
     * @param maximumValue maximum value
     */
    public void setMaximumValue(int maximumValue) {
        this.maximumValue = maximumValue;
    }

    /**
     * Updates the preference to the current stored value.
     */
    public void update() {
        setValue(getPersistedInt(minimumValue));
    }


    @Override
    public CharSequence getSummary() {
        return showValueAsDescription ? value + " " + valueUnit : super.getSummary();
    }

    @Override
    public String getText() {
        return Integer.toString(getValue());
    }

    @Override
    public void setText(String text) {
        setValue(Integer.parseInt(text));
    }

    @Override
    public boolean shouldDisableDependents() {
        return !this.isEnabled() || value == 0;
    }

    @Override
    protected void onSetInitialValue(Object defaultValue) {
        setValue(getPersistedInt(defaultValue == null ? 0 : (Integer) defaultValue));
    }
}
