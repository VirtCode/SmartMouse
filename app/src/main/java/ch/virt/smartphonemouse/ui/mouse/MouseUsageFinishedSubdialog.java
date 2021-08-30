package ch.virt.smartphonemouse.ui.mouse;

import android.view.View;
import android.widget.CheckBox;

import androidx.preference.PreferenceManager;

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

        notAgain.setChecked(!PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean("showUsage", true));

        notAgain.setOnCheckedChangeListener((buttonView, isChecked) -> {
            PreferenceManager.getDefaultSharedPreferences(getContext()).edit().putBoolean("showUsage", !isChecked).apply();
        });

    }

    @Override
    protected void loadComponents(View view) {
        notAgain = view.findViewById(R.id.mouse_usage_finished_notagain);
    }
}
