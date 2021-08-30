package ch.virt.smartphonemouse.transmission;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHidDevice;
import android.bluetooth.BluetoothProfile;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.ComponentActivity;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import ch.virt.smartphonemouse.MainActivity;
import ch.virt.smartphonemouse.transmission.hid.HidDevice;

public class BluetoothHandler implements BluetoothProfile.ServiceListener {
    private static final String TAG = "BluetoothHandler";

    private BluetoothAdapter adapter;
    private BluetoothHidDevice service;
    private BluetoothDiscoverer discoverer;

    private DeviceStorage devices;

    private final ComponentActivity main;

    private HidDevice device;

    private boolean initialized = false;
    private boolean enabled = false;
    private boolean supported = false;
    private boolean opened = false;

    private ActivityResultLauncher<Intent> enableBluetoothLauncher;

    public BluetoothHandler(ComponentActivity context) {
        this.main = context;
        discoverer = new BluetoothDiscoverer(main, adapter);
        devices = new DeviceStorage(main);

        enableBluetoothLauncher = main.registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> reInit());
    }

    public boolean reInitRequired(){
        if (!adapter.isEnabled()){
            reInit();
            return true;
        }

        return false;
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
        supported = false;
        enabled = false;
        initialized = false;

        adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter == null) {
            Log.i(TAG, "Bluetooth is not supported");
            initialized = true;
            supported = false;

            ((MainActivity)main).updateBluetoothStatus();
            return;
        }

        if (!adapter.isEnabled()) {
            Log.i(TAG, "Bluetooth is turned off");

            enabled = false;
            initialized = true;

            ((MainActivity)main).updateBluetoothStatus();
            return;

        }else enabled = true;

        open();
    }

    private void open(){
        if(!adapter.getProfileProxy(main, this, BluetoothProfile.HID_DEVICE)){
            Log.i(TAG, "Bluetooth HID profile is not supported");
            initialized = true;
            supported = false;

            ((MainActivity)main).updateBluetoothStatus();
            return;
        }

        supported = true;
    }

    private void start(){
        Log.i(TAG, "Opened HID Profile successfully");
        initialized = true;

        discoverer = new BluetoothDiscoverer(main, adapter);
        device = new HidDevice(service, this, main);

        Log.d(TAG, "Registering with a HID Device!");
        device.register();

        ((MainActivity)main).updateBluetoothStatus();
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
            Toast.makeText(main, "Reloading Bluetooth", Toast.LENGTH_SHORT).show();

            open();
        }
    }

    public boolean isSupported() {
        return supported;
    }
    public boolean isConnected() {
        if (!initialized || !supported) return false;
        return getHost().isConnected();
    }
    public boolean isConnecting(){
        if (!initialized || !supported) return false;
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
