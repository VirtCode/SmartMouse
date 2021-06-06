package ch.virt.smartphonemouse.ui.connect.dialog;

import android.view.View;
import android.widget.TextView;

import ch.virt.smartphonemouse.R;
import ch.virt.smartphonemouse.helper.MainContext;
import ch.virt.smartphonemouse.transmission.BluetoothDiscoverer;
import ch.virt.smartphonemouse.transmission.BluetoothHandler;
import ch.virt.smartphonemouse.ui.CustomFragment;

public class AddBondedSubdialog extends CustomFragment {

    private BluetoothHandler handler;
    private TextView error;

    private BluetoothDiscoverer.DiscoveredDevice target;


    public AddBondedSubdialog(MainContext context, BluetoothHandler handler, BluetoothDiscoverer.DiscoveredDevice target) {
        super(R.layout.subdialog_add_bonded, context);
        this.handler = handler;
        this.target = target;
    }

    public boolean check(){
        if (handler.isBonded(target.getAddress())){
            error.setVisibility(View.VISIBLE);
            return false;
        }else {
            error.setVisibility(View.GONE);
            return true;
        }
    }

    @Override
    public void render() {

    }

    @Override
    protected void loadComponents(View view) {
        error = view.findViewById(R.id.add_bonded_error);
    }
}
