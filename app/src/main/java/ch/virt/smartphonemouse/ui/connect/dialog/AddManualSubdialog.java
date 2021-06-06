package ch.virt.smartphonemouse.ui.connect.dialog;

import android.view.View;
import android.view.WindowManager;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import ch.virt.smartphonemouse.R;
import ch.virt.smartphonemouse.helper.MainContext;
import ch.virt.smartphonemouse.ui.CustomFragment;

public class AddManualSubdialog extends CustomFragment {

    TextInputLayout nameLayout, macLayout;

    public AddManualSubdialog(MainContext context) {
        super(R.layout.subdialog_add_manual, context);
    }

    @Override
    public void render() {

    }

    public boolean check(){
        boolean valid = true;

        if (nameLayout.getEditText().getText() == null || nameLayout.getEditText().getText().toString().equals("")) {
            nameLayout.setErrorEnabled(true);
            nameLayout.setError(getString(R.string.dialog_add_manual_name_error));
            valid = false;
        } else {
            nameLayout.setErrorEnabled(false);
        }

        if (macLayout.getEditText().getText() == null || !macLayout.getEditText().getText().toString().matches("^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$")){
            macLayout.setErrorEnabled(true);
            macLayout.setError(getString(R.string.dialog_add_manual_mac_error));
            valid = false;
        } else {
            macLayout.setErrorEnabled(false);
        }


        return valid;
    }

    @Override
    protected void loadComponents(View view) {
        nameLayout = view.findViewById(R.id.add_manual_name);
        macLayout = view.findViewById(R.id.add_manual_mac);

    }
}
