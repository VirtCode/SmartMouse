package ch.virt.smartphonemouse.ui.home;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import ch.virt.smartphonemouse.MainActivity;
import ch.virt.smartphonemouse.R;
import ch.virt.smartphonemouse.transmission.BluetoothHandler;

/**
 * This sub fragment gets shown on the home screen when a device is currently connected.
 */
public class HomeConnectedSubfragment extends Fragment {

    private final BluetoothHandler handler;

    private Chronometer chronometer;
    private TextView device;
    private Button more;

    /**
     * Creates the sub fragment.
     *
     * @param handler bluetooth handler to get connected information from
     */
    public HomeConnectedSubfragment(BluetoothHandler handler) {
        super(R.layout.subfragment_home_connected);
        this.handler = handler;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        chronometer = view.findViewById(R.id.home_connected_device_elapsed);
        device = view.findViewById(R.id.home_connected_device_name);
        more = view.findViewById(R.id.home_connected_more);


        chronometer.setBase(handler.getHost().getConnectedSince());
        chronometer.setFormat(getResources().getString(R.string.home_connected_elapsed));
        chronometer.start();

        device.setText(handler.getHost().getDevice().getName());

        more.setOnClickListener(v -> ((MainActivity) getActivity()).navigate(R.id.drawer_connect));
    }
}
