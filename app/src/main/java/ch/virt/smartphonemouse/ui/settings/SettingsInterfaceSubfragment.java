package ch.virt.smartphonemouse.ui.settings;

import android.os.Bundle;

import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceFragmentCompat;

import ch.virt.smartphonemouse.R;

public class SettingsInterfaceSubfragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.settings_interface, null);
    }
}
