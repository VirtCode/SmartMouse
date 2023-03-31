package ch.virt.smartphonemouse.ui.settings.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import ch.virt.smartphonemouse.R;

/**
 * This dialog is used in the settings menu when the user requests to calibrate the sampling rate.
 */
public class CalibrateDialog extends DialogFragment {

    AlertDialog dialog;

    Button positiveButton;

    boolean calibrating = false;

    private DialogInterface.OnDismissListener finishedListener;

    /**
     * Sets the listener that should be used to update the setting once the dialog is done.
     *
     * @param finishedListener dialog dismiss listener that is executed
     */
    public void setFinishedListener(DialogInterface.OnDismissListener finishedListener) {
        this.finishedListener = finishedListener;
    }

    /**
     * Is called when the dialog is created.
     */
    private void created() {
        dialog.setTitle(R.string.dialog_calibrate_title);

        dialog.setCanceledOnTouchOutside(false);

        setFragment(new CalibrateBeginSubdialog());
    }

    private void calibrate() {
        calibrating = true;

        positiveButton.setEnabled(false);
        positiveButton.setText(R.string.dialog_calibrate_done);

        setFragment(new CalibrationHappeningSubdialog(() -> positiveButton.post(this::finished)));
    }

    /**
     * Is called when the calibration process has finished.
     */
    private void finished() {
        dialog.setTitle(R.string.dialog_calibrate_finished_title);

        dialog.setCanceledOnTouchOutside(true);
        positiveButton.setEnabled(true);

        setFragment(new CalibrateFinishedSubdialog());
    }

    /**
     * Sets the fragment of the dialog.
     *
     * @param fragment fragment to set
     */
    private void setFragment(Fragment fragment) {
        getChildFragmentManager().beginTransaction().setReorderingAllowed(true).replace(R.id.calibrate_container, fragment).commit();
    }

    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.dialog_calibrate, null))
                .setPositiveButton(R.string.dialog_calibrate_next, null);


        dialog = builder.create();
        dialog.setTitle("-"); // Add default title so it is shown
        dialog.setOnShowListener(dialogInterface -> {
            positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);

            positiveButton.setOnClickListener((b) -> {
                if  (!calibrating) calibrate();
                else dismiss();
            });

            created();
        });

        return dialog;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);

        if (finishedListener != null) finishedListener.onDismiss(dialog);
    }
}
