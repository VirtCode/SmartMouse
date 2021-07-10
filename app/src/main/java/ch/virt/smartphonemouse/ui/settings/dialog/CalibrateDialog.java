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

import ch.virt.smartphonemouse.R;
import ch.virt.smartphonemouse.helper.Listener;
import ch.virt.smartphonemouse.helper.MainContext;
import ch.virt.smartphonemouse.ui.CustomFragment;

public class CalibrateDialog extends DialogFragment {

    AlertDialog dialog;

    Button positiveButton;

    private final MainContext main;
    private final Listener updateListener;

    public CalibrateDialog(MainContext main, Listener updateListener) {
        this.main = main;
        this.updateListener = updateListener;
    }

    private void created(){
        dialog.setTitle(R.string.dialog_calibrate_samplingrate_title);

        positiveButton.setEnabled(false);
        dialog.setCanceledOnTouchOutside(false);

        setFragment(new SamplingRateSubdialog(main, () -> positiveButton.post(this::finished)));
    }

    private void finished(){
        dialog.setTitle(R.string.dialog_calibrate_finished_title);

        dialog.setCanceledOnTouchOutside(true);
        positiveButton.setEnabled(true);

        setFragment(new CalibrateFinishedSubdialog(main));
    }

    private void setFragment(CustomFragment fragment){
        getChildFragmentManager().beginTransaction().setReorderingAllowed(true).replace(R.id.calibrate_container, fragment).commit();
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);

        updateListener.called();
    }

    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.dialog_calibrate, null))
                .setPositiveButton(R.string.dialog_calibrate_done, null);


        dialog = builder.create();
        dialog.setTitle("-"); // Add default title so it is shown
        dialog.setOnShowListener(dialogInterface -> {
            positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);

            created();
        });

        return dialog;
    }



}
