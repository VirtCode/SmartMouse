package ch.virt.smartphonemouse.ui.connect.dialog;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import ch.virt.smartphonemouse.R;
import ch.virt.smartphonemouse.transmission.BluetoothDiscoverer;

/**
 * This class holds the sub page for the add dialog that informs the user that they have successfully added a device.
 */
public class AddSuccessSubdialog extends Fragment {

    private TextView name;

    private final BluetoothDiscoverer.DiscoveredDevice target;

    /**
     * Creates the sub dialog.
     *
     * @param target device which was added, used to display its name
     */
    public AddSuccessSubdialog(BluetoothDiscoverer.DiscoveredDevice target) {
        super(R.layout.subdialog_add_success);

        this.target = target;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        name = view.findViewById(R.id.add_success_name);
        name.setText(target.getName());
    }
}
