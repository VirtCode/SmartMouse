package ch.virt.smartphonemouse.ui.settings;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.google.android.material.snackbar.Snackbar;

import ch.virt.smartphonemouse.R;
import ch.virt.smartphonemouse.customization.DefaultSettings;
import ch.virt.smartphonemouse.transmission.DeviceStorage;
import ch.virt.smartphonemouse.ui.settings.custom.EditIntegerPreference;

public class SettingsCommunicationSubfragment extends CustomSettingsFragment {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.settings_communication, null);

        Preference communicationRemoveDevices = findPreference("communicationRemoveDevices");

        communicationRemoveDevices.setOnPreferenceClickListener(preference -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.settings_communication_removeall_dialog_message)
                    .setPositiveButton(R.string.settings_communication_removeall_dialog_remove, (dialog, id) -> {

                        SharedPreferences.Editor editor = main.getPreferences().edit();

                        editor.putString(DeviceStorage.DEVICES_KEY, "[]"); // Reset to an empty json array

                        editor.apply();

                        main.snack(main.getResources().getString(R.string.settings_communication_removeall_confirmation), Snackbar.LENGTH_SHORT);

                    })
                    .setNegativeButton(R.string.settings_communication_removeall_dialog_cancel, (dialog, id) -> {});

            Dialog dialog = builder.create();
            dialog.show();

            return true;

        });
    }
}
