package ch.virt.smartphonemouse.ui.connect.dialog;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.location.LocationManagerCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import ch.virt.smartphonemouse.R;
import ch.virt.smartphonemouse.transmission.BluetoothDiscoverer;
import ch.virt.smartphonemouse.transmission.BluetoothHandler;
import ch.virt.smartphonemouse.transmission.HostDevice;

/**
 * This dialog is shown when the user wants to add a device.
 */
public class AddDialog extends DialogFragment {
    private static final int SELECT_STATE = 0;
    private static final int MANUAL_STATE = 1;
    private static final int BONDED_STATE = 2;
    private static final int SUCCESS_STATE = 3;
    private static final int ALREADY_STATE = 4;
    private static final int REQUEST_PERMISSION_STATE = 5;
    private static final int REQUEST_SETTING_STATE = 6;

    private final BluetoothHandler bluetoothHandler;

    private Button positiveButton, negativeButton, neutralButton;
    private DialogInterface.OnDismissListener dismissListener;
    private AlertDialog dialog;

    private int state;
    private Fragment currentFragment;
    private BluetoothDiscoverer.DiscoveredDevice target;

    private ActivityResultLauncher<String> requestLocation;
    private ActivityResultLauncher<Intent> enableLocation;

    /**
     * Creates an add dialog.
     * @param bluetoothHandler bluetooth handler to use
     */
    public AddDialog(BluetoothHandler bluetoothHandler) {
        this.bluetoothHandler = bluetoothHandler;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize Location requests
        requestLocation = registerForActivityResult(new ActivityResultContracts.RequestPermission(), this::checkPermission);
        enableLocation = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> positiveButton.post(this::checkSetting));
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

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        if (bluetoothHandler.getDiscoverer().isScanning()) bluetoothHandler.getDiscoverer().stopDiscovery();

        super.onDismiss(dialog);

        dismissListener.onDismiss(dialog);
    }

    /**
     * This method gets called when the dialog is shown.
     */
    private void created(){
        neutralButton.setVisibility(View.GONE);
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM); // Enable keyboard to be working

        showRequestPermission();
    }

    /**
     * This method sets a sub fragment.
     * @param fragment fragment to be set
     */
    private void setFragment(Fragment fragment){
        currentFragment = fragment;
        getChildFragmentManager().beginTransaction().setReorderingAllowed(true).replace(R.id.add_container, currentFragment).commit();
    }


    /**
     * This method shows the fragment that requests the permission to discover devices.
     */
    public void showRequestPermission(){
        state = REQUEST_PERMISSION_STATE;

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            showRequestSetting(); // Get on if already granted
            return;
        }

        setFragment(new AddRequestPermissionSubdialog());

        dialog.setTitle(R.string.dialog_add_request_permission_title);
        positiveButton.setVisibility(View.VISIBLE);
        neutralButton.setVisibility(View.VISIBLE);
        neutralButton.setText(R.string.dialog_add_select_manual);
    }

    /**
     * This method shows the fragment that requests the user to turn on location in order to discover devices.
     */
    public void showRequestSetting(){
        state = REQUEST_SETTING_STATE;

        if (LocationManagerCompat.isLocationEnabled((LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE))){
            showSelect(); // Get on if already enabled
            return;
        }

        setFragment(new AddRequestSettingSubdialog());

        dialog.setTitle(R.string.dialog_add_request_setting_title);
        positiveButton.setVisibility(View.VISIBLE);
        neutralButton.setVisibility(View.VISIBLE);
        neutralButton.setText(R.string.dialog_add_select_manual);
    }

    /**
     * This method shows the fragment where the user can select form nearby devices.
     */
    public void showSelect(){
        state = SELECT_STATE;

        AddSelectSubdialog fragment = new AddSelectSubdialog(bluetoothHandler);
        fragment.setSelectListener(this::selected);
        setFragment(fragment);

        dialog.setTitle(R.string.dialog_add_select_title);
        positiveButton.setVisibility(View.GONE);
        neutralButton.setVisibility(View.VISIBLE);
        neutralButton.setText(R.string.dialog_add_select_manual);
    }

    /**
     * This method shows the fragment where the user can enter their device details manually.
     */
    public void showManual(){
        state = MANUAL_STATE;

        setFragment(new AddManualSubdialog());

        dialog.setTitle(getString(R.string.dialog_add_manual_title));
        positiveButton.setVisibility(View.VISIBLE);
        neutralButton.setVisibility(View.GONE);
    }

    /**
     * This method shows the fragment which tells the user that they have to remove the target device from their bonded devices.
     */
    public void showBonded(){
        state = BONDED_STATE;

        setFragment(new AddBondedSubdialog(bluetoothHandler, target));

        dialog.setTitle(getString(R.string.dialog_add_bonded_title));
        positiveButton.setVisibility(View.VISIBLE);
        neutralButton.setVisibility(View.GONE);
    }

    /**
     * This method shows the fragment which tells the user that the device is already added.
     */
    public void showAlready(){
        state = ALREADY_STATE;

        setFragment(new AddAlreadySubdialog());

        dialog.setTitle(getString(R.string.dialog_add_already_title));
        neutralButton.setVisibility(View.GONE);
        negativeButton.setVisibility(View.GONE);
        positiveButton.setVisibility(View.VISIBLE);
        positiveButton.setText(R.string.dialog_add_already_positive);
    }

    /**
     * This method shows the fragment which tells the user that the device was added successfully.
     */
    public void showSuccess(){
        state = SUCCESS_STATE;

        setFragment(new AddSuccessSubdialog(target));

        dialog.setTitle(getString(R.string.dialog_add_success_title));
        neutralButton.setVisibility(View.GONE);
        negativeButton.setVisibility(View.GONE);
        positiveButton.setVisibility(View.VISIBLE);
        positiveButton.setText(R.string.dialog_add_success_positive);
    }


    /**
     * Adds the target device to the device storage and displays the success message.
     */
    public void finished(){
        bluetoothHandler.getDevices().addDevice(new HostDevice(target.getAddress(), target.getName()));
        showSuccess();
    }

    /**
     * Proceeds after a device has been selected.
     * @param device selected device.
     */
    public void selected(BluetoothDiscoverer.DiscoveredDevice device){
        target = device;

        if (bluetoothHandler.getDevices().hasDevice(device.getAddress())) showAlready();
        else if (bluetoothHandler.isBonded(device.getAddress())) showBonded();
        else finished();
    }


    /**
     * This method is called when the positive button is clicked.
     */
    public void onNext(){
        switch (state){
            case REQUEST_PERMISSION_STATE:
                requestPermission();
                break;
            case REQUEST_SETTING_STATE:
                requestSetting();
                break;
            case MANUAL_STATE:
                if (((AddManualSubdialog) currentFragment).check()) selected(((AddManualSubdialog) currentFragment).createDevice());
                break;
            case BONDED_STATE:
                    finished();
                break;
            case SUCCESS_STATE:
            case ALREADY_STATE:
                dismiss();
                onDismiss(getDialog());
        }
    }

    /**
     * This method is called when the neutral button is clicked.
     */
    public void onNeutral(){
        if (state == SELECT_STATE || state == REQUEST_PERMISSION_STATE || state == REQUEST_SETTING_STATE) showManual();
    }


    /**
     * Launches the request permission intent to request location permissions.
     */
    public void requestPermission(){
        requestLocation.launch(Manifest.permission.ACCESS_FINE_LOCATION);
    }

    /**
     * Proceeds after the permission may have been granted.
     * @param isGranted whether it was granted
     */
    public void checkPermission(boolean isGranted){
        if (isGranted) {
            showRequestSetting();
        } else ((AddRequestPermissionSubdialog) currentFragment).showError();
    }

    /**
     * Launches the request intent to request to turn on location.
     */
    public void requestSetting(){
        if (LocationManagerCompat.isLocationEnabled((LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE))){
            showSelect();
        } else {
            enableLocation.launch(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }
    }

    /**
     * Checks whether the location setting was turned on.
     */
    public void checkSetting(){
        if (LocationManagerCompat.isLocationEnabled((LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE))){
            showSelect();
        } else {
            ((AddRequestSettingSubdialog) currentFragment).showError();
        }
    }


    /**
     * Sets the dismiss listener.
     * The listener should be set before the dialog is shown.
     *
     * @param dismissListener dismiss listener that will be passed to the dialog
     * @see Dialog#setOnDismissListener(DialogInterface.OnDismissListener)
     */
    public void setOnDismissListener(DialogInterface.OnDismissListener dismissListener) {
        this.dismissListener = dismissListener;
    }
}
