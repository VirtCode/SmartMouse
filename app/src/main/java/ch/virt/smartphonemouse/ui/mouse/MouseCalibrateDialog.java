package ch.virt.smartphonemouse.ui.mouse;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import ch.virt.smartphonemouse.R;
import ch.virt.smartphonemouse.helper.MainContext;
import ch.virt.smartphonemouse.ui.CustomFragment;
import ch.virt.smartphonemouse.ui.settings.dialog.SamplingRateSubdialog;

public class MouseCalibrateDialog extends DialogFragment {

    private AlertDialog dialog;
    private Button positiveButton, negativeButton;

    private boolean introduction;

    private MainContext main;

    public MouseCalibrateDialog(MainContext main) {
        this.main = main;
    }

    private void created(){
        dialog.setTitle(R.string.dialog_mouse_calibrate_title_intro);

        introduction = true;

        positiveButton.setEnabled(true);
        negativeButton.setVisibility(View.VISIBLE);

        setFragment(new MouseMessageSubdialog(main.getResources().getString(R.string.mouse_message_calibrate), main));
    }

    private void next(){
        dialog.setTitle(R.string.dialog_mouse_calibrate_title_calibrate);

        if (introduction) {

            dialog.setCanceledOnTouchOutside(false);
            negativeButton.setVisibility(View.GONE);

            positiveButton.setEnabled(false);
            positiveButton.setText(R.string.dialog_mouse_calibrate_done);

            introduction = false;

            setFragment(new SamplingRateSubdialog(main, () -> positiveButton.post(this::finished)));
        }else dismiss();

    }

    private void finished(){
        dialog.setTitle(R.string.dialog_mouse_calibrate_title_finished);

        dialog.setCanceledOnTouchOutside(true);
        positiveButton.setEnabled(true);

        setFragment(new MouseMessageSubdialog(main.getResources().getString(R.string.dialog_mouse_calibrate_finished), main));
    }

    private void setFragment(CustomFragment fragment){
        getChildFragmentManager().beginTransaction().setReorderingAllowed(true).replace(R.id.mouse_container, fragment).commit();
    }

    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.dialog_mouse, null))
                .setPositiveButton(R.string.dialog_mouse_next, null)
                .setNegativeButton(R.string.dialog_mouse_calibrate_cancel, null);


        dialog = builder.create();
        dialog.setTitle("-"); // Add default title so it is shown
        dialog.setOnShowListener(dialogInterface -> {
            positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(view -> next());

            negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);

            created();
        });

        return dialog;
    }

}
