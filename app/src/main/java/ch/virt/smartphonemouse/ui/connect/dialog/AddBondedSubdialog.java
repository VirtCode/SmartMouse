package ch.virt.smartphonemouse.ui.connect.dialog;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import ch.virt.smartphonemouse.R;
import ch.virt.smartphonemouse.transmission.BluetoothDiscoverer;
import ch.virt.smartphonemouse.transmission.BluetoothHandler;

/**
 * This class contains the sub page of the add dialog, which is displayed when a device is bonded with the smartphone.
 */
public class AddBondedSubdialog extends Fragment {

    private BluetoothHandler handler;
    private TextView error;

    private BluetoothDiscoverer.DiscoveredDevice target;

    /**
     * Creates the sub dialog.
     *
     * @param handler handler to check whether still bonded
     * @param target  target device to check for
     */
    public AddBondedSubdialog(BluetoothHandler handler, BluetoothDiscoverer.DiscoveredDevice target) {
        super(R.layout.subdialog_add_bonded);

        this.handler = handler;
        this.target = target;
    }

    /**
     * Checks if the device is still bonded.
     * If it is still bonded, an error message will be displayed.
     *
     * @return whether it is NOT bonded
     */
    public boolean check() {
        if (handler.isBonded(target.getAddress())) {
            error.setVisibility(View.VISIBLE);
            return false;
        } else {
            error.setVisibility(View.GONE);
            return true;
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        error = view.findViewById(R.id.add_bonded_error);
    }
}
