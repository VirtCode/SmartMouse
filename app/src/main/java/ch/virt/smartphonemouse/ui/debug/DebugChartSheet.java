package ch.virt.smartphonemouse.ui.debug;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import ch.virt.smartphonemouse.R;

public class DebugChartSheet extends DialogFragment {

    Dialog dialog;
    View root;

    @Override
    public void onResume() {
        super.onResume();

        DisplayMetrics metrics = new DisplayMetrics();
        dialog.getWindow().getWindowManager().getDefaultDisplay().getMetrics(metrics);

        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.gravity = Gravity.END;
        params.width = (int) (metrics.widthPixels * 0.4f);
        params.height = MATCH_PARENT;
        params.horizontalMargin = 0;
        params.verticalMargin = 0;
        dialog.getWindow().setAttributes(params);

    }

    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        root = inflater.inflate(R.layout.sheet_debug_chart, null);

        builder.setView(root)
                .setNegativeButton(R.string.dialog_debug_sheet_done, null);

        dialog = builder.create();

        return dialog;
    }
}
