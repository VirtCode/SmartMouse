package ch.virt.smartphonemouse.ui.settings;

import android.os.Bundle;

import androidx.preference.Preference;

import ch.virt.smartphonemouse.R;
import ch.virt.smartphonemouse.helper.Listener;
import ch.virt.smartphonemouse.ui.settings.custom.EditIntegerPreference;
import ch.virt.smartphonemouse.ui.settings.custom.SeekFloatPreference;
import ch.virt.smartphonemouse.ui.settings.dialog.CalibrateDialog;

public class SettingsMovementSubfragment extends CustomSettingsFragment {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.settings_movement, null);

        SeekFloatPreference movementSensitivity = findPreference("movementSensitivity");
        movementSensitivity.setMaximum(16f);
        movementSensitivity.setMinimum(-2.0f);
        movementSensitivity.setSteps(180);
        movementSensitivity.update();

        EditIntegerPreference movementSamplingRealRate = findPreference("movementSamplingRealRate");

        Preference movementSamplingCalibrate = findPreference("movementSamplingCalibrate");
        movementSamplingCalibrate.setOnPreferenceClickListener((Preference.OnPreferenceClickListener) preference -> {

            CalibrateDialog dialog = new CalibrateDialog(main, movementSamplingRealRate::update);
            dialog.show(SettingsMovementSubfragment.this.getParentFragmentManager(), null);

            return true;
        });


    }
}
