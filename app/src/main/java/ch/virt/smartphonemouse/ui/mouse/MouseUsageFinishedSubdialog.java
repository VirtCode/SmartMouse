package ch.virt.smartphonemouse.ui.mouse;

import android.view.View;
import android.widget.CheckBox;

import ch.virt.smartphonemouse.R;
import ch.virt.smartphonemouse.helper.MainContext;
import ch.virt.smartphonemouse.ui.CustomFragment;

public class MouseUsageFinishedSubdialog extends CustomFragment {

    private CheckBox notAgain;

    public MouseUsageFinishedSubdialog(MainContext context) {
        super(R.layout.subdialog_mouse_usage_finished, context);
    }

    @Override
    public void render() {

    }

    @Override
    protected void initComponents() {

        notAgain.setChecked(!main.getPreferences().getBoolean("showUsage", true));

        notAgain.setOnCheckedChangeListener((buttonView, isChecked) -> {
            main.getPreferences().edit().putBoolean("showUsage", !isChecked).apply();
        });

    }

    @Override
    protected void loadComponents(View view) {
        notAgain = view.findViewById(R.id.mouse_usage_finished_notagain);
    }
}
