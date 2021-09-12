package ch.virt.smartphonemouse.ui.settings.dialog;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import ch.virt.smartphonemouse.R;

/**
 * This class is a sub fragment for the calibrate dialog, that is shown when the calibration process has finished.
 */
public class CalibrateFinishedSubdialog extends Fragment {

    private TextView rate;

    /**
     * Creates the sub dialog.
     */
    public CalibrateFinishedSubdialog() {
        super(R.layout.subdialog_calibrate_finished);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rate = view.findViewById(R.id.calibrate_finished_rate);
        rate.setText(getResources().getString(R.string.dialog_calibrate_finished_rate, PreferenceManager.getDefaultSharedPreferences(getContext()).getInt("movementSamplingRealRate", 0)));
    }
}
