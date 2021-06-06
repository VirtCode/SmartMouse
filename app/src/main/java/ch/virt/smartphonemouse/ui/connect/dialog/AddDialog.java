package ch.virt.smartphonemouse.ui.connect.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import ch.virt.smartphonemouse.R;
import ch.virt.smartphonemouse.helper.MainContext;
import ch.virt.smartphonemouse.transmission.BluetoothDiscoverer;
import ch.virt.smartphonemouse.transmission.BluetoothHandler;
import ch.virt.smartphonemouse.ui.CustomFragment;

public class AddDialog extends DialogFragment {
    private static final int SELECT_STATE = 0;
    private static final int MANUAL_STATE = 1;
    private static final int BONDED_STATE = 2;
    private static final int SUCCESS_STATE = 3;
    private static final int ALREADY_STATE = 4;

    BluetoothHandler bluetoothHandler;
    MainContext mainContext;

    Button positiveButton, negativeButton, neutralButton;
    AlertDialog dialog;

    CustomFragment currentSub;
    private int state;

    private BluetoothDiscoverer.DiscoveredDevice target;

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
        setFragment(new AddSelectSubdialog(mainContext, bluetoothHandler, () -> {
            selected(((AddSelectSubdialog) currentSub).getSelected());
        }));

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

    public void showBonded(){
        state = BONDED_STATE;
        setFragment(new AddBondedSubdialog(mainContext, bluetoothHandler, target));

        positiveButton.setVisibility(View.VISIBLE);
        neutralButton.setVisibility(View.GONE);

        dialog.setTitle(getString(R.string.dialog_add_bonded_title));
    }

    public void showSuccess(){
        state = SUCCESS_STATE;
        setFragment(new AddSuccessSubdialog(mainContext, target));

        neutralButton.setVisibility(View.GONE);
        negativeButton.setVisibility(View.GONE);
        positiveButton.setVisibility(View.VISIBLE);
        positiveButton.setText(R.string.dialog_add_success_positive);

        dialog.setTitle(getString(R.string.dialog_add_success_title));
    }

    public void showAlready(){
        state = ALREADY_STATE;
        setFragment(new AddAlreadySubdialog(mainContext));

        neutralButton.setVisibility(View.GONE);
        negativeButton.setVisibility(View.GONE);
        positiveButton.setVisibility(View.VISIBLE);
        positiveButton.setText(R.string.dialog_add_already_positive);

        dialog.setTitle(getString(R.string.dialog_add_already_title));
    }

    public void finished(){
        showSuccess();
    }

    public void selected(BluetoothDiscoverer.DiscoveredDevice device){
        target = device;

        if (false) showAlready();
        if (bluetoothHandler.isBonded(device.getAddress())) showBonded();
        else finished();
    }

    public void onNext(){
        switch (state){
            case MANUAL_STATE:
                if (((AddManualSubdialog) currentSub).check()) selected(((AddManualSubdialog) currentSub).createDevice());
                break;
            case BONDED_STATE:
                if (((AddBondedSubdialog) currentSub).check()) finished();
                break;
            case SUCCESS_STATE:
            case ALREADY_STATE:
                dismiss();
        }
    }

    public void onNeutral(){
        if (state == SELECT_STATE) showManual();
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
