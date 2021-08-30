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

    ReturnListener listener;

    public ConnectFailedSubfragment(MainContext context, BluetoothHandler bluetooth, ReturnListener listener) {
        super(R.layout.subfragment_connect_failed, context);
        this.bluetooth = bluetooth;
        this.listener = listener;
    }

    @Override
    public void render() {
        device.setText(bluetooth.getHost().getDevice().getName());

        remove.setOnClickListener(v -> {
            bluetooth.getDevices().removeDevice(bluetooth.getHost().getDevice().getAddress());
            bluetooth.getHost().markFailedAsRead();
            listener.called();
        });
        back.setOnClickListener(v -> {
            bluetooth.getHost().markFailedAsRead();
            listener.called();
        });
    }

    @Override
    protected void loadComponents(View view) {
        device = view.findViewById(R.id.connect_failed_device);

        back = view.findViewById(R.id.connect_failed_back);
        remove = view.findViewById(R.id.connect_failed_remove);
    }

    public interface ReturnListener {
        void called();
    }
}
