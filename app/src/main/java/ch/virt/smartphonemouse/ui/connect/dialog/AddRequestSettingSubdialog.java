package ch.virt.smartphonemouse.ui.connect.dialog;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import ch.virt.smartphonemouse.R;

/**
 * This class holds the sub page of the add dialog that is displayed when the user should enable location in order to be able to discover near devices.
 */
public class AddRequestSettingSubdialog extends Fragment {

    private TextView error;

    /**
     * Creates the sub dialog.
     */
    public AddRequestSettingSubdialog() {
        super(R.layout.subdialog_add_requestsetting);
    }

    /**
     * Shows the error on the screen.
     */
    public void showError(){
        error.post(() -> error.setVisibility(View.VISIBLE));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        error = view.findViewById(R.id.add_request_setting_error);
    }
}
