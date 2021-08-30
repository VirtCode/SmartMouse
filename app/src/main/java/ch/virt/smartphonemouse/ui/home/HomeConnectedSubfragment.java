package ch.virt.smartphonemouse.ui.home;

import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import ch.virt.smartphonemouse.MainActivity;
import ch.virt.smartphonemouse.R;
import ch.virt.smartphonemouse.helper.MainContext;
import ch.virt.smartphonemouse.transmission.BluetoothHandler;
import ch.virt.smartphonemouse.ui.CustomFragment;

public class HomeConnectedSubfragment extends CustomFragment {

    BluetoothHandler handler;

    Chronometer chronometer;
    TextView device;

    Button more;

    public HomeConnectedSubfragment(BluetoothHandler handler, MainContext main) {
        super(R.layout.subfragment_home_connected, main);
        this.handler = handler;
    }

    @Override
    public void render() {
        chronometer.setBase(handler.getHost().getConnectedSince());
        chronometer.setFormat(getResources().getString(R.string.home_connected_elapsed));
        chronometer.start();

        device.setText(handler.getHost().getDevice().getName());

        more.setOnClickListener(v -> ((MainActivity) getActivity()).navigate(R.id.drawer_connect));
    }

    @Override
    protected void loadComponents(View view) {
        chronometer = view.findViewById(R.id.home_connected_device_elapsed);
        device = view.findViewById(R.id.home_connected_device_name);
        more = view.findViewById(R.id.home_connected_more);
    }
}
