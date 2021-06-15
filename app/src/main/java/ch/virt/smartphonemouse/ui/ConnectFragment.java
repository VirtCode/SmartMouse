package ch.virt.smartphonemouse.ui;

import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import ch.virt.smartphonemouse.R;
import ch.virt.smartphonemouse.helper.MainContext;
import ch.virt.smartphonemouse.transmission.BluetoothHandler;
import ch.virt.smartphonemouse.ui.connect.ConnectConnectedSubfragment;
import ch.virt.smartphonemouse.ui.connect.ConnectConnectingSubfragment;
import ch.virt.smartphonemouse.ui.connect.ConnectSelectSubfragment;
import ch.virt.smartphonemouse.ui.home.HomeConnectedSubfragment;

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

            if (bluetooth.isConnected()) setStatus(R.color.status_connected, R.string.connect_status_connected);
            else setStatus(R.color.status_disconnected, R.string.connect_status_disconnected);

            if (bluetooth.isConnecting()) {
                connecting();
                setStatus(R.color.status_connecting, R.string.connect_status_connecting);
            }
            else if (!bluetooth.isConnected()){
                select();
                setStatus(R.color.status_disconnected, R.string.connect_status_disconnected);
            }
            else{
                connected();
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

    public void select(){
        getChildFragmentManager().beginTransaction().setReorderingAllowed(true).replace(R.id.connect_container, new ConnectSelectSubfragment(main, bluetooth)).commit();
    }

    public void connected(){
        getChildFragmentManager().beginTransaction().setReorderingAllowed(true).replace(R.id.connect_container, new ConnectConnectedSubfragment(main, bluetooth)).commit();
    }

    public void connecting(){
        getChildFragmentManager().beginTransaction().setReorderingAllowed(true).replace(R.id.connect_container, new ConnectConnectingSubfragment(main, bluetooth)).commit();

    }
}
