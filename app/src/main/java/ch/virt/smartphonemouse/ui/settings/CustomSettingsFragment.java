package ch.virt.smartphonemouse.ui.settings;

import androidx.preference.PreferenceFragmentCompat;

import ch.virt.smartphonemouse.helper.MainContext;

public abstract class CustomSettingsFragment extends PreferenceFragmentCompat {

    protected MainContext main;

    public void setMain(MainContext main) {
        this.main = main;
    }
}
