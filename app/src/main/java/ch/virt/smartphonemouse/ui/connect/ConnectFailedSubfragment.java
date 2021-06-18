package ch.virt.smartphonemouse.ui.connect;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import ch.virt.smartphonemouse.R;
import ch.virt.smartphonemouse.helper.MainContext;
import ch.virt.smartphonemouse.transmission.BluetoothHandler;
import ch.virt.smartphonemouse.ui.CustomFragment;

public class ConnectFailedSubfragment extends CustomFragment {

    BluetoothHandler bluetooth;

    TextView device;
    Button back;
    Button remove;

    public ConnectFailedSubfragment(MainContext context, BluetoothHandler bluetooth) {
        super(R.layout.subfragment_connect_failed, context);
        this.bluetooth = bluetooth;
    }

    @Override
    public void render() {
        device.setText(bluetooth.getHost().getDevice().getName());

        remove.setOnClickListener(v -> {
            bluetooth.getDevices().removeDevice(bluetooth.getHost().getDevice().getAddress());
            bluetooth.getHost().markFailedAsRead();
            main.refresh();
        });
        back.setOnClickListener(v -> {
            bluetooth.getHost().markFailedAsRead();
            main.refresh();
        });
    }

    @Override
    protected void loadComponents(View view) {
        device = view.findViewById(R.id.connect_failed_device);

        back = view.findViewById(R.id.connect_failed_back);
        remove = view.findViewById(R.id.connect_failed_remove);
    }
}
