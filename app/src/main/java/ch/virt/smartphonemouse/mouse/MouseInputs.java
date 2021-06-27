package ch.virt.smartphonemouse.mouse;

import ch.virt.smartphonemouse.transmission.BluetoothHandler;

public class MouseInputs {

    private float minimalPositionChange = 0;

    private float xPosition, yPosition;
    private int wheelPosition;
    private boolean leftButton, middleButton, rightButton;

    BluetoothHandler bluetoothHandler;

    private Thread thread;
    private boolean running;
    private long lastTime;

    public MouseInputs(BluetoothHandler bluetoothHandler) {
        this.bluetoothHandler = bluetoothHandler;
    }

    public void start(){
        thread = new Thread(this::run);

        running = true;
        thread.start();
    }

    public void run(){
        lastTime = System.nanoTime();

        while (running) {
            long current = System.nanoTime();

            if (current - lastTime >= 1e9 / 100){
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
