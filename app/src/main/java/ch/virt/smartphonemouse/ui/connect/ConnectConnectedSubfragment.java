package ch.virt.smartphonemouse.ui.connect;

import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import ch.virt.smartphonemouse.MainActivity;
import ch.virt.smartphonemouse.R;
import ch.virt.smartphonemouse.helper.MainContext;
import ch.virt.smartphonemouse.transmission.BluetoothHandler;
import ch.virt.smartphonemouse.ui.CustomFragment;

public class ConnectConnectedSubfragment extends CustomFragment {

    Chronometer elapsed;
    TextView name;

    Button disconnect;
    Button mouse;

    BluetoothHandler bluetooth;

    public ConnectConnectedSubfragment(MainContext context, BluetoothHandler bluetooth) {
        super(R.layout.subfragment_connect_connected, context);
        this.bluetooth = bluetooth;
    }

    @Override
    public void render() {
        if (bluetooth.isConnected()){

            name.setText(bluetooth.getHost().getDevice().getName());

            elapsed.setBase(bluetooth.getHost().getConnectedSince());
            elapsed.setFormat(getResources().getString(R.string.connect_connected_elapsed));
            elapsed.start();

            disconnect.setOnClickListener(v -> bluetooth.getHost().disconnect());
            mouse.setOnClickListener(v -> {
                bluetooth.getHost().sendReport(false, false, false, 0, 20, 0);
                ((MainActivity) getActivity()).navigate(R.id.drawer_mouse);
            });
        }
    }

    @Override
    protected void loadComponents(View view) {
        elapsed = view.findViewById(R.id.connect_connected_device_elapsed);
        name = view.findViewById(R.id.connect_connected_device_name);
        disconnect = view.findViewById(R.id.connect_connected_disconnect);
        mouse = view.findViewById(R.id.connect_connected_mouse);
    }
}
