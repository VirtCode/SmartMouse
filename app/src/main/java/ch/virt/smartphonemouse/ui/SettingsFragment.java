package ch.virt.smartphonemouse.ui;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import ch.virt.smartphonemouse.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.settings, null);
    }
}
