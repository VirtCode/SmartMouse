package ch.virt.smartphonemouse.ui.connect.dialog;

import android.Manifest;
import android.content.pm.PackageManager;
import android.view.View;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;

import ch.virt.smartphonemouse.R;
import ch.virt.smartphonemouse.helper.MainContext;
import ch.virt.smartphonemouse.ui.CustomFragment;

public class AddRequestPermissionSubdialog extends CustomFragment {

    TextView error;

    public AddRequestPermissionSubdialog(MainContext context) {
        super(R.layout.subdialog_add_requestpermission, context);
    }

    public void showError(){
        error.post(() -> error.setVisibility(View.VISIBLE));
    }

    @Override
    public void render() {

    }

    @Override
    protected void loadComponents(View view) {
        error = view.findViewById(R.id.add_request_permission_error);
    }
}
