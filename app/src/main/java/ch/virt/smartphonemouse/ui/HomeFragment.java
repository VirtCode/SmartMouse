package ch.virt.smartphonemouse.ui;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import ch.virt.smartphonemouse.MainActivity;
import ch.virt.smartphonemouse.R;
import ch.virt.smartphonemouse.transmission.BluetoothHandler;
import ch.virt.smartphonemouse.transmission.DebugTransmitter;
import ch.virt.smartphonemouse.ui.home.HomeConnectedSubfragment;
import ch.virt.smartphonemouse.ui.home.HomeDisabledSubfragment;
import ch.virt.smartphonemouse.ui.home.HomeDisconnectedSubfragment;
import ch.virt.smartphonemouse.ui.home.HomeUnsupportedSubfragment;

/**
 * This fragment contains the home page of the app, which shows basic information.
 */
public class HomeFragment extends Fragment {

    private BluetoothHandler bluetooth;
    private DebugTransmitter debug;

    private ImageView status;
    private TextView statusText;
    private TextView debugStatus;

    private Button button;

    /**
     * Creates the fragment.
     *
     * @param bluetooth bluetooth handler to use
     */
    public HomeFragment(BluetoothHandler bluetooth, DebugTransmitter debug) {
        super(R.layout.fragment_home);

        this.bluetooth = bluetooth;
        this.debug = debug;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        status = view.findViewById(R.id.home_status);
        statusText = view.findViewById(R.id.home_status_text);
        button = view.findViewById(R.id.home_button);

        debugStatus = view.findViewById(R.id.home_debug_status);
        debugStatus.setOnClickListener(v -> update());

        update();
    }

    /**
     * Updates the content of the page according to the current status.
     */
    public void update() {
        if (bluetooth.isInitialized())

            if (!bluetooth.isEnabled())
                setStatus(R.color.status_init, R.string.home_status_disabled, R.string.home_button_disabled, v -> bluetooth.enableBluetooth(), new HomeDisabledSubfragment(bluetooth));

            else if (!bluetooth.isSupported())
                setStatus(R.color.status_unsupported, R.string.home_status_unsupported, R.string.home_button_unsupported, v -> getActivity().finish(), new HomeUnsupportedSubfragment());

            else if (bluetooth.isConnected())
                setStatus(R.color.status_connected, R.string.home_status_connected, R.string.home_button_connected, v -> ((MainActivity) getActivity()).navigate(R.id.drawer_mouse), new HomeConnectedSubfragment(bluetooth));

            else
                setStatus(R.color.status_disconnected, R.string.home_status_disconnected, R.string.home_button_disconnected, v -> ((MainActivity) getActivity()).navigate(R.id.drawer_connect), new HomeDisconnectedSubfragment());

        if (debug.isEnabled()) {
            debugStatus.setVisibility(View.VISIBLE);

            if (debug.isConnected()) debugStatus.setText(String.format(getContext().getText(R.string.home_debug_connected).toString(), debug.getServerString()));
            else debugStatus.setText(String.format(getContext().getText(R.string.home_debug_disconnected).toString(), debug.getServerString()));
        }
    }

    /**
     * Sets the status of the page.
     *
     * @param statusColor    color of the status
     * @param statusText     name of the status
     * @param buttonText     text of the primary button of this status
     * @param buttonListener action of the primary button of this status
     * @param fragment       fragment to be displayed
     */
    private void setStatus(int statusColor, int statusText, int buttonText, View.OnClickListener buttonListener, Fragment fragment) {
        ((GradientDrawable) status.getBackground()).setColor(getResources().getColor(statusColor, null));
        this.statusText.setText(statusText);

        button.setEnabled(true);
        button.setText(buttonText);

        button.setOnClickListener(buttonListener);

        getChildFragmentManager().beginTransaction().setReorderingAllowed(true).replace(R.id.home_container, fragment).commit();
    }
}
