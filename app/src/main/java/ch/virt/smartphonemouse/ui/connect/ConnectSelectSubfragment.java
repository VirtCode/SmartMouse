package ch.virt.smartphonemouse.ui.connect;

import android.view.View;

import ch.virt.smartphonemouse.R;
import ch.virt.smartphonemouse.helper.MainContext;
import ch.virt.smartphonemouse.transmission.BluetoothHandler;
import ch.virt.smartphonemouse.ui.CustomFragment;

public class ConnectSelectSubfragment extends CustomFragment {

    private BluetoothHandler bluetooth;

    public ConnectSelectSubfragment(MainContext context, BluetoothHandler bluetooth) {
        super(R.layout.subfragment_connect_select, context);
        this.bluetooth = bluetooth;
    }

    @Override
    public void render() {

    }

    @Override
    protected void loadComponents(View view) {

    }
}
