package ch.virt.smartphonemouse.transmission;

/**
 * This class represents a known host device.
 */
public class HostDevice {

    private String address;
    private String name;
    private long lastConnected;

    /**
     * Creates a host device.
     *
     * @param address bluetooth mac address of the device
     * @param name    name of the device
     */
    public HostDevice(String address, String name) {
        this.address = address;
        this.name = name;
        this.lastConnected = -1;
    }

    /**
     * Sets when the device has last connected.
     *
     * @param lastConnected unix timestamp when last connected
     */
    public void setLastConnected(long lastConnected) {
        this.lastConnected = lastConnected;
    }

    /**
     * Returns the bluetooth mac address of the device.
     *
     * @return bluetooth mac address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Returns the name of the device.
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns when the device was last connected to.
     *
     * @return unix timestamp of when it was last connected
     */
    public long getLastConnected() {
        return lastConnected;
    }
}
