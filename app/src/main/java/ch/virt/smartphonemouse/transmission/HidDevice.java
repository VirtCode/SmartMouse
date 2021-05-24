package ch.virt.smartphonemouse.transmission;

import android.bluetooth.BluetoothHidDevice;
import android.bluetooth.BluetoothHidDeviceAppSdpSettings;

import ch.virt.smartphonemouse.ui.MainContext;

public class HidDevice extends BluetoothHidDevice.Callback {
    private static final String NAME = "Smartphone Mouse";
    private static final String DESCRIPTION = "Acceleration based Smartphone Mouse";
    private static final String PROVIDER = "Virt";

    BluetoothHidDevice service;
    MainContext context;

    public HidDevice(BluetoothHidDevice device, MainContext context) {
        this.service = device;
        this.context = context;
    }

    private  BluetoothHidDeviceAppSdpSettings createSDP(){
        return new BluetoothHidDeviceAppSdpSettings(NAME, DESCRIPTION, PROVIDER, BluetoothHidDevice.SUBCLASS1_MOUSE, HidDescriptor.DESCRIPTOR);
    }

    public void register(){
        service.registerApp(createSDP(), null, null, context.getContext().getMainExecutor(), this);
    }
}
