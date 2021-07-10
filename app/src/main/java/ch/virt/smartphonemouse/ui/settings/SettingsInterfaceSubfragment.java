package ch.virt.smartphonemouse.ui.settings;

import android.os.Bundle;

import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceFragmentCompat;

import ch.virt.smartphonemouse.R;
import ch.virt.smartphonemouse.ui.settings.custom.SeekFloatPreference;
import ch.virt.smartphonemouse.ui.settings.custom.SeekIntegerPreference;

public class SettingsInterfaceSubfragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.settings_interface, null);

        SeekFloatPreference interfaceLayoutMiddleWidth = findPreference("interfaceLayoutMiddleWidth");
        interfaceLayoutMiddleWidth.setMaximum(0.5f);
        interfaceLayoutMiddleWidth.setMinimum(0.0f);
        interfaceLayoutMiddleWidth.setSteps(100);
        interfaceLayoutMiddleWidth.update();

        SeekFloatPreference interfaceLayoutHeight = findPreference("interfaceLayoutHeight");
        interfaceLayoutHeight.setMaximum(1.0f);
        interfaceLayoutHeight.setMinimum(0.0f);
        interfaceLayoutHeight.setSteps(100);
        interfaceLayoutHeight.update();

        SeekIntegerPreference interfaceVibrationsSpecialIntensity = findPreference("interfaceVibrationsSpecialIntensity");
        interfaceVibrationsSpecialIntensity.setMaximum(100);
        interfaceVibrationsSpecialIntensity.setMinimum(0);
        interfaceVibrationsSpecialIntensity.setSteps(100);
        interfaceVibrationsSpecialIntensity.update();

        SeekIntegerPreference interfaceVibrationsScrollIntensity = findPreference("interfaceVibrationsScrollIntensity");
        interfaceVibrationsScrollIntensity.setMaximum(100);
        interfaceVibrationsScrollIntensity.setMinimum(0);
        interfaceVibrationsScrollIntensity.setSteps(100);
        interfaceVibrationsScrollIntensity.update();

        SeekIntegerPreference interfaceVibrationsButtonIntensity = findPreference("interfaceVibrationsButtonIntensity");
        interfaceVibrationsButtonIntensity.setMaximum(100);
        interfaceVibrationsButtonIntensity.setMinimum(0);
        interfaceVibrationsButtonIntensity.setSteps(100);
        interfaceVibrationsButtonIntensity.update();

        SeekFloatPreference interfaceVisualsIntensity = findPreference("interfaceVisualsIntensity");
        interfaceVisualsIntensity.setMaximum(1.0f);
        interfaceVisualsIntensity.setMinimum(0.0f);
        interfaceVisualsIntensity.setSteps(100);
        interfaceVisualsIntensity.update();
    }
}
