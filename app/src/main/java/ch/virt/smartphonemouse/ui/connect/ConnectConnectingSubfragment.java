package ch.virt.smartphonemouse.ui.connect;

import android.view.View;
import android.widget.TextView;

import ch.virt.smartphonemouse.R;
import ch.virt.smartphonemouse.helper.MainContext;
import ch.virt.smartphonemouse.transmission.BluetoothHandler;
import ch.virt.smartphonemouse.ui.CustomFragment;

public class ConnectConnectingSubfragment extends CustomFragment {

    BluetoothHandler bluetooth;

    public ConnectConnectingSubfragment(MainContext context, BluetoothHandler bluetooth) {
        super(R.layout.subfragment_connect_connecting, context);

        this.bluetooth = bluetooth;
    }

    @Override
    public void render() {
    }

    @Override
    protected void loadComponents(View view) {
    }
}
