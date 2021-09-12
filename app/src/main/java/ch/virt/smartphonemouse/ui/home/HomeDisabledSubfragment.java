package ch.virt.smartphonemouse.ui.home;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import ch.virt.smartphonemouse.R;
import ch.virt.smartphonemouse.transmission.BluetoothHandler;

/**
 * This sub fragment on the home page gets shown when bluetooth happens to be disabled.
 */
public class HomeDisabledSubfragment extends Fragment {

    private final BluetoothHandler handler;

    private Button refresh;

    /**
     * Creates the sub fragment.
     *
     * @param handler bluetooth handler to use to refresh
     */
    public HomeDisabledSubfragment(BluetoothHandler handler) {
        super(R.layout.subfragment_home_disabled);
        this.handler = handler;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        refresh = view.findViewById(R.id.home_disabled_recheck);

        refresh.setOnClickListener(v -> handler.reInit());
    }
}
