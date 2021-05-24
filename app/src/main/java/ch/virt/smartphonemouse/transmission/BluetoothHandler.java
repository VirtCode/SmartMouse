package ch.virt.smartphonemouse.transmission;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothHidDevice;
import android.bluetooth.BluetoothProfile;
import android.content.Intent;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;

import com.google.android.material.snackbar.Snackbar;

import ch.virt.smartphonemouse.R;
import ch.virt.smartphonemouse.helper.MainContext;

public class BluetoothHandler implements BluetoothProfile.ServiceListener {
    private static final String TAG = "BluetoothHandler";

    private BluetoothAdapter adapter;
    private BluetoothHidDevice service;
    private final MainContext context;

    private HidDevice device;

    private boolean initialized = false;
    private boolean enabled = false;
    private boolean supported = false;
    private boolean opened = false;
    private boolean connected = false;

    private ActivityResultLauncher<Intent> enableBluetoothLauncher;

    public BluetoothHandler(MainContext context) {
        this.context = context;

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
            connected = false;

            Log.i(TAG, "Reconnecting to Service");
            context.snack(context.getResources().getString(R.string.snack_bluetooth_closed), Snackbar.LENGTH_SHORT);

            open();
        }
    }

    public boolean isSupported() {
        return supported;
    }
    public boolean isConnected() {
        return connected;
    }
    public boolean isEnabled() {
        return enabled;
    }
    public boolean isInitialized() {
        return initialized;
    }
}
