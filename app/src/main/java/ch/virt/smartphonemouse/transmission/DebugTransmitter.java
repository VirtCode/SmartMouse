package ch.virt.smartphonemouse.transmission;

import android.os.Build;
import android.util.Log;
import ch.virt.smartphonemouse.mouse.math.Vec2f;
import ch.virt.smartphonemouse.mouse.math.Vec3f;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

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

    private boolean enabled;
    private String host;
    private int port;

    private boolean connected;
    private String connectionFailure;
    private final List<ByteBuffer> pendingPackets;
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

    public DebugTransmitter(boolean enabled, String host, int port) {
        this.enabled = enabled;
        this.host = host;
        this.port = port;

        this.connected = false;
        this.pendingPackets = new ArrayList<>();
        this.columns = new ArrayList<>();
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void connect() { // TODO: Implement disconnect method
        if (!enabled) return;
        Log.i(TAG, "connect: Connecting to debug host on " + host + ":" + port);

        thread = new Thread(() -> {
            while (true) {
                try {

                    Socket socket = new Socket(host, port);
                    OutputStream stream = socket.getOutputStream();
                    Log.i(TAG, "connect: Successfully connected to debug host");

                    // Add login and columns up top
                    List<ByteBuffer> present = new ArrayList<>(pendingPackets);
                    pendingPackets.clear();
                    transmitLogin();
                    transmitColumns();
                    pendingPackets.addAll(present);

                    connected = true;

                    while (socket.isConnected()) {
                        while (!pendingPackets.isEmpty()) {
                            ByteBuffer packet = pendingPackets.remove(0);
                            if (packet == null) continue;

                            stream.write(packet.array());
                            stream.flush();
                        }

                        try {
                            Thread.sleep(1); // Busy-wait, yes not the cleanest approach but it will work i guess
                        } catch (InterruptedException ignored) {}
                    }


                } catch (IOException e) {
                    connected = false;
                    connectionFailure = e.getMessage();
                    Log.i(TAG, "connect: Failed to connect to debug host: " + connectionFailure);

                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException ignored) {
                    }
                }
            }
        });

        thread.start();
    }

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
        } else if (Float.class.equals(type)) {
            columns.add(new Column(name, TYPE_F32));
        } else if (Double.class.equals(type)) {
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

    public void startTransmission() {
        if (!enabled) return; // Allow when not connected, to instantly start transmission
        transmitTransmission(true);

        transmitting = true;
        currentData = ByteBuffer.allocate(size);
    }

    public void endTransmission() {
        if (!enabled || !connected || !transmitting) return;
        transmitTransmission(false);

        transmitting = false;
    }

    public void stageVec3f(Vec3f data) {
        if (!enabled || !connected || !transmitting) return;
        if (currentData.remaining() < 3 * 4) return;

        currentData.putFloat(data.x);
        currentData.putFloat(data.y);
        currentData.putFloat(data.z);
    }

    public void stageVec2f(Vec2f data) {
        if (!enabled || !connected || !transmitting) return;
        if (currentData.remaining() < 2 * 4) return;

        currentData.putFloat(data.x);
        currentData.putFloat(data.y);
    }

    public void stageFloat(float data) {
        if (!enabled || !connected || !transmitting) return;
        if (currentData.remaining() < 4) return;

        currentData.putFloat(data);
    }

    public void stageDouble(double data) {
        if (!enabled || !connected || !transmitting) return;
        if (currentData.remaining() < 8) return;

        currentData.putDouble(data);
    }

    public void stageInteger(int data) {
        if (!enabled || !connected || !transmitting) return;
        if (currentData.remaining() < 4) return;

        currentData.putInt(data);
    }

    public void stageLong(long data) {
        if (!enabled || !connected || !transmitting) return;
        if (currentData.remaining() < 8) return;

        currentData.putLong(data);
    }

    public void stageBoolean(boolean data) {
        if (!enabled || !connected || !transmitting) return;
        if (currentData.remaining() < 1) return;

        currentData.put((byte) (data ? 0x01 : 0x00));
    }

    public void commit() {
        if (!enabled || !connected || !transmitting) return;

        transmitData(currentData);
        currentData.clear();
    }

    private void transmitLogin() {
        // null-terminate string here --v
        byte[] model = (Build.MODEL + '\0').getBytes(StandardCharsets.US_ASCII);

        ByteBuffer buffer = ByteBuffer.allocate(1 + model.length); // Packet ID + Model Name

        buffer.put(ID_LOGIN);
        buffer.put(model);

        pendingPackets.add(buffer);
    }

    private void transmitColumns() {
        for (Column column : columns) {
            // null-terminate string here    --v
            byte[] name = (column.name + '\0').getBytes(StandardCharsets.US_ASCII);

            ByteBuffer buffer = ByteBuffer.allocate(1 + 1 + name.length); // Packet ID + Type + Column Name

            buffer.put(ID_DATA_REGISTER);
            buffer.put(column.type);
            buffer.put(name);

            pendingPackets.add(buffer);
        }
    }

    private void transmitTransmission(boolean start) {
        ByteBuffer buffer = ByteBuffer.allocate(1 + 1); // Packet ID + Start or End

        buffer.put(ID_TRANSMISSION_STATE);
        buffer.put((byte) (start ? 0x01 : 0x00));

        pendingPackets.add(buffer);
    }

    private void transmitData(ByteBuffer data) {
        ByteBuffer buffer = ByteBuffer.allocate(1 + data.capacity()); // Packet ID + Data
        data.position(0);

        buffer.put(ID_DATA);
        buffer.put(data);

        pendingPackets.add(buffer);
    }

}
