package ch.virt.smartphonemouse.transmission;

import android.content.Context;

import androidx.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class DeviceStorage {
    public static final String DEVICES_KEY = "devices";

    Context context;

    List<HostDevice> devices;

    public DeviceStorage(Context context) {
        this.context = context;
        load();
    }

    public void load(){
        String src = PreferenceManager.getDefaultSharedPreferences(context).getString(DEVICES_KEY, "[]");
        devices = new Gson().fromJson(src, new TypeToken<ArrayList<HostDevice>>(){}.getType());
    }

    public void save(){
        devices.sort((o1, o2) -> -Long.compare(o1.getLastConnected(), o2.getLastConnected()));
        String src = new Gson().toJson(devices);
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(DEVICES_KEY, src).apply();
    }

    public List<HostDevice> getDevices() {
        return devices;
    }

    public HostDevice getDevice(int i){
        return devices.get(i);
    }

    public HostDevice getDevice(String address){
        for (HostDevice device : devices) {
            if (device.getAddress().equals(address)) return device;
        }

        return null;
    }

    public void addDevice(HostDevice device){
        devices.add(device);
        save();
    }

    public void removeDevice(int i){
        HostDevice device = getDevice(i);

        if (device != null) {
            removeDevice(device);
        }
    }

    public void removeDevice(String address){
        HostDevice device = getDevice(address);

        if (device != null) {
            removeDevice(device);
        }
    }

    public void removeDevice(HostDevice device){
        devices.remove(device);
        save();
    }

    public boolean hasDevice(String address){
        return getDevice(address) != null;
    }
}
