package ch.virt.smartphonemouse.transmission;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * This class handles the discovery of new bluetooth devices to connect to.
 */
public class BluetoothDiscoverer extends BroadcastReceiver {

    private BluetoothAdapter adapter;

    private boolean isScanning;
    private List<DiscoveredDevice> devices;

    private UpdateListener updateListener;
    private ScanListener scanListener;

    /**
     * Creates a bluetooth discoverer.
     *
     * @param context context to get events from
     * @param adapter bluetooth adapter to use
     */
    public BluetoothDiscoverer(Context context, BluetoothAdapter adapter) {
        this.adapter = adapter;

        devices = new ArrayList<>();

        IntentFilter intents = new IntentFilter();
        intents.addAction(BluetoothDevice.ACTION_FOUND);
        intents.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        intents.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

        context.registerReceiver(this, intents);
    }

    /**
     * Starts the discovery for new devices.
     */
    public void startDiscovery() {
        adapter.startDiscovery();
    }

    /**
     * Stops the discovery
     */
    public void stopDiscovery() {
        adapter.cancelDiscovery();
    }

    /**
     * Removes the already found devices.
     */
    public void reset() {
        devices.clear();
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();
        if (BluetoothDevice.ACTION_FOUND.equals(action)) {

            DiscoveredDevice discovered = new DiscoveredDevice(intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE));
            if (!devices.contains(discovered)) { // Ignore duplicates
                devices.add(discovered);

                if (updateListener != null) updateListener.update(devices);
            }
        } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action) || BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
            isScanning = BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action);
            if (scanListener != null) scanListener.changed(isScanning);
        }
    }

    /**
     * Returns the already found devices.
     *
     * @return list of already found devices
     */
    public List<DiscoveredDevice> getDevices() {
        return devices;
    }

    /**
     * Returns whether the listener is still scanning for devices.
     *
     * @return is scanning
     */
    public boolean isScanning() {
        return isScanning;
    }

    /**
     * Sets the update listener.
     * This listener gets called when a new device is discovered.
     *
     * @param updateListener update listener
     */
    public void setUpdateListener(UpdateListener updateListener) {
        this.updateListener = updateListener;
    }

    /**
     * Sets the scan listener.
     * This listener is called when the scan status changes.
     *
     * @param scanListener scan listener
     */
    public void setScanListener(ScanListener scanListener) {
        this.scanListener = scanListener;
    }

    /**
     * This subclass contains the basic information for a discovered bluetooth device.
     */
    public static class DiscoveredDevice {
        private String name;
        private String address;
        private int majorClass;

        /**
         * Creates a discovered device from a bluetooth device.
         *
         * @param device bluetooth device that is the discovered device
         */
        public DiscoveredDevice(BluetoothDevice device) {
            this((device.getName() == null || device.getName().equals("")) ? "Unknown" : device.getName(), device.getAddress(), device.getBluetoothClass().getMajorDeviceClass());
        }

        /**
         * Creates a discovered device from a set of information.
         *
         * @param name       name of the device
         * @param address    bluetooth mac address of the device
         * @param majorClass major device class of the device
         */
        public DiscoveredDevice(String name, String address, int majorClass) {
            this.name = name;
            this.address = address;
            this.majorClass = majorClass;
        }

        /**
         * Returns the name of the device.
         *
         * @return name
         */
        public String getName() {
            return name;
        }

        /**
         * Returns the bluetooth mac address of the device.
         *
         * @return bluetooth mac address
         */
        public String getAddress() {
            return address;
        }

        /**
         * Returns the major device class of the device.
         *
         * @return major device class
         */
        public int getMajorClass() {
            return majorClass;
        }

        @Override
        public boolean equals(@Nullable Object obj) {
            return obj instanceof DiscoveredDevice && address.equals(((DiscoveredDevice) obj).address);
        }
    }

    /**
     * This interface is a basic listener for updates on the discovered device list.
     */
    public interface UpdateListener {

        /**
         * Device list got updated.
         *
         * @param devices currently discovered devices
         */
        void update(List<DiscoveredDevice> devices);
    }

    /**
     * This interface is a basic listener when the scanning starts / stops
     */
    public interface ScanListener {

        /**
         * Scan status got changed.
         *
         * @param scanning whether it is currently scanning
         */
        void changed(boolean scanning);
    }
}
