package ch.virt.smartphonemouse.ui.settings.dialog;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import ch.virt.smartphonemouse.R;
import ch.virt.smartphonemouse.customization.CalibrationHandler;
import ch.virt.smartphonemouse.mouse.Calibration;

/**
 * This fragment for a dialog is used to handle the calibration of the sampling rate.
 */
public class CalibrationHappeningSubdialog extends Fragment {

    private TextView time;

    private CalibrationHandler calibrator;
    private final DoneListener doneListener;

    /**
     * Creates the sub dialog fragment.
     *
     * @param doneListener listener that gets called when the calibration process is finished.
     */
    public CalibrationHappeningSubdialog(DoneListener doneListener) {
        super(R.layout.subdialog_calibrate_happening);

        this.doneListener = doneListener;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        calibrator = new CalibrationHandler(getContext());

        time = view.findViewById(R.id.calibrate_samplingrate_time);
        time.setText(getResources().getString(R.string.dialog_calibrate_happening_init));

        calibrator.calibrate(state -> time.post(() -> {
            if (state == Calibration.STATE_SAMPLING) time.setText(getResources().getString(R.string.dialog_calibrate_happening_sampling));
            else if (state == Calibration.STATE_NOISE) time.setText(getResources().getString(R.string.dialog_calibrate_happening_noise));
            else if (state == Calibration.STATE_END) doneListener.done();
        }));
    }

    public interface DoneListener {
        void done();
    }
}
