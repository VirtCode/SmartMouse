package ch.virt.smartphonemouse.ui.connect.dialog;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import ch.virt.smartphonemouse.R;

/**
 * This class holds a sub page for the add dialog when the user must grant the app permissions to discover new devices.
 */
public class AddRequestPermissionSubdialog extends Fragment {

    private TextView error;

    /**
     * Creates the sub dialog.
     */
    public AddRequestPermissionSubdialog() {
        super(R.layout.subdialog_add_requestpermission);
    }

    /**
     * Displays the error on the screen.
     */
    public void showError() {
        error.post(() -> error.setVisibility(View.VISIBLE));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        error = view.findViewById(R.id.add_request_permission_error);
    }
}
