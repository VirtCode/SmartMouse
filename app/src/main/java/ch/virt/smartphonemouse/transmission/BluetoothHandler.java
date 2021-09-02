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

/**
 * This class handles everything related to bluetooth.
 */
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

    private ActivityResultLauncher<Intent> enableBluetoothLauncher;

    /**
     * Creates a bluetooth handler
     *
     * @param context activity to use for various things
     */
    public BluetoothHandler(ComponentActivity context) {
        this.main = context;
        discoverer = new BluetoothDiscoverer(main, adapter);
        devices = new DeviceStorage(main);

        enableBluetoothLauncher = main.registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> reInit());
    }

    /**
     * Checks whether bluetooth is still turned on and if not, reinitializes
     *
     * @return whether a reinitialization is required
     */
    public boolean reInitRequired() {
        if (!adapter.isEnabled()) {
            reInit();
            return true;
        }

        return false;
    }

    /**
     * Reinitializes the bluetooth things.
     */
    public void reInit() {
        initialized = false;
        init();
    }

    /**
     * Enables bluetooth by prompting the user.
     */
    public void enableBluetooth() {

        if (!adapter.isEnabled()) {
            Log.i(TAG, "Enabling Bluetooth");

            enableBluetoothLauncher.launch(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE));
        }
    }

    /**
     * Intitializes the bluetooth things.
     */
    private void init() {
        supported = false;
        enabled = false;
        initialized = false;

        adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter == null) {
            Log.i(TAG, "Bluetooth is not supported");
            initialized = true;
            supported = false;

            ((MainActivity) main).updateBluetoothStatus();
            return;
        }

        if (!adapter.isEnabled()) {
            Log.i(TAG, "Bluetooth is turned off");

            enabled = false;
            initialized = true;

            ((MainActivity) main).updateBluetoothStatus();
            return;

        } else enabled = true;

        open();
    }

    /**
     * Opens the bluetooth hid profile.
     */
    private void open() {
        if (!adapter.getProfileProxy(main, this, BluetoothProfile.HID_DEVICE)) {
            Log.i(TAG, "Bluetooth HID profile is not supported");
            initialized = true;
            supported = false;

            ((MainActivity) main).updateBluetoothStatus();
            return;
        }

        supported = true;
    }

    /**
     * Registers the app as a hid device.
     */
    private void start() {
        Log.i(TAG, "Opened HID Profile successfully");
        initialized = true;

        discoverer = new BluetoothDiscoverer(main, adapter);
        device = new HidDevice(service, this, main);

        Log.d(TAG, "Registering with a HID Device!");
        device.register();

        ((MainActivity) main).updateBluetoothStatus();
    }

    @Override
    public void onServiceConnected(int profile, BluetoothProfile proxy) {
        if (profile == BluetoothProfile.HID_DEVICE) {
            this.service = (BluetoothHidDevice) proxy;

            start();
        }
    }

    @Override
    public void onServiceDisconnected(int profile) {
        if (profile == BluetoothProfile.HID_DEVICE) {

            Log.i(TAG, "Reconnecting to Service");
            Toast.makeText(main, "Reloading Bluetooth", Toast.LENGTH_SHORT).show();

            open();
        }
    }

    /**
     * Returns whether the bluetooth hid profile is supported by this device.
     *
     * @return is supported
     */
    public boolean isSupported() {
        return supported;
    }

    /**
     * Returns whether the app is connected to a host device.
     *
     * @return is connected
     */
    public boolean isConnected() {
        if (!initialized || !supported) return false;
        return getHost().isConnected();
    }

    /**
     * Returns whether the app is currently connecting to a host device
     *
     * @return is connecting
     */
    public boolean isConnecting() {
        if (!initialized || !supported) return false;
        return getHost().isConnecting();
    }

    /**
     * Returns whether bluetooth is enabled
     *
     * @return is enabled
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Returns whether bluetooth has ben initialized.
     *
     * @return is initialized
     */
    public boolean isInitialized() {
        return initialized;
    }

    /**
     * Returns the class responsible for discovering new bluetooth devices.
     *
     * @return bluetooth discoverer
     */
    public BluetoothDiscoverer getDiscoverer() {
        return discoverer;
    }

    /**
     * Returns a storage of all known devices.
     *
     * @return known devices
     */
    public DeviceStorage getDevices() {
        return devices;
    }

    /**
     * Returns the hid interface.
     *
     * @return hid device to interact with the hid profile
     */
    public HidDevice getHost() {
        return device;
    }

    /**
     * Gets a real bluetooth device from a host device that can be saved.
     *
     * @param device saved host device
     * @return fetched bluetooth device
     */
    public BluetoothDevice fromHostDevice(HostDevice device) {
        return adapter.getRemoteDevice(device.getAddress());
    }

    /**
     * Returns whether the smartphone is already bonded to a certain device.
     *
     * @param address address of that device
     * @return whether it is bonded
     */
    public boolean isBonded(String address) {

        for (BluetoothDevice device : adapter.getBondedDevices()) {
            if (device.getAddress().equals(address)) return true;
        }

        return false;
    }
}
