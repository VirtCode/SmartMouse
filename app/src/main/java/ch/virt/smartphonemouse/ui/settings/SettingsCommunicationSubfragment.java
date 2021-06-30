package ch.virt.smartphonemouse.ui.settings;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import ch.virt.smartphonemouse.R;

public class SettingsCommunicationSubfragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.settings_communication, null);

    }
}
