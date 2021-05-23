package ch.virt.smartphonemouse.ui.home;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.w3c.dom.Text;

import ch.virt.smartphonemouse.R;
import ch.virt.smartphonemouse.transmission.BluetoothHandler;

/**
 * @author VirtCode
 * @version 1.0
 */
public class HomeConnectedSubfragment extends Fragment {

    BluetoothHandler handler;

    Chronometer chronometer;
    TextView device;

    Button disconnect;

    public HomeConnectedSubfragment(BluetoothHandler handler) {
        super(R.layout.subfragment_home_connected);
        this.handler = handler;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        chronometer = view.findViewById(R.id.home_connected_device_elapsed);
        device = view.findViewById(R.id.home_connected_device_name);
        disconnect = view.findViewById(R.id.home_connected_disconnect);

        chronometer.setBase(handler.connectedSince());
        chronometer.setFormat(getResources().getString(R.string.home_connected_elapsed));
        chronometer.start();

        device.setText(handler.connectedDevice());

        disconnect.setOnClickListener(v -> handler.disconnect());
    }
}
