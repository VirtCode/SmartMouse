package ch.virt.smartphonemouse.ui.connect;

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
 * This class is a sub fragment for the connect page.
 * This fragment is displayed when the app has a successful connection.
 */
public class ConnectConnectedSubfragment extends Fragment {

    private Chronometer elapsed;
    private TextView name;

    private Button disconnect;
    private Button mouse;

    private final BluetoothHandler bluetooth;

    /**
     * Creates this sub fragment.
     *
     * @param bluetooth bluetooth handler to read status from
     */
    public ConnectConnectedSubfragment(BluetoothHandler bluetooth) {
        super(R.layout.subfragment_connect_connected);
        this.bluetooth = bluetooth;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        elapsed = view.findViewById(R.id.connect_connected_device_elapsed);
        name = view.findViewById(R.id.connect_connected_device_name);
        disconnect = view.findViewById(R.id.connect_connected_disconnect);
        mouse = view.findViewById(R.id.connect_connected_mouse);

        if (bluetooth.isConnected()) {

            name.setText(bluetooth.getHost().getDevice().getName());

            elapsed.setBase(bluetooth.getHost().getConnectedSince());
            elapsed.setFormat(getResources().getString(R.string.connect_connected_elapsed));
            elapsed.start();

            disconnect.setOnClickListener(v -> bluetooth.getHost().disconnect());
            mouse.setOnClickListener(v -> ((MainActivity) getActivity()).navigate(R.id.drawer_mouse));
        }
    }
}
