package ch.virt.smartphonemouse.ui;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import ch.virt.smartphonemouse.R;
import ch.virt.smartphonemouse.transmission.BluetoothHandler;
import ch.virt.smartphonemouse.ui.connect.ConnectConnectedSubfragment;
import ch.virt.smartphonemouse.ui.connect.ConnectConnectingSubfragment;
import ch.virt.smartphonemouse.ui.connect.ConnectFailedSubfragment;
import ch.virt.smartphonemouse.ui.connect.ConnectSelectSubfragment;

/**
 * This fragment allows the user to see the current connection status and to change it.
 */
public class ConnectFragment extends Fragment {

    private ImageView status;
    private TextView statusText;

    private BluetoothHandler bluetooth;

    /**
     * Creates the fragment.
     *
     * @param bluetooth bluetooth handler used for bluetooth operations
     */
    public ConnectFragment(BluetoothHandler bluetooth) {
        super(R.layout.fragment_connect);
        this.bluetooth = bluetooth;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        status = view.findViewById(R.id.connect_status);
        statusText = view.findViewById(R.id.connect_status_text);

        update();
    }

    /**
     * Updates the content on the page according to the current status.
     */
    public void update() {
        if (bluetooth.isInitialized()) {

            if (bluetooth.isConnecting()) {

                loadFragment(new ConnectConnectingSubfragment());
                setStatus(R.color.status_connecting, R.string.connect_status_connecting);

            } else if (!bluetooth.isConnected()) {

                if (bluetooth.getHost().hasFailed()) {

                    ConnectFailedSubfragment fragment = new ConnectFailedSubfragment(bluetooth);
                    fragment.setReturnListener(this::update); // Updates the fragment when going back
                    loadFragment(fragment);

                } else loadFragment(new ConnectSelectSubfragment(bluetooth));

                setStatus(R.color.status_disconnected, R.string.connect_status_disconnected);
            } else {

                loadFragment(new ConnectConnectedSubfragment(bluetooth));
                setStatus(R.color.status_connected, R.string.connect_status_connected);

            }
        }
    }

    /**
     * Sets the status of the page.
     *
     * @param color color of the current status
     * @param text  name of the current status
     */
    private void setStatus(int color, int text) {
        ((GradientDrawable) status.getBackground()).setColor(getResources().getColor(color, null));
        statusText.setText(text);
    }

    /**
     * Sets the inner content to the requested fragment.
     *
     * @param fragment fragment to set to
     */
    private void loadFragment(Fragment fragment) {
        getChildFragmentManager().beginTransaction().setReorderingAllowed(true).replace(R.id.connect_container, fragment).commit();
    }
}
