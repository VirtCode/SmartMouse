package ch.virt.smartphonemouse.transmission;

public class HostDevice {

    private String address;
    private String name;
    private long lastConnected;

    public HostDevice(String address, String name) {
        this.address = address;
        this.name = name;
        this.lastConnected = -1;
    }

    public void setLastConnected(long lastConnected) {
        this.lastConnected = lastConnected;
    }

    public String getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }

    public long getLastConnected() {
        return lastConnected;
    }
}
