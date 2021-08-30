package ch.virt.smartphonemouse.ui;

import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import ch.virt.smartphonemouse.MainActivity;
import ch.virt.smartphonemouse.R;
import ch.virt.smartphonemouse.helper.MainContext;
import ch.virt.smartphonemouse.transmission.BluetoothHandler;
import ch.virt.smartphonemouse.ui.home.HomeConnectedSubfragment;
import ch.virt.smartphonemouse.ui.home.HomeDisabledSubfragment;
import ch.virt.smartphonemouse.ui.home.HomeDisconnectedSubfragment;
import ch.virt.smartphonemouse.ui.home.HomeUnsupportedSubfragment;

public class HomeFragment extends CustomFragment {

    private BluetoothHandler bluetooth;

    private ImageView status;
    private TextView statusText;

    private Button button;

    public HomeFragment(BluetoothHandler bluetooth, MainContext mainContext) {
        super(R.layout.fragment_home, mainContext);

        this.bluetooth = bluetooth;
    }

    @Override
    public void render() {
        if (bluetooth.isInitialized())

            if (!bluetooth.isEnabled())
                setStatus(R.color.status_init, R.string.home_status_disabled, R.string.home_button_disabled, v -> bluetooth.enableBluetooth(), new HomeDisabledSubfragment(bluetooth));

            else if (!bluetooth.isSupported())
                setStatus(R.color.status_unsupported, R.string.home_status_unsupported, R.string.home_button_unsupported, v -> getActivity().finish(), new HomeUnsupportedSubfragment());

            else if (bluetooth.isConnected())
                setStatus(R.color.status_connected, R.string.home_status_connected, R.string.home_button_connected, v -> ((MainActivity) getActivity()).navigate(R.id.drawer_mouse), new HomeConnectedSubfragment(bluetooth, main));

            else
                setStatus(R.color.status_disconnected, R.string.home_status_disconnected, R.string.home_button_disconnected, v -> ((MainActivity) getActivity()).navigate(R.id.drawer_connect), new HomeDisconnectedSubfragment());
    }

    protected void loadComponents(View view){
        status = view.findViewById(R.id.home_status);
        statusText = view.findViewById(R.id.home_status_text);
        button = view.findViewById(R.id.home_button);
    }

    private void setStatus(int statusColor, int statusText, int buttonText, View.OnClickListener buttonListener, Fragment fragment){
        ((GradientDrawable) status.getBackground()).setColor(getResources().getColor(statusColor, null));
        this.statusText.setText(statusText);

        button.setEnabled(true);
        button.setText(buttonText);

        button.setOnClickListener(buttonListener);

        getChildFragmentManager().beginTransaction().setReorderingAllowed(true).replace(R.id.home_container, fragment).commit();
    }
}
