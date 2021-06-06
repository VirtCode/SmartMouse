package ch.virt.smartphonemouse.ui.connect.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import ch.virt.smartphonemouse.R;
import ch.virt.smartphonemouse.helper.MainContext;
import ch.virt.smartphonemouse.transmission.BluetoothHandler;
import ch.virt.smartphonemouse.ui.CustomFragment;

public class AddDialog extends DialogFragment {
    private static final int SELECT_STATE = 0;
    private static final int MANUAL_STATE = 1;

    BluetoothHandler bluetoothHandler;
    MainContext mainContext;

    Button positiveButton, negativeButton, neutralButton;
    AlertDialog dialog;

    CustomFragment currentSub;
    private int state;



    public AddDialog(BluetoothHandler bluetoothHandler, MainContext mainContext) {
        this.bluetoothHandler = bluetoothHandler;
        this.mainContext = mainContext;
    }

    public void created(){
        neutralButton.setVisibility(View.GONE);
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM); // Enable keyboard to be working

        showSelect();
    }

    private void setFragment(CustomFragment fragment){
        currentSub = fragment;
        getChildFragmentManager().beginTransaction().setReorderingAllowed(true).replace(R.id.add_container, currentSub).commit();
    }

    public void showSelect(){
        state = SELECT_STATE;
        setFragment(new AddSelectSubdialog(mainContext, bluetoothHandler));

        positiveButton.setVisibility(View.GONE);
        neutralButton.setVisibility(View.VISIBLE);

        neutralButton.setText(R.string.dialog_add_select_manual);
        dialog.setTitle(R.string.dialog_add_select_title);

    }

    public void showManual(){
        state = MANUAL_STATE;
        setFragment(new AddManualSubdialog(mainContext));

        positiveButton.setVisibility(View.VISIBLE);
        neutralButton.setVisibility(View.GONE);

        dialog.setTitle(getString(R.string.dialog_add_manual_title));
    }

    public void onNext(){
        switch (state){
            case MANUAL_STATE:
                if (((AddManualSubdialog) currentSub).check()) {

                }

            case SELECT_STATE:

                break;
        }
    }

    public void onNeutral(){
        switch (state){
            case SELECT_STATE:
                showManual();
                break;
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.dialog_add, null))
                .setPositiveButton(R.string.dialog_add_next, null)
                .setNegativeButton(R.string.dialog_add_cancel, null)
                .setNeutralButton("-", null);


        dialog = builder.create();
        dialog.setTitle("-"); // Add default title so it is shown
        dialog.setOnShowListener(dialogInterface -> {
            positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            neutralButton = dialog.getButton(AlertDialog.BUTTON_NEUTRAL);

            positiveButton.setOnClickListener((v) -> onNext());
            neutralButton.setOnClickListener((v) -> onNeutral());

            created();
        });

        return dialog;
    }


}
