package ch.virt.smartphonemouse.mouse;

import android.content.Context;

import androidx.preference.PreferenceManager;

import ch.virt.smartphonemouse.transmission.BluetoothHandler;

public class MouseInputs {

    private final float NANO_TO_FULL = 1e9f;

    private float xPosition, yPosition;
    private int wheelPosition;
    private boolean leftButton, middleButton, rightButton;

    private int transmissionRate = 0;
    private BluetoothHandler bluetoothHandler;
    private Context context;

    private Thread thread;
    private boolean running;
    private long lastTime;

    public MouseInputs(BluetoothHandler bluetoothHandler, Context context) {
        this.bluetoothHandler = bluetoothHandler;
        this.context = context;
    }

    public void start(){
        if (running) return;

        thread = new Thread(this::run);

        transmissionRate = PreferenceManager.getDefaultSharedPreferences(context).getInt("communicationTransmissionRate", 100);

        running = true;
        thread.start();
    }

    public void run(){
        lastTime = System.nanoTime();

        while (running) {
            long current = System.nanoTime();

            if (current - lastTime >= NANO_TO_FULL / transmissionRate){
                sendUpdate();
                lastTime = current;
            }
        }
    }

    public void stop(){
        running = false;
    }

    private void sendUpdate(){
        int x = (int) xPosition;
        int y = (int) yPosition;

        if (bluetoothHandler.getHost().isConnected()) bluetoothHandler.getHost().sendReport(leftButton, middleButton, rightButton, wheelPosition, x, y);

        // Reset Deltas
        xPosition -= x;
        yPosition -= y;
        wheelPosition = 0;
    }


    public void changeWheelPosition(int delta){
        wheelPosition += delta;
    }

    public void setLeftButton(boolean leftButton) {
        this.leftButton = leftButton;
    }

    public void setMiddleButton(boolean middleButton) {
        this.middleButton = middleButton;
    }

    public void setRightButton(boolean rightButton) {
        this.rightButton = rightButton;
    }

    public void changeXPosition(float x){
        xPosition += x;
    }

    public void changeYPosition(float y){
        yPosition += y;
    }

}
