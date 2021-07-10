package ch.virt.smartphonemouse.ui.mouse;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import ch.virt.smartphonemouse.R;
import ch.virt.smartphonemouse.helper.MainContext;
import ch.virt.smartphonemouse.ui.CustomFragment;
import ch.virt.smartphonemouse.ui.settings.dialog.CalibrateFinishedSubdialog;
import ch.virt.smartphonemouse.ui.settings.dialog.SamplingRateSubdialog;

public class MouseMessageSubdialog extends CustomFragment {

    private String message;
    private TextView messageView;

    public MouseMessageSubdialog(String message, MainContext context) {
        super(R.layout.subdialog_mouse_message, context);

        this.message = message;
    }

    @Override
    public void render() {

    }

    @Override
    protected void initComponents() {
        messageView.setText(message);
    }

    @Override
    protected void loadComponents(View view) {
        messageView = view.findViewById(R.id.mouse_message_message);
    }
}
