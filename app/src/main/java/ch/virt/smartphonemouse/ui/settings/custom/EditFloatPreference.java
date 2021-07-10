package ch.virt.smartphonemouse.ui.settings.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.InputType;
import android.util.AttributeSet;

import androidx.preference.EditTextPreference;

import ch.virt.smartphonemouse.R;

public class EditFloatPreference extends EditTextPreference {

    private boolean showValueAsDescription;
    private String valueUnit;
    private float minimumValue = 0;
    private float maximumValue = 1000;

    private float value;

    public EditFloatPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        final TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.EditFloatPreference, defStyleAttr, defStyleRes);
        showValueAsDescription = a.getBoolean(R.styleable.EditFloatPreference_showValueAsSummary, false);
        boolean signed = a.getBoolean(R.styleable.EditFloatPreference_signed, false);
        valueUnit = a.getString(R.styleable.EditFloatPreference_valueUnit);
        a.recycle();

        if (valueUnit == null) valueUnit = "";
        if (signed) minimumValue = -maximumValue;

        if (showValueAsDescription) setSummaryProvider(preference -> EditFloatPreference.this.getSummary());

        setOnBindEditTextListener(editText -> editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | (signed ? InputType.TYPE_NUMBER_FLAG_SIGNED : 0x00)));
    }

    public EditFloatPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public EditFloatPreference(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.editTextPreferenceStyle);
    }

    public EditFloatPreference(Context context) {
        this(context, null);
    }

    @Override
    public CharSequence getSummary() {
        return showValueAsDescription ? value + " " + valueUnit : super.getSummary();
    }

    public String getValueUnit() {
        return valueUnit;
    }

    public void setValueUnit(String valueUnit) {
        this.valueUnit = valueUnit;
    }

    public float getValue() {
        return value;
    }

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

    public float getMinimumValue() {
        return minimumValue;
    }

    public void setMinimumValue(float minimumValue) {
        this.minimumValue = minimumValue;
    }

    public float getMaximumValue() {
        return maximumValue;
    }

    public void setMaximumValue(float maximumValue) {
        this.maximumValue = maximumValue;
    }

    public void update(){
        setValue(getPersistedFloat(minimumValue));
    }
}
