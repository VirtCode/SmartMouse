package ch.virt.smartphonemouse.ui.debug;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.WindowManager;

import androidx.fragment.app.DialogFragment;

public class SideDialogFragment extends DialogFragment {

    @Override
    public void onResume() {
        super.onResume();

        DisplayMetrics metrics = new DisplayMetrics();
        getDialog().getWindow().getWindowManager().getDefaultDisplay().getMetrics(metrics);

        WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
        params.gravity = Gravity.END;
        params.width = (int) (metrics.widthPixels * 0.4f);
        params.height = MATCH_PARENT;
        params.horizontalMargin = 0;
        params.verticalMargin = 0;
        getDialog().getWindow().setAttributes(params);

    }

}
