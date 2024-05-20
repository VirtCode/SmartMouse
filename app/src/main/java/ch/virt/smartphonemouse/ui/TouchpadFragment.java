package ch.virt.smartphonemouse.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.RelativeLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import ch.virt.smartphonemouse.R;
import ch.virt.smartphonemouse.mouse.MouseInputs;
import ch.virt.smartphonemouse.mouse.MovementHandler;

import java.util.Arrays;

public class TouchpadFragment extends Fragment {

    private static final String TAG = "TouchpadFragment";

    final MouseInputs mouse;
    public boolean running = true;

    RelativeLayout root;

    public TouchpadFragment(MouseInputs mouse) {
        super(R.layout.fragment_mouse);

        this.mouse = mouse;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        root = view.findViewById(R.id.mouse_root);
        root.setBackgroundResource(R.color.mouse_background_dark);

        root.setOnTouchListener((v, event) -> viewTouched(event));

        Thread thread = new Thread(() -> {
            while (running) {
                mouse.touchpad(down, x, y);
                try {
                    // 100Hz
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        thread.start();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.mouse_background_dark));
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.design_default_color_primary_dark));
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        this.running = false;

        super.onDestroyView();
    }

    int[] id = {-1, -1};
    final boolean[] down = new boolean[2];
    final int[] x = new int[2];
    final int[] y = new int[2];

    public boolean viewTouched(MotionEvent event) {

        for (int index = 0; index < event.getPointerCount(); index++) {
            int number = -1;

            // get local index
            if (event.getActionIndex() == index && ((event.getActionMasked() == MotionEvent.ACTION_DOWN) || (event.getActionMasked() == MotionEvent.ACTION_POINTER_DOWN))) {
                //Log.i("ARRAYS", "Adding");
                for (int i = 0; i < id.length; i++)
                    if (id[i] == -1) number = i;
            } else {
                //Log.i("ARRAYS", "Finding");
                for (int i = 0; i < id.length; i++)
                    if (id[i] == event.getPointerId(index)) number = i;
            }

            if (number == -1) return true;
            id[number] = event.getPointerId(index);

            // remove removed pointer
            if (event.getActionIndex() == index && ((event.getActionMasked() == MotionEvent.ACTION_UP) || (event.getActionMasked() == MotionEvent.ACTION_POINTER_UP))) {
                //Log.i("ARRAYS", "Removing");
                id[number] = -1;
            }

            down[number] = id[number] != -1;
            y[number] = 2048 - (int) (event.getX(index) / root.getWidth() * 2048);
            x[number] = (int) (event.getY(index) / root.getHeight() * 4096);
        }

        //Log.i("ARRAYS", "id: " + Arrays.toString(id));
        //Log.i("ARRAYS", "down: " + Arrays.toString(down));
        //Log.i("ARRAYS", "x: " + Arrays.toString(x));
        //Log.i("ARRAYS", "y: " + Arrays.toString(y));

        return true;
    }
}
