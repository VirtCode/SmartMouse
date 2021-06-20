package ch.virt.smartphonemouse.mouse;

import ch.virt.smartphonemouse.transmission.BluetoothHandler;

public class MouseInputs {

    private float minimalPositionChange = 0;

    private float xPosition, yPosition;
    private int wheelPosition;
    private boolean leftButton, middleButton, rightButton;

    BluetoothHandler bluetoothHandler;

    public MouseInputs(BluetoothHandler bluetoothHandler) {
        this.bluetoothHandler = bluetoothHandler;
    }

    public void sendUpdate(){
        int x = (int) xPosition;
        int y = (int) yPosition;

        if (bluetoothHandler.getHost().isConnected()) bluetoothHandler.getHost().sendReport(leftButton, middleButton, rightButton, wheelPosition, x, y);

        // Reset Deltas
        xPosition -= x;
        yPosition -= y;
        wheelPosition = 0;
    }

    public void changeWheelPosition(int delta){
        if (delta != 0){
            wheelPosition += delta;
            sendUpdate(); // Wheel Positions should also be sent immediately
        }
    }

    public void setLeftButton(boolean leftButton) {
        if (this.leftButton != leftButton) {
            this.leftButton = leftButton;

            sendUpdate(); // Button Presses are urgent
        }
    }

    public void setMiddleButton(boolean middleButton) {
        if (this.middleButton != middleButton) {
            this.middleButton = middleButton;

            sendUpdate(); // Button Presses are urgent
        }
    }

    public void setRightButton(boolean rightButton) {
        if (this.rightButton != rightButton) {
            this.rightButton = rightButton;

            sendUpdate(); // Button Presses are urgent
        }
    }

    public void changeXPosition(int x){
        xPosition += x;

        if (Math.abs(xPosition) > minimalPositionChange) sendUpdate(); // Only send change after enough changes
    }

    public void changeYPosition(int y){
        yPosition += y;

        if (Math.abs(yPosition) > minimalPositionChange) sendUpdate(); // Only send change after enough changes
    }

}
