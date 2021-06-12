package ch.virt.smartphonemouse.transmission.hid;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHidDevice;
import android.bluetooth.BluetoothHidDeviceAppSdpSettings;
import android.util.Log;

import ch.virt.smartphonemouse.helper.MainContext;
import ch.virt.smartphonemouse.transmission.hid.HidDescriptor;

public class HidDevice extends BluetoothHidDevice.Callback {
    private static final String TAG = "HidDevice";
    
    private static final String NAME = "Smartphone Mouse";
    private static final String DESCRIPTION = "Acceleration based Smartphone Mouse";
    private static final String PROVIDER = "Virt";

    BluetoothHidDevice service;
    MainContext context;

    BluetoothDevice device;

    boolean registered;
    boolean connected;
    boolean connecting;

    public HidDevice(BluetoothHidDevice device, MainContext context) {
        this.service = device;
        this.context = context;
    }

    private  BluetoothHidDeviceAppSdpSettings createSDP(){
        return new BluetoothHidDeviceAppSdpSettings(NAME, DESCRIPTION, PROVIDER, BluetoothHidDevice.SUBCLASS1_MOUSE, HidDescriptor.DESCRIPTOR);
    }

    public void register(){
        if (!registered) service.registerApp(createSDP(), null, null, context.getContext().getMainExecutor(), this);
        else Log.d(TAG, "The Device is already registered!");
    }

    @Override
    public void onAppStatusChanged(BluetoothDevice pluggedDevice, boolean registered) {
        this.registered = registered;

        Log.d(TAG, "The hid device has been registered " + (registered ? "successfully" : "unsuccessfully"));
    }

    @Override
    public void onConnectionStateChanged(BluetoothDevice device, int state) {

        switch (state){
            case BluetoothHidDevice.STATE_CONNECTED:
                connecting = false;
                connected = true;

                context.refresh();

                Log.d(TAG, "HID Host connected!");
                break;

            case BluetoothHidDevice.STATE_DISCONNECTED:
                connecting = false;
                connected = false;

                context.refresh();

                Log.d(TAG, "HID Host disconnected!");
                break;

            default: super.onConnectionStateChanged(device, state);
        }
    }

    public void connect(BluetoothDevice device){
        if (registered && !connected && !connecting){

            this.device = device;
            service.connect(device);
            connecting = true;

            context.refresh();

        } else Log.d(TAG, "Cannot connect to host whilst connecting or being connected and must be registered");
    }
    
    public void disconnect(){
        if (registered && connected && !connecting){

            service.disconnect(device);

        } else Log.d(TAG, "Cannot connect to host whilst connecting or being connected");
    }

    public void sendReport(boolean left, boolean middle, boolean right, int wheel, int x, int y){
        if (!registered || !connected || connecting){
            Log.d(TAG, "Cannot send a to the host report when no device is connected successfully!");
            return;
        }

        byte[] report = new byte[4];

        report[0] = (byte) ((left ? 1 : 0) | (middle ? 2 : 0) | (right ? 4 : 0)); // First bit left, second middle, third right and the rest padding
        report[1] = (byte) x;
        report[2] = (byte) y;
        report[3] = (byte) wheel;

        service.sendReport(device, 1, report); // Id 1 because of the descriptor
    }

    public BluetoothDevice getDevice() {
        return device;
    }

    public boolean isRegistered() {
        return registered;
    }

    public boolean isConnected() {
        return connected;
    }

    public boolean isConnecting() {
        return connecting;
    }
}
