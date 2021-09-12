package ch.virt.smartphonemouse.ui.mouse;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import ch.virt.smartphonemouse.R;

/**
 * This dialog is shown when the user opens the mouse fragment. It tells the user how to use the mouse.
 */
public class MouseUsageDialog extends DialogFragment {

    private AlertDialog dialog;
    private Button positiveButton;

    private int index;

    private UsageFinishedListener finishedListener;


    public MouseUsageDialog(UsageFinishedListener finishedListener) {
        this.finishedListener = finishedListener;
    }

    /**
     * Returns a message to show the user at the given index.
     *
     * @param index index of the message
     * @return resource string id of the message
     */
    private int getCurrentMessage(int index) {

        switch (index) {
            case 0:
                return R.string.dialog_mouse_usage_even;
            case 1:
                return R.string.dialog_mouse_usage_move;
            case 2:
                return R.string.dialog_mouse_usage_buttons;
            case 3:
                return R.string.dialog_mouse_usage_return;
            default:
                return 0;
        }
    }

    /**
     * Returns how many messages can be displayed.
     *
     * @return amount of messages
     */
    private int getMessageAmount() {
        return 4;
    }


    /**
     * Is called when the dialog is created.
     */
    private void create() {
        showFragment();

        dialog.setCanceledOnTouchOutside(false);
    }


    /**
     * Is called when the user skips to the next page.
     */
    private void next() {
        if (index == getMessageAmount()) {
            dismiss();
            return;
        }

        index++;
        if (index == getMessageAmount()) {

            dialog.setCanceledOnTouchOutside(true);
            setFragment(new MouseUsageFinishedSubdialog());

        } else showFragment();

    }

    /**
     * Shows a message fragment with the current index.
     */
    private void showFragment() {
        setFragment(new MouseMessageSubdialog(getResources().getString(getCurrentMessage(index))));
    }

    /**
     * Displays a fragment in the respective fragment container.
     *
     * @param fragment fragment to display
     */
    private void setFragment(Fragment fragment) {
        getChildFragmentManager().beginTransaction().setReorderingAllowed(true).replace(R.id.mouse_container, fragment).commit();
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.dialog_mouse, null))
                .setPositiveButton(R.string.dialog_mouse_usage_next, null);

        dialog = builder.create();
        dialog.setTitle(R.string.dialog_mouse_usage_title);
        dialog.setOnShowListener(dialogInterface -> {
            positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(view -> next());

            create();
        });

        return dialog;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        finishedListener.finished();

        super.onDismiss(dialog);
    }


    /**
     * This interface is a basic listener for when the user has finished reading the instructions.
     */
    public interface UsageFinishedListener {

        /**
         * Called when the user is finished.
         */
        void finished();
    }
}
