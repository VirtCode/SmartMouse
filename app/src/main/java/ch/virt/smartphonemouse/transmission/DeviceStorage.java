package ch.virt.smartphonemouse.transmission;

import android.content.Context;

import androidx.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * This class stores already known bluetooth devices.
 */
public class DeviceStorage {
    public static final String DEVICES_KEY = "devices";

    private Context context;
    private List<HostDevice> devices;

    /**
     * Creates and loads the device storage.
     *
     * @param context context to access preferences
     */
    public DeviceStorage(Context context) {
        this.context = context;
        load();
    }

    /**
     * Loads the devices from the preferences.
     */
    public void load() {
        String src = PreferenceManager.getDefaultSharedPreferences(context).getString(DEVICES_KEY, "[]");
        devices = new Gson().fromJson(src, new TypeToken<ArrayList<HostDevice>>() { }.getType());
    }

    /**
     * Saves the devices to the preferences.
     */
    public void save() {
        devices.sort((o1, o2) -> -Long.compare(o1.getLastConnected(), o2.getLastConnected()));
        String src = new Gson().toJson(devices);
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(DEVICES_KEY, src).apply();
    }

    /**
     * Returns all known devices.
     *
     * @return list of known host devices.
     */
    public List<HostDevice> getDevices() {
        return devices;
    }

    /**
     * Returns a specific host device at a certain index.
     *
     * @param i index of the device.
     * @return known device
     */
    public HostDevice getDevice(int i) {
        return devices.get(i);
    }

    /**
     * Returns a specific host device with a certain bluetooth mac address.
     *
     * @param address bluetooth mac address
     * @return known device
     */
    public HostDevice getDevice(String address) {
        for (HostDevice device : devices) {
            if (device.getAddress().equals(address)) return device;
        }

        return null;
    }

    /**
     * Adds a device to the storage.
     *
     * @param device known device to add
     */
    public void addDevice(HostDevice device) {
        devices.add(device);
        save();
    }

    /**
     * Removes a device at an index from the known devices.
     *
     * @param i index of that device
     */
    public void removeDevice(int i) {
        HostDevice device = getDevice(i);

        if (device != null) {
            removeDevice(device);
        }
    }

    /**
     * Removes a device with a specific bluetooth mac address.
     *
     * @param address bluetooth mac address of that device
     */
    public void removeDevice(String address) {
        HostDevice device = getDevice(address);

        if (device != null) {
            removeDevice(device);
        }
    }

    /**
     * Removes a known device.
     *
     * @param device device to remove
     */
    public void removeDevice(HostDevice device) {
        devices.remove(device);
        save();
    }

    /**
     * Returns whether a certain device is present in the known devices.
     *
     * @param address bluetooth mac address of said device
     * @return whether it is present
     */
    public boolean hasDevice(String address) {
        return getDevice(address) != null;
    }
}
