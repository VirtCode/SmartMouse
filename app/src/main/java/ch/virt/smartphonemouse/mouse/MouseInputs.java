package ch.virt.smartphonemouse.mouse;

import android.content.Context;

import androidx.preference.PreferenceManager;

import ch.virt.smartphonemouse.transmission.BluetoothHandler;

/**
 * This class collects the calculated inputs and transmits them to the computer when needed.
 */
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

    /**
     * Creates this class.
     *
     * @param bluetoothHandler bluetooth handler to send the signals to
     * @param context          context to fetch settings from
     */
    public MouseInputs(BluetoothHandler bluetoothHandler, Context context) {
        this.bluetoothHandler = bluetoothHandler;
        this.context = context;
    }

    /**
     * Starts the transmission to the pc.
     */
    public void start() {
        if (running) return;

        thread = new Thread(this::run);

        transmissionRate = PreferenceManager.getDefaultSharedPreferences(context).getInt("communicationTransmissionRate", 100);

        running = true;
        thread.start();
    }

    /**
     * Starts the sending loop.
     * Should be executed on a separate thread.
     */
    private void run() {
        lastTime = System.nanoTime();

        while (running) {
            long current = System.nanoTime();

            if (current - lastTime >= NANO_TO_FULL / transmissionRate) {
                sendUpdate();
                lastTime = current;
            }
        }
    }

    /**
     * Stops the transmission to the pc.
     */
    public void stop() {
        running = false;
    }

    /**
     * Sends an update to the pc.
     */
    private void sendUpdate() {
        int x = (int) xPosition;
        int y = (int) yPosition;

        if (bluetoothHandler.getHost().isConnected())
            bluetoothHandler.getHost().sendReport(leftButton, middleButton, rightButton, wheelPosition, x, y);

        // Reset Deltas
        xPosition -= x;
        yPosition -= y;
        wheelPosition = 0;
    }


    /**
     * Changes the current wheel position.
     *
     * @param delta change in wheel steps
     */
    public void changeWheelPosition(int delta) {
        wheelPosition += delta;
    }

    /**
     * Sets the current state of the left button.
     *
     * @param leftButton whether the left button is pressed
     */
    public void setLeftButton(boolean leftButton) {
        this.leftButton = leftButton;
    }

    /**
     * Sets the current state of the middle button.
     *
     * @param middleButton whether the middle button is pressed
     */
    public void setMiddleButton(boolean middleButton) {
        this.middleButton = middleButton;
    }

    /**
     * Sets the current state of the right button.
     *
     * @param rightButton whether the right button is pressed
     */
    public void setRightButton(boolean rightButton) {
        this.rightButton = rightButton;
    }

    /**
     * Changes the x position of the mouse
     *
     * @param x change of the position
     */
    public void changeXPosition(float x) {
        xPosition += x;
    }

    /**
     * Changes the y position of the mouse
     *
     * @param y change of the position
     */
    public void changeYPosition(float y) {
        yPosition += y;
    }
}
