package ch.virt.smartphonemouse.ui.settings;

import android.os.Bundle;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import ch.virt.smartphonemouse.R;
import ch.virt.smartphonemouse.ui.settings.custom.EditFloatPreference;
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
        movementSensitivity.setMaximum(20000f);
        movementSensitivity.setMinimum(10000f);
        movementSensitivity.setSteps(200);
        movementSensitivity.update();

        EditFloatPreference rotThreshold = findPreference("movementThresholdRotation");
        EditFloatPreference accThreshold = findPreference("movementThresholdAcceleration");
        EditFloatPreference samplingRate = findPreference("movementSampling");

        Preference movementSamplingCalibrate = findPreference("movementRecalibrate");
        movementSamplingCalibrate.setOnPreferenceClickListener(preference -> {

            CalibrateDialog dialog = new CalibrateDialog();
            dialog.setFinishedListener(dialog1 -> {
                // Update changed values after calibration
                rotThreshold.update();
                accThreshold.update();
                samplingRate.update();
            });

            dialog.show(SettingsMovementSubfragment.this.getParentFragmentManager(), null);

            return true;
        });

        checkAdvanced();
    }

    public void checkAdvanced() {
        boolean advanced = PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean("advanced", false);
        findPreference("movementEnableGravityRotation").setVisible(advanced);
        findPreference("movementCalibrationSampling").setVisible(advanced);
        findPreference("movementCalibrationNoise").setVisible(advanced);
        findPreference("movementNoiseRatioAcceleration").setVisible(advanced);
        findPreference("movementNoiseFactorAcceleration").setVisible(advanced);
        findPreference("movementNoiseRatioRotation").setVisible(advanced);
        findPreference("movementNoiseFactorRotation").setVisible(advanced);
        findPreference("movementThresholdAcceleration").setVisible(advanced);
        findPreference("movementThresholdRotation").setVisible(advanced);
        findPreference("movementSampling").setVisible(advanced);
        findPreference("movementDurationWindowGravity").setVisible(advanced);
        findPreference("movementDurationWindowNoise").setVisible(advanced);
        findPreference("movementDurationThreshold").setVisible(advanced);
        findPreference("movementDurationGravity").setVisible(advanced);
    }
}
