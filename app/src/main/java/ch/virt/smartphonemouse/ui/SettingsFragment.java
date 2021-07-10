package ch.virt.smartphonemouse.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.google.android.material.snackbar.Snackbar;

import ch.virt.smartphonemouse.R;
import ch.virt.smartphonemouse.customization.DefaultSettings;
import ch.virt.smartphonemouse.helper.MainContext;

public class SettingsFragment extends PreferenceFragmentCompat {

    private final MainContext main;

    public SettingsFragment(MainContext main) {
        this.main = main;
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.settings, null);

        Preference reset = findPreference("reset");
        reset.setOnPreferenceClickListener(preference -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.settings_reset_dialog_message)
                    .setPositiveButton(R.string.settings_reset_dialog_reset, (dialog, id) -> {

                        DefaultSettings.set(main.getPreferences());

                        main.snack(main.getResources().getString(R.string.settings_reset_confirmation), Snackbar.LENGTH_SHORT);

                    })
                    .setNegativeButton(R.string.settings_reset_dialog_cancel, (dialog, id) -> {});

            Dialog dialog = builder.create();
            dialog.show();

            return true;

        });
    }
}
