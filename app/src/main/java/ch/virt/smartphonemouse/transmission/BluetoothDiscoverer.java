package ch.virt.smartphonemouse.transmission;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import java.util.ArrayList;
import java.util.List;

import ch.virt.smartphonemouse.helper.Listener;
import ch.virt.smartphonemouse.helper.MainContext;

public class BluetoothDiscoverer extends BroadcastReceiver {

    MainContext context;
    BluetoothAdapter adapter;

    boolean isScanning;
    List<DiscoveredDevice> devices;

    Listener updateListener;
    Listener scanListener;

    public BluetoothDiscoverer(MainContext context, BluetoothAdapter adapter) {
        this.context = context;
        this.adapter = adapter;

        devices = new ArrayList<>();

        IntentFilter intents = new IntentFilter();
        intents.addAction(BluetoothDevice.ACTION_FOUND);
        intents.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        intents.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

        context.registerReceiver(this, intents);
    }

    public void startDiscovery() {
        System.out.println(ActivityCompat.checkSelfPermission(context.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED);
        System.out.println(ActivityCompat.checkSelfPermission(context.getContext(), Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED);
        System.out.println(ActivityCompat.checkSelfPermission(context.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED);
        System.out.println(ActivityCompat.checkSelfPermission(context.getContext(), Manifest.permission.BLUETOOTH_ADMIN) == PackageManager.PERMISSION_GRANTED);
        System.out.println(adapter.getState() == BluetoothAdapter.STATE_ON);
        System.out.println("Started: " + adapter.startDiscovery());

    }

    public void stopDiscovery() {
        adapter.cancelDiscovery();
    }

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

                if (updateListener != null) updateListener.called();
            }
        }else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action) || BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)){
            isScanning = BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action);
            if (scanListener != null) scanListener.called();
        }
    }

    public List<DiscoveredDevice> getDevices() {
        return devices;
    }

    public boolean isScanning() {
        return isScanning;
    }

    public static class DiscoveredDevice {
        private String name;
        private String address;
        private int majorClass;

        public DiscoveredDevice(BluetoothDevice device){
            this((device.getName() == null || device.getName().equals("")) ? "Unknown" : device.getName(), device.getAddress(), device.getBluetoothClass().getMajorDeviceClass());
        }

        public DiscoveredDevice(String name, String address, int majorClass) {
            this.name = name;
            this.address = address;
            this.majorClass = majorClass;
        }

        public String getName() {
            return name;
        }

        public String getAddress() {
            return address;
        }

        public int getMajorClass() {
            return majorClass;
        }

        @Override
        public boolean equals(@Nullable Object obj) {
            return obj instanceof DiscoveredDevice && address.equals(((DiscoveredDevice) obj).address);
        }
    }

    public void setUpdateListener(Listener updateListener) {
        this.updateListener = updateListener;
    }

    public void setScanListener(Listener scanListener) {
        this.scanListener = scanListener;
    }
}
