package ch.virt.smartphonemouse.ui.connect;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import ch.virt.smartphonemouse.R;
import ch.virt.smartphonemouse.transmission.BluetoothHandler;
import ch.virt.smartphonemouse.transmission.HostDevice;
import ch.virt.smartphonemouse.ui.connect.dialog.AddDialog;
import ch.virt.smartphonemouse.ui.connect.dialog.InfoDialog;

/**
 * This class is a sub fragment for the connect page.
 * On this fragment, the user can select or add a device, which he then can connect to.
 */
public class ConnectSelectSubfragment extends Fragment {

    private final BluetoothHandler bluetooth;

    private ListAdapter adapter;
    private RecyclerView list;
    private Button add;

    /**
     * Creates the sub fragment.
     * @param bluetooth bluetooth handler to handle bluetooth
     */
    public ConnectSelectSubfragment(BluetoothHandler bluetooth) {
        super(R.layout.subfragment_connect_select);
        this.bluetooth = bluetooth;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        list = view.findViewById(R.id.connect_select_items);
        add = view.findViewById(R.id.connect_select_add);


        add.setOnClickListener(v -> add());

        adapter = new ListAdapter(bluetooth.getDevices().getDevices());
        adapter.setConnectListener(this::connect);
        adapter.setInfoListener(this::info);

        list.setAdapter(adapter);
        list.setLayoutManager(new LinearLayoutManager(getContext()));

    }

    /**
     * Opens the information dialog for the specified device.
     * @param device device
     */
    public void info(HostDevice device){
        InfoDialog dialog = new InfoDialog(bluetooth, device);
        dialog.setOnDismissListener((d) -> adapter.notifyDataSetChanged());
        dialog.show(this.getParentFragmentManager(), null);
    }

    /**
     * Connects to the known device specified
     * @param device device to connect to
     */
    public void connect(HostDevice device){
        bluetooth.getHost().connect(device);
    }

    /**
     * Starts the dialog to add a device.
     */
    private void add(){
        AddDialog dialog = new AddDialog(bluetooth);
        dialog.setOnDismissListener((d) -> adapter.notifyDataSetChanged());
        dialog.show(this.getParentFragmentManager(), null);
    }

    /**
     * This class is the list adapter for the recycler view which shows the known devices.
     */
    private static class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

        private final List<HostDevice> devices;
        private ListResultListener connectListener;
        private ListResultListener infoListener;

        /**
         * Creates the list adapter.
         * @param devices devices to show
         */
        public ListAdapter(List<HostDevice> devices) {
            this.devices = devices;
        }

        /**
         * Sets the listener that gets called when the user clicks on an entry.
         * @param connectListener connect listener
         */
        public void setConnectListener(ListResultListener connectListener) {
            this.connectListener = connectListener;
        }

        /**
         * Sets the listener that gets called when the user clicks on the info sign on an entry.
         * @param infoListener info listener
         */
        public void setInfoListener(ListResultListener infoListener) {
            this.infoListener = infoListener;
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

        /**
         * This is the view holder for that recyclerview.
         */
        public class ViewHolder extends RecyclerView.ViewHolder {

            private final TextView name;
            private final TextView last;
            private final ImageView settings;

            private HostDevice device;

            /**
             * Creates the view holder.
             * @param view view to hold
             */
            public ViewHolder(View view) {
                super(view);

                name = view.findViewById(R.id.connect_select_list_item_name);
                last = view.findViewById(R.id.connect_select_list_item_last);
                settings = view.findViewById(R.id.connect_select_list_item_settings);

                view.setOnClickListener(v -> connectListener.result(device));
                settings.setOnClickListener(v -> infoListener.result(device));
            }

            /**
             * Sets the device the holder does represent.
             * @param device device
             */
            public void setDevice(HostDevice device){
                this.device = device;

                name.setText(device.getName());
                last.setText(itemView.getResources().getString(R.string.connect_select_item_last, device.getLastConnected() == -1 ? itemView.getResources().getString(R.string.connect_select_item_never) : new SimpleDateFormat(itemView.getResources().getString(R.string.connect_select_item_last_format)).format(new Date(device.getLastConnected()))));
            }
        }

        /**
         * This interface is a basic listener which is used for events regarding the recyclerview in this activity.
         */
        public interface ListResultListener {

            /**
             * Gets called when an entry is clicked.
             * @param device device of that entry
             */
            void result(HostDevice device);
        }

    }
}
