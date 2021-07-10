package ch.virt.smartphonemouse.ui.settings;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import ch.virt.smartphonemouse.R;
import ch.virt.smartphonemouse.ui.settings.custom.SeekFloatPreference;

public class SettingsMovementSubfragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.settings_movement, null);

        SeekFloatPreference movementSensitivity = findPreference("movementSensitivity");
        movementSensitivity.setMaximum(16f);
        movementSensitivity.setMinimum(-2.0f);
        movementSensitivity.setSteps(180);
        movementSensitivity.update();
    }
}
