package ch.virt.smartphonemouse.ui.connect.dialog;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ch.virt.smartphonemouse.R;
import ch.virt.smartphonemouse.helper.MainContext;
import ch.virt.smartphonemouse.transmission.BluetoothDiscoverer;
import ch.virt.smartphonemouse.transmission.BluetoothHandler;
import ch.virt.smartphonemouse.ui.CustomFragment;

public class AddSelectSubdialog extends CustomFragment {

    BluetoothHandler bluetooth;
    RecyclerView list;
    Button scanning;

    ListAdapter adapter;

    public AddSelectSubdialog(MainContext context, BluetoothHandler bluetoothAdapter) {
        super(R.layout.subdialog_add_select, context);
        this.bluetooth = bluetoothAdapter;
    }

    @Override
    public void render() {
        adapter = new ListAdapter(bluetooth.getDiscoverer().getDevices());
        list.setAdapter(adapter);
        list.setLayoutManager(new LinearLayoutManager(getContext()));

        scanning.setOnClickListener(v -> startDiscovery());

        bluetooth.getDiscoverer().setUpdateListener(adapter::notifyDataSetChanged);
        bluetooth.getDiscoverer().setScanListener(this::discoveryUpdated);

        bluetooth.getDiscoverer().startDiscovery();
        bluetooth.getDiscoverer().reset();
    }

    public void startDiscovery(){
        bluetooth.getDiscoverer().reset();
        bluetooth.getDiscoverer().startDiscovery();
    }

    public void discoveryUpdated(){
        if (bluetooth.getDiscoverer().isScanning()) {
            scanning.setEnabled(false);
            scanning.setText(R.string.dialog_add_select_scanning);
        }else {
            scanning.setEnabled(true);
            scanning.setText(R.string.dialog_add_select_rescan);
        }
    }

    @Override
    protected void loadComponents(View view) {
        list = view.findViewById(R.id.add_select_list);
        scanning = view.findViewById(R.id.add_select_scanning);
    }

    private static class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

        List<BluetoothDiscoverer.DiscoveredDevice> devices;

        public ListAdapter(List<BluetoothDiscoverer.DiscoveredDevice> devices) {
            this.devices = devices;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_add_select, parent, false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull AddSelectSubdialog.ListAdapter.ViewHolder holder, int position) {
            holder.getLabel().setText(devices.get(position).getName());

            // Only covers the three major types
            if (devices.get(position).getMajorClass() == BluetoothClass.Device.Major.PHONE) holder.getIcon().setImageResource(R.drawable.device_add_smartphone);
            else if (devices.get(position).getMajorClass() == BluetoothClass.Device.Major.COMPUTER) holder.getIcon().setImageResource(R.drawable.device_add_computer);
            else holder.getIcon().setImageResource(R.drawable.device_add_other);
        }

        @Override
        public int getItemCount() {
            return devices.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            private final TextView label;
            private final ImageView icon;

            public ViewHolder(View view) {
                super(view);

                label = view.findViewById(R.id.add_select_list_item_text);
                icon = view.findViewById(R.id.add_select_list_item_icon);
            }

            public TextView getLabel() {
                return label;
            }

            public ImageView getIcon() {
                return icon;
            }
        }


    }
}
