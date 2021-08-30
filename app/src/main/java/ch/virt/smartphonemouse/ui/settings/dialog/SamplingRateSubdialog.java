package ch.virt.smartphonemouse.ui.settings.dialog;

import android.view.View;
import android.widget.TextView;

import ch.virt.smartphonemouse.R;
import ch.virt.smartphonemouse.customization.SamplingRateCalibrator;
import ch.virt.smartphonemouse.helper.Listener;
import ch.virt.smartphonemouse.helper.MainContext;
import ch.virt.smartphonemouse.ui.CustomFragment;

public class SamplingRateSubdialog extends CustomFragment {

    private TextView time;

    private SamplingRateCalibrator calibrator;

    private final Listener doneListener;

    public SamplingRateSubdialog(MainContext context, Listener doneListener) {
        super(R.layout.subdialog_calibrate_samplingrate, context);
        this.doneListener = doneListener;

        calibrator = new SamplingRateCalibrator(getContext()); //TODO: Will probably produce nullpointer
    }

    @Override
    public void render() {

    }

    @Override
    protected void initComponents() {

        time.setText(getResources().getString(R.string.dialog_calibrate_samplingrate_time, calibrator.getTestLength() / 1000 * 2)); // Double the time, so it is always shorter

        calibrator.calibrate(doneListener);

    }

    @Override
    protected void loadComponents(View view) {
        time = view.findViewById(R.id.calibrate_samplingrate_time);
    }
}
