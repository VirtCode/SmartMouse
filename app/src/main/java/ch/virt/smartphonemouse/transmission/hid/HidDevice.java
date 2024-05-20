package ch.virt.smartphonemouse.transmission.hid;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHidDevice;
import android.bluetooth.BluetoothHidDeviceAppSdpSettings;
import android.content.Context;
import android.os.SystemClock;
import android.util.Log;

import ch.virt.smartphonemouse.MainActivity;
import ch.virt.smartphonemouse.transmission.BluetoothHandler;
import ch.virt.smartphonemouse.transmission.HostDevice;

/**
 * This class is used to interact and use the bluetooth hid profile.
 */
public class HidDevice extends BluetoothHidDevice.Callback {
    private static final String TAG = "HidDevice";

    private static final String NAME = "Smartphone Mouse";
    private static final String DESCRIPTION = "Acceleration based Smartphone Mouse";
    private static final String PROVIDER = "Virt";

    private BluetoothHidDevice service;
    private Context context;

    private BluetoothDevice device;
    private long connectedSince;

    private boolean registered;
    private boolean connected;
    private boolean connecting;

    private boolean lastFailed;

    private BluetoothHandler bluetooth;

    /**
     * Creates this class.
     *
     * @param device    device from android to interact with
     * @param bluetooth bluetooth handler to perform bluetooth actions
     * @param context   context, presumably the main activity used for refreshing the ui
     */
    public HidDevice(BluetoothHidDevice device, BluetoothHandler bluetooth, Context context) {
        this.service = device;
        this.context = context;
        this.bluetooth = bluetooth;
    }

    /**
     * Creates the Service Discovery Protocol records.
     *
     * @return Service Discovery Protocol records
     */
    private BluetoothHidDeviceAppSdpSettings createSDP() {
        return new BluetoothHidDeviceAppSdpSettings(NAME, DESCRIPTION, PROVIDER, BluetoothHidDevice.SUBCLASS1_MOUSE, HidDescriptor.DESCRIPTOR);
    }

    /**
     * Registers the app as a hid.
     */
    public void register() {
        if (!registered)
            service.registerApp(createSDP(), null, null, context.getMainExecutor(), this);
        else Log.d(TAG, "The Device is already registered!");
    }

    @Override
    public void onAppStatusChanged(BluetoothDevice pluggedDevice, boolean registered) {
        this.registered = registered;

        Log.d(TAG, "The hid device is now " + (registered ? "registered" : "NOT registered"));
    }

    @Override
    public void onConnectionStateChanged(BluetoothDevice device, int state) {

        switch (state) {
            case BluetoothHidDevice.STATE_CONNECTED:
                connecting = false;
                connected = true;

                connectedSince = SystemClock.elapsedRealtime();
                bluetooth.getDevices().getDevice(device.getAddress()).setLastConnected(System.currentTimeMillis());
                bluetooth.getDevices().save();

                ((MainActivity) context).updateBluetoothStatus();

                Log.d(TAG, "HID Host connected!");
                break;

            case BluetoothHidDevice.STATE_DISCONNECTED:
                if (!connected) lastFailed = true;

                connecting = false;
                connected = false;

                ((MainActivity) context).updateBluetoothStatus();

                Log.d(TAG, "HID Host disconnected!");
                break;

            default:
                super.onConnectionStateChanged(device, state);
        }
    }

    /**
     * Connects as a HID to the provided host device
     *
     * @param deviceH host device to connect to
     */
    public void connect(HostDevice deviceH) {
        if (bluetooth.reInitRequired()) return;

        if (registered && !connected && !connecting) {
            this.device = bluetooth.fromHostDevice(deviceH);

            service.connect(device);
            connecting = true;

            ((MainActivity) context).updateBluetoothStatus();

        } else
            Log.d(TAG, "Cannot connect to host whilst connecting or being connected and must be registered");
    }

    /**
     * Disconnects from the host device.
     */
    public void disconnect() {
        if (registered && connected && !connecting) {

            service.disconnect(device);

        } else Log.d(TAG, "Cannot connect to host whilst connecting or not being connected");
    }

    /**
     * Sends a report to the host device.
     *
     * @param left   whether the left mouse button is pressed
     * @param middle whether the middle mouse button is pressed
     * @param right  whether the right mouse button is pressed
     * @param wheel  change of position of the mouse wheel
     * @param x      change of x position
     * @param y      change of y position
     */
    public void sendMouseReport(boolean left, boolean middle, boolean right, int wheel, int x, int y) {
        if (!registered || !connected || connecting) {
            Log.d(TAG, "Cannot send a report to the host when no device is connected successfully!");
            return;
        }

        byte[] report = new byte[4];

        report[0] = (byte) ((left ? 1 : 0) | (middle ? 4 : 0) | (right ? 2 : 0)); // First bit left, second right, third middle and the rest padding
        report[1] = (byte) x;
        report[2] = (byte) y;
        report[3] = (byte) wheel;

        service.sendReport(device, 1, report); // id 1 because of the descriptor
    }

    public void sendTouchpadReport(boolean down, int x, int y, boolean down2, int x2, int y2, boolean down3, int x3, int y3) {
        if (!registered || !connected || connecting) {
            Log.d(TAG, "Cannot send a report to the host when no device is connected successfully!");
        }

        byte[] report = new byte[10];
        report[0] = (byte) ((down ? 3 : 0) | 0x01 << 2);
        report[1] = (byte) (x);
        report[2] = (byte) (x >> 8);
        report[3] = (byte) (y);
        report[4] = (byte) (y >> 8);
        report[5 + 0] = (byte) ((down2 ? 3 : 0) | 0x10 << 2);
        report[5 + 1] = (byte) (x2);
        report[5 + 2] = (byte) (x2 >> 8);
        report[5 + 3] = (byte) (y2);
        report[5 + 4] = (byte) (y2 >> 8);
        /*
        report[10 + 0] = (byte) ((down3 ? 3 : 0) | 0x11 << 2);
        report[10 + 1] = (byte) (x3);
        report[10 + 2] = (byte) (x3 >> 8);
        report[10 + 3] = (byte) (y3);
        report[10 + 4] = (byte) (y3 >> 8);

         */

        service.sendReport(device, 2, report); // id 2, see descriptor
    }

    /**
     * Returns the currently connected host device.
     *
     * @return connected device
     */
    public BluetoothDevice getDevice() {
        return device;
    }

    /**
     * Returns whether the app is registered.
     *
     * @return is registered
     */
    public boolean isRegistered() {
        return registered;
    }

    /**
     * Returns whether the app is connected to a host device.
     *
     * @return is connected
     */
    public boolean isConnected() {
        return connected;
    }

    /**
     * Returns whether the app is currently connecting to a host device.
     *
     * @return is connecting
     */
    public boolean isConnecting() {
        return connecting;
    }

    /**
     * Returns whether the last connection to a host device has failed.
     *
     * @return has last failed
     */
    public boolean hasFailed() {
        return lastFailed;
    }

    /**
     * Marks the last failed status as read.
     */
    public void markFailedAsRead() {
        lastFailed = false;
    }

    /**
     * Returns the timestamp when the host device has connected.
     *
     * @return timestamp in milliseconds since the device booted
     */
    public long getConnectedSince() {
        return connectedSince;
    }
}
