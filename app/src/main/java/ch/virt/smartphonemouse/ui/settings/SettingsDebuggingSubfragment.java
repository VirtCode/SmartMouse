package ch.virt.smartphonemouse.ui.settings;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import ch.virt.smartphonemouse.R;
import ch.virt.smartphonemouse.ui.settings.custom.EditFloatPreference;
import ch.virt.smartphonemouse.ui.settings.custom.SeekFloatPreference;
import ch.virt.smartphonemouse.ui.settings.dialog.CalibrateDialog;

/**
 * This fragment is the settings page, where the user can configure everything regarding the movement.
 */
public class SettingsDebuggingSubfragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.settings_debugging, null);

        findPreference("debugDownload").setOnPreferenceClickListener(preference -> {

            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getContext().getText(R.string.settings_debug_server_download_url).toString()));
            startActivity(browserIntent);

            return false;
        });
    }
}
