package ch.virt.smartphonemouse.ui.connect.dialog;

import android.view.View;
import android.widget.TextView;

import ch.virt.smartphonemouse.R;
import ch.virt.smartphonemouse.helper.MainContext;
import ch.virt.smartphonemouse.transmission.BluetoothDiscoverer;
import ch.virt.smartphonemouse.ui.CustomFragment;

public class AddSuccessSubdialog extends CustomFragment {

    private TextView name;

    private BluetoothDiscoverer.DiscoveredDevice target;

    public AddSuccessSubdialog(MainContext context, BluetoothDiscoverer.DiscoveredDevice target) {
        super(R.layout.subdialog_add_success, context);

        this.target = target;
    }

    @Override
    public void render() {
        name.setText(target.getName());
    }

    @Override
    protected void loadComponents(View view) {
        name = view.findViewById(R.id.add_success_name);
    }
}
