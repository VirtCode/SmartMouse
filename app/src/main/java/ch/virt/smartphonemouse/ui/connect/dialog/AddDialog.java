package ch.virt.smartphonemouse.ui.connect.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import ch.virt.smartphonemouse.R;
import ch.virt.smartphonemouse.helper.MainContext;
import ch.virt.smartphonemouse.transmission.BluetoothHandler;

public class AddDialog extends DialogFragment {

    BluetoothHandler bluetoothHandler;
    MainContext mainContext;

    Button positiveButton, negativeButton, neutralButton;
    AlertDialog dialog;



    public AddDialog(BluetoothHandler bluetoothHandler, MainContext mainContext) {
        this.bluetoothHandler = bluetoothHandler;
        this.mainContext = mainContext;
    }

    public void created(){
        neutralButton.setVisibility(View.GONE);

        showSelect();
    }

    public void showSelect(){
        getChildFragmentManager().beginTransaction().setReorderingAllowed(true).replace(R.id.add_container, new AddSelectSubdialog(mainContext, bluetoothHandler)).commit();

        positiveButton.setVisibility(View.GONE);
        neutralButton.setVisibility(View.VISIBLE);

        neutralButton.setText(R.string.dialog_add_select_manual);
        dialog.setTitle(R.string.dialog_add_select_title);

    }

    public void showManual(){

    }

    public void onNext(){


    }

    public void onNeutral(){

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.dialog_add, null))
                .setPositiveButton(R.string.dialog_add_next, null)
                .setNegativeButton(R.string.dialog_add_cancel, null)
                .setNeutralButton("Bruh", null);


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
