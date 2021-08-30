package ch.virt.smartphonemouse.ui.mouse;

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

public class MouseUsageDialog extends DialogFragment {

    private AlertDialog dialog;
    private Button positiveButton;

    private int index;
    private int maxIndex = 4;

    private MainContext main;
    private Listener finishedListener;

    public MouseUsageDialog(MainContext main, Listener finishedListener) {
        this.main = main;
        this.finishedListener = finishedListener;
    }

    private void create(){
        showFragment();

        dialog.setCanceledOnTouchOutside(false);
    }


    private void next(){
        if (index == maxIndex) {
            dismiss();
            return;
        }

        index++;
        if (index == maxIndex) {

            dialog.setCanceledOnTouchOutside(true);
            setFragment(new MouseUsageFinishedSubdialog(main));

        }else showFragment();

    }

    private void showFragment(){
        setFragment(new MouseMessageSubdialog(getResources().getString(getCurrentMessage(index)), main));
    }

    public int getCurrentMessage(int index){

        switch (index) {
            case 0: return R.string.dialog_mouse_usage_even;
            case 1: return R.string.dialog_mouse_usage_move;
            case 2: return R.string.dialog_mouse_usage_buttons;
            case 3: return R.string.dialog_mouse_usage_return;
        }

        return 0;

    }

    private void setFragment(CustomFragment fragment){
        getChildFragmentManager().beginTransaction().setReorderingAllowed(true).replace(R.id.mouse_container, fragment).commit();
    }

    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.dialog_mouse, null))
                .setPositiveButton(R.string.dialog_mouse_usage_next, null);

        dialog = builder.create();
        dialog.setTitle(R.string.dialog_mouse_usage_title); // Add default title so it is shown
        dialog.setOnShowListener(dialogInterface -> {
            positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(view -> next());

            create();
        });

        return dialog;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        finishedListener.called();

        super.onDismiss(dialog);
    }
}
