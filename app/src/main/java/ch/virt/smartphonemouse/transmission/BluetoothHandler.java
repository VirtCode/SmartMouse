package ch.virt.smartphonemouse.transmission;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHidDevice;
import android.bluetooth.BluetoothProfile;
import android.content.Intent;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;

import com.google.android.material.snackbar.Snackbar;

import ch.virt.smartphonemouse.R;
import ch.virt.smartphonemouse.helper.MainContext;
import ch.virt.smartphonemouse.transmission.hid.HidDevice;

public class BluetoothHandler implements BluetoothProfile.ServiceListener {
    private static final String TAG = "BluetoothHandler";

    private BluetoothAdapter adapter;
    private BluetoothHidDevice service;
    private BluetoothDiscoverer discoverer;

    private DeviceStorage devices;

    private final MainContext context;

    private HidDevice device;

    private boolean initialized = false;
    private boolean enabled = false;
    private boolean supported = false;
    private boolean opened = false;

    private ActivityResultLauncher<Intent> enableBluetoothLauncher;

    public BluetoothHandler(MainContext context) {
        this.context = context;
        discoverer = new BluetoothDiscoverer(context, adapter);
        devices = new DeviceStorage(context);

        enableBluetoothLauncher = context.registerActivityForResult(result -> reInit());
    }

    public void reInit(){
        initialized = false;
        init();
    }

    public void enableBluetooth(){

        if (!adapter.isEnabled()) {
            Log.i(TAG, "Enabling Bluetooth");

            enableBluetoothLauncher.launch(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE));
        }
    }

    private void init() {
        adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter == null) {
            Log.i(TAG, "Bluetooth is not supported");
            initialized = true;
            supported = false;

            context.refresh();
            return;
        }

        if (!adapter.isEnabled()) {
            Log.i(TAG, "Bluetooth is turned off");

            enabled = false;
            initialized = true;

            context.refresh();
            return;

        }else enabled = true;

        open();
    }

    private void open(){
        if(!adapter.getProfileProxy(context.getContext(), this, BluetoothProfile.HID_DEVICE)){
            Log.i(TAG, "Bluetooth HID profile is not supported");
            initialized = true;
            supported = false;

            context.refresh();
            return;
        }

        supported = true;
    }

    private void start(){
        Log.i(TAG, "Opened HID Profile successfully");
        initialized = true;

        discoverer = new BluetoothDiscoverer(context, adapter);
        device = new HidDevice(service, context);

        Log.d(TAG, "Registering with a HID Device!");
        device.register();

        context.refresh();
    }

    @Override
    public void onServiceConnected(int profile, BluetoothProfile proxy) {
        if (profile == BluetoothProfile.HID_DEVICE){
            this.service = (BluetoothHidDevice) proxy;

            opened = true;
            start();
        }
    }

    @Override
    public void onServiceDisconnected(int profile) {
        if (profile == BluetoothProfile.HID_DEVICE){
            opened = false;

            Log.i(TAG, "Reconnecting to Service");
            context.snack(context.getResources().getString(R.string.snack_bluetooth_closed), Snackbar.LENGTH_SHORT);

            open();
        }
    }

    public boolean isSupported() {
        return supported;
    }
    public boolean isConnected() {
        if (!initialized) return false;
        return getHost().isConnected();
    }
    public boolean isConnecting(){
        if (!initialized) return false;
        return getHost().isConnecting();
    }
    public boolean isEnabled() {
        return enabled;
    }
    public boolean isInitialized() {
        return initialized;
    }

    public BluetoothDiscoverer getDiscoverer() {
        return discoverer;
    }

    public DeviceStorage getDevices() {
        return devices;
    }

    public HidDevice getHost() {
        return device;
    }

    public BluetoothDevice fromHostDevice(HostDevice device){
        return adapter.getRemoteDevice(device.getAddress());
    }

    public boolean isBonded(String address){
        boolean result = false;

        for (BluetoothDevice device : adapter.getBondedDevices()){
            if (device.getAddress().equals(address)) result = true;
        }

        return result;
    }
}
