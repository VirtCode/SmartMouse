package ch.virt.smartphonemouse.transmission;

import android.bluetooth.BluetoothAdapter;
import android.os.SystemClock;

/**
 * @author VirtCode
 * @version 1.0
 */
public class BluetoothHandler {

    BluetoothAdapter adapter;

    boolean supported = false;

    private void init(){
        adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter == null){
            supported = false;
            return;
        }

        if (!adapter.isEnabled()){

        }
    }

    public boolean isSupported(){
        return true;
    }

    public boolean isConnected(){
        return true;
    }

    public String connectedDevice(){
        return "";
    }

    public long connectedSince(){
        return SystemClock.elapsedRealtime();
    }

    public void disconnect(){

    }

}
