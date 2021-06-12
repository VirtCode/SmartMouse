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
import ch.virt.smartphonemouse.helper.Listener;
import ch.virt.smartphonemouse.transmission.BluetoothHandler;
import ch.virt.smartphonemouse.transmission.HostDevice;

public class InfoDialog extends DialogFragment {

    private final BluetoothHandler bluetooth;
    private final HostDevice device;
    private final Listener closed;

    public InfoDialog(BluetoothHandler bluetooth, HostDevice device, Listener closed) {
        this.bluetooth = bluetooth;
        this.device = device;
        this.closed = closed;
    }

    public void populate(View view) {

        ((TextView) view.findViewById(R.id.info_address)).setText(device.getAddress());
        ((TextView) view.findViewById(R.id.info_name)).setText(device.getName());
        ((TextView) view.findViewById(R.id.info_last)).setText(device.getLastConnected() == -1 ? view.getResources().getString(R.string.dialog_info_last_never) : new SimpleDateFormat(view.getResources().getString(R.string.dialog_info_last_format)).format(new Date(device.getLastConnected())));
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);

        closed.called();
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
