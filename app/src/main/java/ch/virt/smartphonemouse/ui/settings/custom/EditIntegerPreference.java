package ch.virt.smartphonemouse.ui.settings.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.InputType;
import android.util.AttributeSet;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.core.content.res.TypedArrayUtils;
import androidx.preference.DialogPreference;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;

import ch.virt.smartphonemouse.R;

public class EditIntegerPreference extends EditTextPreference {

    private boolean showValueAsDescription;
    private String valueUnit;
    private int minimumValue = 0;
    private int maximumValue = 1000;

    private int value;

    public EditIntegerPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        final TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.EditIntegerPreference, defStyleAttr, defStyleRes);
        showValueAsDescription = a.getBoolean(R.styleable.EditIntegerPreference_showValueAsSummary, false);
        boolean signed = a.getBoolean(R.styleable.EditFloatPreference_signed, false);
        valueUnit = a.getString(R.styleable.EditIntegerPreference_valueUnit);
        a.recycle();

        if (valueUnit == null) valueUnit = "";
        if (signed) minimumValue = -maximumValue;

        if (showValueAsDescription) setSummaryProvider(preference -> EditIntegerPreference.this.getSummary());

        setOnBindEditTextListener(editText -> editText.setInputType(InputType.TYPE_CLASS_NUMBER | (signed ? InputType.TYPE_NUMBER_FLAG_SIGNED : 0x00)));
    }

    public EditIntegerPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public EditIntegerPreference(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.editTextPreferenceStyle);
    }

    public EditIntegerPreference(Context context) {
        this(context, null);
    }

    public boolean isShowValueAsDescription() {
        return showValueAsDescription;
    }

    public void setShowValueAsDescription(boolean showValueAsDescription) {
        this.showValueAsDescription = showValueAsDescription;
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

    public int getValue() {
        return value;
    }

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

    public int getMinimumValue() {
        return minimumValue;
    }

    public void setMinimumValue(int minimumValue) {
        this.minimumValue = minimumValue;
    }

    public int getMaximumValue() {
        return maximumValue;
    }

    public void setMaximumValue(int maximumValue) {
        this.maximumValue = maximumValue;
    }

    public void update(){
        setValue(getPersistedInt(minimumValue));
    }
}
