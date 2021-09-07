package ch.virt.smartphonemouse.ui.mouse;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import ch.virt.smartphonemouse.R;

/**
 * This sub dialog shows some basic information as text to the user.
 */
public class MouseMessageSubdialog extends Fragment {

    private String message;
    private TextView messageView;

    /**
     * Creates the message sub dialog.
     *
     * @param message message that gets shown
     */
    public MouseMessageSubdialog(String message) {
        super(R.layout.subdialog_mouse_message);

        this.message = message;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        messageView = view.findViewById(R.id.mouse_message_message);

        messageView.setText(message);
    }
}
