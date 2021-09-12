package ch.virt.smartphonemouse.ui.settings.dialog;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import ch.virt.smartphonemouse.R;
import ch.virt.smartphonemouse.customization.SamplingRateCalibrator;

/**
 * This fragment for a dialog is used to handle the calibration of the sampling rate.
 */
public class SamplingRateSubdialog extends Fragment {

    private TextView time;

    private SamplingRateCalibrator calibrator;
    private final SamplingRateCalibrator.DoneListener doneListener;

    /**
     * Creates the sub dialog fragment.
     *
     * @param doneListener listener that gets called when the calibration process is finished.
     */
    public SamplingRateSubdialog(SamplingRateCalibrator.DoneListener doneListener) {
        super(R.layout.subdialog_calibrate_samplingrate);

        this.doneListener = doneListener;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        calibrator = new SamplingRateCalibrator(getContext());

        time = view.findViewById(R.id.calibrate_samplingrate_time);
        time.setText(getResources().getString(R.string.dialog_calibrate_samplingrate_time, calibrator.getTestLength() / 1000));

        calibrator.calibrate(doneListener);
    }
}
