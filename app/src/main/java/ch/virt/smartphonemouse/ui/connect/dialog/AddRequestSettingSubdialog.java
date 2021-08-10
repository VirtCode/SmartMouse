package ch.virt.smartphonemouse.ui.connect.dialog;

import android.view.View;
import android.widget.TextView;

import ch.virt.smartphonemouse.R;
import ch.virt.smartphonemouse.helper.MainContext;
import ch.virt.smartphonemouse.ui.CustomFragment;

public class AddRequestSettingSubdialog extends CustomFragment {

    TextView error;

    public AddRequestSettingSubdialog(MainContext context) {
        super(R.layout.subdialog_add_requestsetting, context);
    }

    public void showError(){
        error.post(() -> error.setVisibility(View.VISIBLE));
    }

    @Override
    public void render() {

    }

    @Override
    protected void loadComponents(View view) {
        error = view.findViewById(R.id.add_request_setting_error);
    }
}
