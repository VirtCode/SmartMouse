package ch.virt.smartphonemouse.ui;

import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import ch.virt.smartphonemouse.R;
import ch.virt.smartphonemouse.helper.MainContext;
import ch.virt.smartphonemouse.transmission.BluetoothHandler;
import ch.virt.smartphonemouse.ui.connect.ConnectConnectedSubfragment;
import ch.virt.smartphonemouse.ui.connect.ConnectConnectingSubfragment;
import ch.virt.smartphonemouse.ui.connect.ConnectFailedSubfragment;
import ch.virt.smartphonemouse.ui.connect.ConnectSelectSubfragment;

public class ConnectFragment extends CustomFragment {

    private ImageView status;
    private TextView statusText;

    private BluetoothHandler bluetooth;

    public ConnectFragment(MainContext main, BluetoothHandler bluetooth){
        super(R.layout.fragment_connect, main);
        this.bluetooth = bluetooth;
    }

    @Override
    public void render() {
        if (bluetooth.isInitialized()){

            if (bluetooth.isConnecting()) {
                loadFragment(new ConnectConnectingSubfragment());
                setStatus(R.color.status_connecting, R.string.connect_status_connecting);
            }
            else if (!bluetooth.isConnected()){
                if (bluetooth.getHost().hasFailed()){
                    ConnectFailedSubfragment fragment = new ConnectFailedSubfragment(bluetooth);
                    fragment.setReturnListener(this::render);
                    loadFragment(fragment);
                }
                else loadFragment(new ConnectSelectSubfragment(bluetooth));

                setStatus(R.color.status_disconnected, R.string.connect_status_disconnected);
            }
            else{
                loadFragment(new ConnectConnectedSubfragment(bluetooth));
                setStatus(R.color.status_connected, R.string.connect_status_connected);
            }
        }
    }

    @Override
    protected void loadComponents(View view) {
        status = view.findViewById(R.id.connect_status);
        statusText = view.findViewById(R.id.connect_status_text);
    }

    private void setStatus(int color, int text){
        ((GradientDrawable) status.getBackground()).setColor(getResources().getColor(color, null));
        statusText.setText(text);
    }

    private void loadFragment(Fragment fragment){
        getChildFragmentManager().beginTransaction().setReorderingAllowed(true).replace(R.id.connect_container, fragment).commit();
    }
}
