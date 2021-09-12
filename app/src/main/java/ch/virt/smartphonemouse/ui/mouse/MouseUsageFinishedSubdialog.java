package ch.virt.smartphonemouse.ui.mouse;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import ch.virt.smartphonemouse.R;

/**
 * This sub dialog is the last page of the usage dialog. It gives the user the option to disable every further usage dialog.
 */
public class MouseUsageFinishedSubdialog extends Fragment {

    private CheckBox notAgain;

    /**
     * Creates the sub dialog.
     */
    public MouseUsageFinishedSubdialog() {
        super(R.layout.subdialog_mouse_usage_finished);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        notAgain = view.findViewById(R.id.mouse_usage_finished_notagain);


        notAgain.setChecked(!PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean("showUsage", true));

        notAgain.setOnCheckedChangeListener((buttonView, isChecked) -> PreferenceManager.getDefaultSharedPreferences(getContext()).edit().putBoolean("showUsage", !isChecked).apply());
    }
}
