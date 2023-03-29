package ch.virt.smartphonemouse.transmission;

import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import ch.virt.smartphonemouse.mouse.Processing;
import ch.virt.smartphonemouse.mouse.math.Vec2f;
import ch.virt.smartphonemouse.mouse.math.Vec3f;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * This class handles transmission to a SensorServer. It is used with the following workflow:
 * <p>First, the transmission should be connected to a SensorServer (see DebugTransmitter#connect()). During this process, the transmitter logs in and transmits its columns.</p>
 * <p>After a successful connection, the transmitter may start a transmission (see DebugTransmitter#startTransmission()).</p>
 * <p>If a transmission is open, the transmitter can operate. To transmit data, a client has to stage data in the order, he has registered the columns earlier. If all data for a data row is staged, data has to be committed (see DebugTransmitter#commmit()).</p>
 * <p>After a transmission, the transmission can be ended (see DebugTransmitter#endTransmission()), and the connection closed if required (see DebugTransmitter#disconnect())</p>
 */
public class DebugTransmitter {

    private static final String TAG = "DebugTransmitter";

    public static final byte ID_LOGIN = 0x01;
    public static final byte ID_DATA_REGISTER = 0x02;
    public static final byte ID_TRANSMISSION_STATE = 0x03;
    public static final byte ID_DATA = 0x04;

    public static final byte TYPE_BOOL = 0x01;
    public static final byte TYPE_I32 = 0x02;
    public static final byte TYPE_I64 = 0x03;
    public static final byte TYPE_F32 = 0x04;
    public static final byte TYPE_F64 = 0x05;

    private final SharedPreferences preferences;

    // A change requires a restart of the app
    private final boolean enabled;
    private final String host;
    private final int port;

    private boolean connected;
    private String connectionFailure;
    private final BlockingQueue<ByteBuffer> pendingPackets;
    private Thread thread;

    private static class Column {
        String name;
        byte type;

        public Column(String name, byte type) {
            this.name = name;
            this.type = type;
        }
    }
    private final List<Column> columns;
    private int size;

    private boolean transmitting;
    private ByteBuffer currentData;

    /**
     * Creates a transmitter
     * @param preferences preferences to read enabled, host and port from
     */
    public DebugTransmitter(SharedPreferences preferences) {
        this.preferences = preferences;

        this.enabled = preferences.getBoolean("debugEnabled", false);
        this.host = this.preferences.getString("debugHost", "undefined");
        this.port = this.preferences.getInt("debugPort", 55555);

        this.connected = false;
        this.pendingPackets = new LinkedBlockingDeque<>();
        this.columns = new ArrayList<>();
    }

    /**
     * Returns whether debugging is currently enabled
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Returns whether the transmitter is currently connected
     */
    public boolean isConnected() {
        return connected;
    }

    /**
     * Returns a string combination of the server hostname and port
     */
    public String getServerString() {
        return host + ":" + port;
    }

    /**
     * Connects to the set SensorServer
     */
    public void connect() {
        if (!enabled || connected) return;

        Log.i(TAG, "connect: Connecting to debug host on " + host + ":" + port);
        connected = false;
        if (thread != null) thread.interrupt();

        thread = new Thread(() -> {
            while (true) {
                try {

                    Socket socket = new Socket(host, port);
                    OutputStream stream = socket.getOutputStream();
                    Log.i(TAG, "connect: Successfully connected to debug host");

                    pendingPackets.clear();
                    transmitLogin();
                    reloadColumns();
                    transmitColumns();

                    connected = true;

                    try {
                        while (socket.isConnected()) {
                            ByteBuffer packet = pendingPackets.take();

                            stream.write(packet.array());
                            stream.flush();
                        }

                        if (socket.isConnected()) socket.close();

                        connected = false;
                    } catch (InterruptedException e) {
                        Log.w(TAG, "Thread was interrupted, disconnecting");

                        socket.close();
                        connected = false;
                        break;
                    }


                } catch (IOException e) {
                    connected = false;
                    connectionFailure = e.getMessage();
                    Log.i(TAG, "connect: Failed to connect to debug host: " + connectionFailure, e);

                    try {
                        Thread.sleep(10000); // Wait 10 seconds until attempting reconnection
                    } catch (InterruptedException ignored) { break; }
                }
            }
        });

        thread.start();
    }

    /**
     * Disconnects from the server
     */
    public void disconnect() {
        thread.interrupt();
        connected = false;
    }

    /**
     * Registers a column for transmission
     * @param name column name
     * @param type data type of column
     */
    public void registerColumn(String name, Class<?> type) {
        if (!enabled) return;

        // Add columns
        if (Vec2f.class.equals(type)) {
            columns.add(new Column(name + "-x", TYPE_F32));
            columns.add(new Column(name + "-y", TYPE_F32));
        } else if (Vec3f.class.equals(type)) {
            columns.add(new Column(name + "-x", TYPE_F32));
            columns.add(new Column(name + "-y", TYPE_F32));
            columns.add(new Column(name + "-z", TYPE_F32));
        } else if (Float.class.equals(type)) columns.add(new Column(name, TYPE_F32));
        else if (Double.class.equals(type)) {
            columns.add(new Column(name, TYPE_F64));
        } else if (Integer.class.equals(type)) {
            columns.add(new Column(name, TYPE_I32));
        } else if (Long.class.equals(type)) {
            columns.add(new Column(name, TYPE_I64));
        } else if (Boolean.class.equals(type)) {
            columns.add(new Column(name, TYPE_BOOL));
        }

        // Update size
        int size = 0;
        for (Column c : columns) {
            switch (c.type) {
                case TYPE_BOOL:
                    size += 1;
                    break;
                case TYPE_I32:
                case TYPE_F32:
                    size += 4;
                    break;
                case TYPE_I64:
                case TYPE_F64:
                    size += 8;
                    break;
            }
        }

        this.size = size;
    }

    /**
     * Starts a new transmission
     */
    public void startTransmission() {
        if (!enabled || !connected) return; // Allow when not connected, to instantly start transmission
        pendingPackets.clear(); // remove packets from previous transmission if present

        transmitTransmission(true);

        transmitting = true;
        currentData = ByteBuffer.allocate(size);
    }

    /**
     * Ends the current transmission
     */
    public void endTransmission() {
        if (!enabled || !connected || !transmitting) return;
        transmitTransmission(false);

        transmitting = false;
    }

    /**
     * Stages a 3d float vector
     */
    public void stageVec3f(Vec3f data) {
        if (!enabled || !connected || !transmitting) return;
        if (currentData.remaining() < 3 * 4) return;

        currentData.putFloat(data.x);
        currentData.putFloat(data.y);
        currentData.putFloat(data.z);
    }

    /**
     * Stages a 2d float vector
     */
    public void stageVec2f(Vec2f data) {
        if (!enabled || !connected || !transmitting) return;
        if (currentData.remaining() < 2 * 4) return;

        currentData.putFloat(data.x);
        currentData.putFloat(data.y);
    }

    /**
     * Stages a float
     */
    public void stageFloat(float data) {
        if (!enabled || !connected || !transmitting) return;
        if (currentData.remaining() < 4) return;

        currentData.putFloat(data);
    }

    /**
     * Stages a double
     */
    public void stageDouble(double data) {
        if (!enabled || !connected || !transmitting) return;
        if (currentData.remaining() < 8) return;

        currentData.putDouble(data);
    }

    /**
     * Stages an integer
     */
    public void stageInteger(int data) {
        if (!enabled || !connected || !transmitting) return;
        if (currentData.remaining() < 4) return;

        currentData.putInt(data);
    }

    /**
     * Stages a long
     */
    public void stageLong(long data) {
        if (!enabled || !connected || !transmitting) return;
        if (currentData.remaining() < 8) return;

        currentData.putLong(data);
    }

    /**
     * Stages a boolean
     */
    public void stageBoolean(boolean data) {
        if (!enabled || !connected || !transmitting) return;
        if (currentData.remaining() < 1) return;

        currentData.put((byte) (data ? 0x01 : 0x00));
    }

    /**
     * Commits current staged data and transmits it to the server
     */
    public void commit() {
        if (!enabled || !connected || !transmitting) return;

        transmitData(currentData);
        currentData.clear();
    }

    /**
     * Transmits a login packet to the server
     */
    private void transmitLogin() {
        // null-terminate string here --v
        byte[] model = (Build.MODEL + '\0').getBytes(StandardCharsets.US_ASCII);

        ByteBuffer buffer = ByteBuffer.allocate(1 + model.length); // Packet ID + Model Name

        buffer.put(ID_LOGIN);
        buffer.put(model);

        pendingPackets.add(buffer);
    }

    /**
     * Reloads required columns from the Processing class
     */
    private void reloadColumns() {
        columns.clear();
        Processing.registerDebugColumns(this);
    }

    /**
     * Transmits all registered columns to the server
     */
    private void transmitColumns() {
        for (Column column : columns) {
            // null-terminate string here  v
            byte[] name = (column.name + '\0').getBytes(StandardCharsets.US_ASCII);

            ByteBuffer buffer = ByteBuffer.allocate(1 + 1 + name.length); // Packet ID + Type + Column Name

            buffer.put(ID_DATA_REGISTER);
            buffer.put(column.type);
            buffer.put(name);

            pendingPackets.add(buffer);
        }
    }

    /**
     * Transmits the beginning or end of a transmission to the server
     * @param start is beginning
     */
    private void transmitTransmission(boolean start) {
        ByteBuffer buffer = ByteBuffer.allocate(1 + 1); // Packet ID + Start or End

        buffer.put(ID_TRANSMISSION_STATE);
        buffer.put((byte) (start ? 0x01 : 0x00));

        pendingPackets.add(buffer);
    }

    /**
     * Transmits processing data to the server
     * @param data buffer to transmit
     */
    private void transmitData(ByteBuffer data) {
        ByteBuffer buffer = ByteBuffer.allocate(1 + data.capacity()); // Packet ID + Data
        data.position(0);

        buffer.put(ID_DATA);
        buffer.put(data);

        pendingPackets.add(buffer);
    }

}
