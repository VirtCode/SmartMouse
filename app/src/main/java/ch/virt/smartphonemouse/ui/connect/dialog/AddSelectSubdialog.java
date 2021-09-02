package ch.virt.smartphonemouse.ui.connect.dialog;

import android.bluetooth.BluetoothClass;
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

import java.util.List;

import ch.virt.smartphonemouse.R;
import ch.virt.smartphonemouse.transmission.BluetoothDiscoverer;
import ch.virt.smartphonemouse.transmission.BluetoothHandler;

/**
 * This class holds a sub page for the add dialog that is used to display discovered devices and to select them.
 */
public class AddSelectSubdialog extends Fragment {

    private final BluetoothHandler bluetooth;

    private Button scanning;
    private RecyclerView list;
    private ListAdapter adapter;
    private ListAdapter.SelectListener selectListener;

    /**
     * Creates the sub dialog.
     *
     * @param bluetoothAdapter bluetooth handler to use
     */
    public AddSelectSubdialog(BluetoothHandler bluetoothAdapter) {
        super(R.layout.subdialog_add_select);
        this.bluetooth = bluetoothAdapter;
    }

    /**
     * Starts the discovery.
     */
    private void startDiscovery() {
        bluetooth.getDiscoverer().reset();
        bluetooth.getDiscoverer().startDiscovery();
    }

    /**
     * This method updates the scan button.
     *
     * @param status current scanning status
     */
    private void discoveryUpdated(boolean status) {

        if (status) {
            scanning.setEnabled(false);
            scanning.setText(R.string.dialog_add_select_scanning);
        } else {
            scanning.setEnabled(true);
            scanning.setText(R.string.dialog_add_select_rescan);
        }
    }

    /**
     * Sets the listener for when a device has been selected.
     * This method has to be called before the dialog is shown.
     *
     * @param selectListener select listener
     */
    public void setSelectListener(ListAdapter.SelectListener selectListener) {
        this.selectListener = selectListener;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        list = view.findViewById(R.id.add_select_list);
        scanning = view.findViewById(R.id.add_select_scanning);


        adapter = new ListAdapter(bluetooth.getDiscoverer().getDevices());
        adapter.setSelectListener(selectListener);

        list.setAdapter(adapter);
        list.setLayoutManager(new LinearLayoutManager(getContext()));

        scanning.setOnClickListener(v -> startDiscovery());

        bluetooth.getDiscoverer().setUpdateListener(devices -> adapter.notifyDataSetChanged());
        bluetooth.getDiscoverer().setScanListener(this::discoveryUpdated);

        startDiscovery();
    }


    /**
     * This list adapter handles the recycler view that shows all the discovered devices.
     */
    private static class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

        private List<BluetoothDiscoverer.DiscoveredDevice> devices;
        private SelectListener selectListener;

        /**
         * Creates the list adapter.
         *
         * @param devices devices to show
         */
        public ListAdapter(List<BluetoothDiscoverer.DiscoveredDevice> devices) {
            this.devices = devices;
        }

        /**
         * Sets the listener that is called when a device is selected.
         *
         * @param selectListener select listener
         */
        public void setSelectListener(SelectListener selectListener) {
            this.selectListener = selectListener;
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
            holder.populate(devices.get(position));
        }

        @Override
        public int getItemCount() {
            return devices.size();
        }

        /**
         * This class is the view holder for the recycler view that shows the discovered devices.
         */
        public class ViewHolder extends RecyclerView.ViewHolder {

            private final TextView label;
            private final ImageView icon;

            private BluetoothDiscoverer.DiscoveredDevice device;

            /**
             * Creates the view holder.
             *
             * @param view view that the holder holds
             */
            public ViewHolder(View view) {
                super(view);

                view.setOnClickListener(v -> selectListener.called(device));

                label = view.findViewById(R.id.add_select_list_item_text);
                icon = view.findViewById(R.id.add_select_list_item_icon);
            }

            /**
             * Populates the view with the data about one device.
             *
             * @param device device to populate with
             */
            public void populate(BluetoothDiscoverer.DiscoveredDevice device) {
                this.device = device;

                label.setText(device.getName());

                // Only covers the three major types
                if (device.getMajorClass() == BluetoothClass.Device.Major.PHONE)
                    icon.setImageResource(R.drawable.device_add_smartphone);
                else if (device.getMajorClass() == BluetoothClass.Device.Major.COMPUTER)
                    icon.setImageResource(R.drawable.device_add_computer);
                else icon.setImageResource(R.drawable.device_add_other);
            }
        }

        /**
         * This interface is a basic listener for when a list entry is selected.
         */
        public interface SelectListener {

            /**
             * Called when a list entry is selected.
             *
             * @param device device that is selected
             */
            void called(BluetoothDiscoverer.DiscoveredDevice device);
        }
    }
}
