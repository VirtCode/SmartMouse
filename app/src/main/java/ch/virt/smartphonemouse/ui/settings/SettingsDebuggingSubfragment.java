package ch.virt.smartphonemouse.ui.settings;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import ch.virt.smartphonemouse.R;
import ch.virt.smartphonemouse.mouse.Parameters;
import ch.virt.smartphonemouse.ui.settings.custom.EditFloatPreference;
import ch.virt.smartphonemouse.ui.settings.custom.SeekFloatPreference;
import ch.virt.smartphonemouse.ui.settings.dialog.CalibrateDialog;
import com.google.gson.Gson;

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

        findPreference("exportParameters").setOnPreferenceClickListener(preference -> {
            Parameters params = new Parameters(PreferenceManager.getDefaultSharedPreferences(getContext()));
            String s = new Gson().toJson(params.get());

            ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setPrimaryClip(ClipData.newPlainText("SmartMouse Parameter Export", s));

            return false;
        });
    }
}
