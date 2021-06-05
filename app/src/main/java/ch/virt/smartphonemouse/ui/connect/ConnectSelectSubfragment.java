package ch.virt.smartphonemouse.ui.connect;

import android.view.View;
import android.widget.Button;

import androidx.recyclerview.widget.RecyclerView;

import ch.virt.smartphonemouse.R;
import ch.virt.smartphonemouse.helper.MainContext;
import ch.virt.smartphonemouse.transmission.BluetoothHandler;
import ch.virt.smartphonemouse.ui.CustomFragment;
import ch.virt.smartphonemouse.ui.connect.dialog.AddDialog;

public class ConnectSelectSubfragment extends CustomFragment {

    private BluetoothHandler bluetooth;

    RecyclerView list;
    Button add;

    public ConnectSelectSubfragment(MainContext context, BluetoothHandler bluetooth) {
        super(R.layout.subfragment_connect_select, context);
        this.bluetooth = bluetooth;
    }

    @Override
    public void render() {
    }

    @Override
    protected void loadComponents(View view) {
        list = view.findViewById(R.id.connect_select_items);
        add = view.findViewById(R.id.connect_select_add);
    }

    @Override
    protected void initComponents() {
        add.setOnClickListener(view -> add());
    }

    private void add(){
        AddDialog dialog = new AddDialog(bluetooth, main);
        dialog.show(this.getParentFragmentManager(), null);
    }
}
