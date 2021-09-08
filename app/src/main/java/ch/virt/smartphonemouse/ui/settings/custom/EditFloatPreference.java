package ch.virt.smartphonemouse.ui.settings.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.InputType;
import android.util.AttributeSet;

import androidx.preference.EditTextPreference;

import ch.virt.smartphonemouse.R;

/**
 * This preference is a preference that opens a dialog and allows you to edit a float.
 */
public class EditFloatPreference extends EditTextPreference {

    private boolean showValueAsDescription;
    private String valueUnit;
    private float minimumValue = 0;
    private float maximumValue = 1000;

    private float value;

    /**
     * Creates a preference.
     *
     * @param context      context for the preference to be in
     * @param attrs        attributes
     * @param defStyleAttr style attributes
     * @param defStyleRes  style resources
     */
    public EditFloatPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        final TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.EditFloatPreference, defStyleAttr, defStyleRes);
        showValueAsDescription = a.getBoolean(R.styleable.EditFloatPreference_showValueAsSummary, false);
        boolean signed = a.getBoolean(R.styleable.EditFloatPreference_signed, false);
        valueUnit = a.getString(R.styleable.EditFloatPreference_valueUnit);
        a.recycle();

        if (valueUnit == null) valueUnit = "";
        if (signed) minimumValue = -maximumValue;

        if (showValueAsDescription)
            setSummaryProvider(preference -> EditFloatPreference.this.getSummary());

        setOnBindEditTextListener(editText -> editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | (signed ? InputType.TYPE_NUMBER_FLAG_SIGNED : 0x00)));
    }

    /**
     * Creates a preference.
     *
     * @param context      context for the preference to be in
     * @param attrs        attributes
     * @param defStyleAttr style attributes
     */
    public EditFloatPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    /**
     * Creates a preference.
     *
     * @param context context for the preference to be in
     * @param attrs   attributes
     */
    public EditFloatPreference(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.editTextPreferenceStyle);
    }

    /**
     * Creates a preference.
     *
     * @param context context for the preference to be in
     */
    public EditFloatPreference(Context context) {
        this(context, null);
    }


    /**
     * Returns the unit that is displayed to the user.
     *
     * @return unit of the value
     */
    public String getValueUnit() {
        return valueUnit;
    }

    /**
     * Sets the unit that is displayed to the user.
     *
     * @param valueUnit unit of the entered value
     */
    public void setValueUnit(String valueUnit) {
        this.valueUnit = valueUnit;
    }

    /**
     * Returns the value of the preference.
     *
     * @return value
     */
    public float getValue() {
        return value;
    }

    /**
     * Sets the value of the preference.
     *
     * @param value value
     */
    public void setValue(float value) {
        final boolean wasBlocking = shouldDisableDependents();

        this.value = Math.min(Math.max(value, minimumValue), maximumValue);

        persistFloat(value);

        final boolean isBlocking = shouldDisableDependents();
        if (isBlocking != wasBlocking) {
            notifyDependencyChange(isBlocking);
        }

        notifyChanged();
    }

    /**
     * Returns the minimum value that may be entered.
     *
     * @return minimum value
     */
    public float getMinimumValue() {
        return minimumValue;
    }

    /**
     * Sets the minimum value that may be entered.
     *
     * @param minimumValue minimum value
     */
    public void setMinimumValue(float minimumValue) {
        this.minimumValue = minimumValue;
    }

    /**
     * Returns the maximum value may be entered.
     *
     * @return maximum value
     */
    public float getMaximumValue() {
        return maximumValue;
    }

    /**
     * Sets the maximum value that may be entered.
     *
     * @param maximumValue of the preference to be set
     */
    public void setMaximumValue(float maximumValue) {
        this.maximumValue = maximumValue;
    }

    /**
     * Updates the contained value from the preference storage.
     */
    public void update() {
        setValue(getPersistedFloat(minimumValue));
    }


    @Override
    public CharSequence getSummary() {
        return showValueAsDescription ? value + " " + valueUnit : super.getSummary();
    }

    @Override
    public String getText() {
        return Float.toString(getValue());
    }

    @Override
    public void setText(String text) {
        setValue(Float.parseFloat(text));
    }

    @Override
    public boolean shouldDisableDependents() {
        return !this.isEnabled() || value == 0;
    }

    @Override
    protected void onSetInitialValue(Object defaultValue) {
        setValue(getPersistedFloat(defaultValue == null ? 0 : (Float) defaultValue));
    }
}
