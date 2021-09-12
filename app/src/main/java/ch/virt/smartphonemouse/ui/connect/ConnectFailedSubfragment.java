package ch.virt.smartphonemouse.ui.connect;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import ch.virt.smartphonemouse.R;
import ch.virt.smartphonemouse.transmission.BluetoothHandler;

/**
 * This is a sub fragment for the connect page.
 * This fragment is shown when a recent connection failed.
 */
public class ConnectFailedSubfragment extends Fragment {

    private final BluetoothHandler bluetooth;

    private TextView device;
    private Button back;
    private Button remove;

    private ReturnListener listener;

    /**
     * Creates the sub fragment.
     * @param bluetooth bluetooth handler to query information from
     */
    public ConnectFailedSubfragment(BluetoothHandler bluetooth) {
        super(R.layout.subfragment_connect_failed);
        this.bluetooth = bluetooth;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        device = view.findViewById(R.id.connect_failed_device);

        back = view.findViewById(R.id.connect_failed_back);
        remove = view.findViewById(R.id.connect_failed_remove);


        device.setText(bluetooth.getHost().getDevice().getName());

        remove.setOnClickListener(v -> {
            bluetooth.getDevices().removeDevice(bluetooth.getHost().getDevice().getAddress());
            bluetooth.getHost().markFailedAsRead();
            listener.returned();
        });
        back.setOnClickListener(v -> {
            bluetooth.getHost().markFailedAsRead();
            listener.returned();
        });
    }

    /**
     * Sets the return listener that is called when the user wants to return from this fragment
     * @param listener return listener
     */
    public void setReturnListener(ReturnListener listener) {
        this.listener = listener;
    }

    /**
     * This interface is a basic listener that is called when the user returns from the failure message.
     */
    public interface ReturnListener {

        /**
         * Called when the user returns.
         */
        void returned();
    }
}
