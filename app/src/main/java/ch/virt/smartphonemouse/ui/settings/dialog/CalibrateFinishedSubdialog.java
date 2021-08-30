package ch.virt.smartphonemouse.ui.settings.dialog;

import android.view.View;
import android.widget.TextView;

import androidx.preference.PreferenceManager;

import ch.virt.smartphonemouse.R;
import ch.virt.smartphonemouse.helper.MainContext;
import ch.virt.smartphonemouse.ui.CustomFragment;

public class CalibrateFinishedSubdialog extends CustomFragment {

    TextView rate;

    public CalibrateFinishedSubdialog(MainContext context) {
        super(R.layout.subdialog_calibrate_finished, context);
    }

    @Override
    public void render() {

    }

    @Override
    protected void initComponents() {
        rate.setText(getResources().getString(R.string.dialog_calibrate_finished_rate, PreferenceManager.getDefaultSharedPreferences(getContext()).getInt("movementSamplingRealRate", 0)));
    }

    @Override
    protected void loadComponents(View view) {
        rate = view.findViewById(R.id.calibrate_finished_rate);
    }
}
