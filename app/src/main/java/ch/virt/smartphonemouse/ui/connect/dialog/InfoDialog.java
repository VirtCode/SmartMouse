package ch.virt.smartphonemouse.ui.connect.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.text.SimpleDateFormat;
import java.util.Date;

import ch.virt.smartphonemouse.R;
import ch.virt.smartphonemouse.transmission.BluetoothHandler;
import ch.virt.smartphonemouse.transmission.HostDevice;

/**
 * This dialog is shown when the user wants to see more information about a device.
 */
public class InfoDialog extends DialogFragment {

    private final BluetoothHandler bluetooth;
    private final HostDevice device;

    private DialogInterface.OnDismissListener dismissListener;

    /**
     * Creates the info dialog.
     *
     * @param bluetooth bluetooth handler to remove device from
     * @param device    device that is viewed
     */
    public InfoDialog(BluetoothHandler bluetooth, HostDevice device) {
        this.bluetooth = bluetooth;
        this.device = device;
    }

    /**
     * Populates the text views on the view with the data of the device.
     *
     * @param view view to populate
     */
    private void populate(View view) {
        ((TextView) view.findViewById(R.id.info_address)).setText(device.getAddress());
        ((TextView) view.findViewById(R.id.info_name)).setText(device.getName());
        ((TextView) view.findViewById(R.id.info_last)).setText(device.getLastConnected() == -1 ? view.getResources().getString(R.string.dialog_info_last_never) : new SimpleDateFormat(view.getResources().getString(R.string.dialog_info_last_format)).format(new Date(device.getLastConnected())));
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

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);

        dismissListener.onDismiss(dialog);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_info, null);
        populate(view);

        builder.setView(view)
                .setPositiveButton(R.string.dialog_info_positive, null)
                .setNeutralButton(R.string.dialog_info_delete, (dialog, which) -> bluetooth.getDevices().removeDevice(device.getAddress()));


        Dialog dialog = builder.create();
        dialog.setTitle(R.string.dialog_info_title);

        return dialog;
    }
}
