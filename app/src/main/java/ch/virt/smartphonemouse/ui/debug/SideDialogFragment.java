package ch.virt.smartphonemouse.ui.debug;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.WindowManager;

import androidx.fragment.app.DialogFragment;

/**
 * This class configures a dialog fragment as a dialog that appears on the side of the screen.
 * This dialog is similar to a side sheet from material design, but since it is not implemented by their library, this is used.
 */
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
