package ch.virt.smartphonemouse.ui.connect;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import ch.virt.smartphonemouse.R;
import ch.virt.smartphonemouse.helper.Listener;
import ch.virt.smartphonemouse.helper.MainContext;
import ch.virt.smartphonemouse.transmission.BluetoothHandler;
import ch.virt.smartphonemouse.transmission.HostDevice;
import ch.virt.smartphonemouse.ui.CustomFragment;
import ch.virt.smartphonemouse.ui.connect.dialog.AddDialog;
import ch.virt.smartphonemouse.ui.connect.dialog.InfoDialog;

public class ConnectSelectSubfragment extends CustomFragment {

    private BluetoothHandler bluetooth;

    ListAdapter adapter;
    RecyclerView list;
    Button add;

    public ConnectSelectSubfragment(MainContext context, BluetoothHandler bluetooth) {
        super(R.layout.subfragment_connect_select, context);
        this.bluetooth = bluetooth;
    }

    @Override
    public void render() {
        adapter = new ListAdapter(bluetooth.getDevices().getDevices(), this::connect, this::info);

        list.setAdapter(adapter);
        list.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    public void info(){
        InfoDialog dialog = new InfoDialog(bluetooth, adapter.getSelected(), () -> adapter.notifyDataSetChanged());
        dialog.show(this.getParentFragmentManager(), null);
    }

    public void connect(){
        bluetooth.getHost().connect(bluetooth.fromHostDevice(adapter.getSelected()));
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
        AddDialog dialog = new AddDialog(bluetooth, main, () -> adapter.notifyDataSetChanged());
        dialog.show(this.getParentFragmentManager(), null);
    }

    private static class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

        private List<HostDevice> devices;
        private int selectedDevice;
        private Listener selectListener;
        private Listener settingsListener;

        public ListAdapter(List<HostDevice> devices, Listener listener, Listener settingsListener) {
            selectListener = listener;
            this.devices = devices;
            this.settingsListener = settingsListener;
        }

        public HostDevice getSelected(){
            return devices.get(selectedDevice);
        }

        @NonNull
        @Override
        public ListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_connect_select, parent, false);

            return new ListAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ListAdapter.ViewHolder holder, int position) {
            holder.setDevice(devices.get(position));
        }

        @Override
        public int getItemCount() {
            return devices.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            private final TextView name;
            private final TextView last;
            private final ImageView settings;

            public ViewHolder(View view) {
                super(view);

                view.setOnClickListener(v -> {
                    selectedDevice = getLayoutPosition();
                    selectListener.called();
                });

                name = view.findViewById(R.id.connect_select_list_item_name);
                last = view.findViewById(R.id.connect_select_list_item_last);
                settings = view.findViewById(R.id.connect_select_list_item_settings);

                settings.setOnClickListener(v -> {
                    selectedDevice = getLayoutPosition();
                    settingsListener.called();
                });
            }

            public void setDevice(HostDevice device){
                name.setText(device.getName());
                last.setText(itemView.getResources().getString(R.string.connect_select_item_last, device.getLastConnected() == -1 ? itemView.getResources().getString(R.string.connect_select_item_never) : new SimpleDateFormat(itemView.getResources().getString(R.string.connect_select_item_last_format)).format(new Date(device.getLastConnected()))));
            }
        }


    }
}
