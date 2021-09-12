package ch.virt.smartphonemouse.ui.settings;

import android.os.Bundle;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import ch.virt.smartphonemouse.R;
import ch.virt.smartphonemouse.ui.settings.custom.EditIntegerPreference;
import ch.virt.smartphonemouse.ui.settings.custom.SeekFloatPreference;
import ch.virt.smartphonemouse.ui.settings.dialog.CalibrateDialog;

/**
 * This fragment is the settings page, where the user can configure everything regarding the movement.
 */
public class SettingsMovementSubfragment extends PreferenceFragmentCompat {

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
        movementSamplingCalibrate.setOnPreferenceClickListener(preference -> {

            CalibrateDialog dialog = new CalibrateDialog();
            dialog.setFinishedListener((v) -> movementSamplingRealRate.update());
            dialog.show(SettingsMovementSubfragment.this.getParentFragmentManager(), null);

            return true;
        });
    }
}
