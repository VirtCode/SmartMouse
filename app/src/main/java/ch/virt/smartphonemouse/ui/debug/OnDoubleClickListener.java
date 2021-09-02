package ch.virt.smartphonemouse.ui.debug;

import android.view.View;

/**
 * This class is a further abstraction of the on click listener that listens to double clicks.
 */
public abstract class OnDoubleClickListener implements View.OnClickListener {

    public static final long DOUBLE_CLICK_DELAY = 200;

    private long lastClickTime = 0;

    @Override
    public void onClick(View v) {
        long currentClickTime = System.currentTimeMillis();

        if (currentClickTime - lastClickTime < DOUBLE_CLICK_DELAY) onDoubleClick(v);

        lastClickTime = currentClickTime;

    }

    /**
     * Gets called when a double click is performed.
     *
     * @param v view it was performed on
     */
    public abstract void onDoubleClick(View v);
}
