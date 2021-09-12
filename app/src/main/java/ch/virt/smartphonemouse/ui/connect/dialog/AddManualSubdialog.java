package ch.virt.smartphonemouse.ui.connect.dialog;

import android.bluetooth.BluetoothClass;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;

import ch.virt.smartphonemouse.R;
import ch.virt.smartphonemouse.transmission.BluetoothDiscoverer;

/**
 * This class contains the sub page of the add dialog that lets the user add their own custom device by their bluetooth mac address.
 */
public class AddManualSubdialog extends Fragment {

    TextInputLayout nameLayout, macLayout;

    /**
     * Creates the sub dialog.
     */
    public AddManualSubdialog() {
        super(R.layout.subdialog_add_manual);
    }

    /**
     * Checks whether the current entered name and mac bluetooth address are correct.
     *
     * @return whether the inputs are correct
     */
    public boolean check() {
        boolean valid = true;

        if (nameLayout.getEditText().getText() == null || nameLayout.getEditText().getText().toString().equals("")) {
            nameLayout.setErrorEnabled(true);
            nameLayout.setError(getString(R.string.dialog_add_manual_name_error));
            valid = false;
        } else {
            nameLayout.setErrorEnabled(false);
        }

        if (macLayout.getEditText().getText() == null || !macLayout.getEditText().getText().toString().matches("^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$")) {
            macLayout.setErrorEnabled(true);
            macLayout.setError(getString(R.string.dialog_add_manual_mac_error));
            valid = false;
        } else {
            macLayout.setErrorEnabled(false);
        }


        return valid;
    }

    /**
     * Returns a discovered device, created from the entered details.
     *
     * @return discovered device
     */
    public BluetoothDiscoverer.DiscoveredDevice createDevice() {
        return new BluetoothDiscoverer.DiscoveredDevice(nameLayout.getEditText().getText().toString(), macLayout.getEditText().getText().toString(), BluetoothClass.Device.Major.UNCATEGORIZED);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        nameLayout = view.findViewById(R.id.add_manual_name);
        macLayout = view.findViewById(R.id.add_manual_mac);
    }
}
