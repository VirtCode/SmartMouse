package ch.virt.smartphonemouse.ui.debug;

import android.view.View;

public abstract class OnDoubleClickListener implements View.OnClickListener {

    public static final long DOUBLE_CLICK_DELAY = 200;

    private long lastClickTime = 0;

    @Override
    public void onClick(View v) {
        long currentClickTime = System.currentTimeMillis();

        if (currentClickTime - lastClickTime < DOUBLE_CLICK_DELAY) onDoubleClick(v);

        lastClickTime = currentClickTime;

    }

    public abstract void onDoubleClick(View v);
}
