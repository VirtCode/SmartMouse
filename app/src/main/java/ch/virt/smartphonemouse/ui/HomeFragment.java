package ch.virt.smartphonemouse.ui;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import ch.virt.smartphonemouse.R;
import ch.virt.smartphonemouse.transmission.BluetoothHandler;
import ch.virt.smartphonemouse.ui.home.HomeConnectedSubfragment;
import ch.virt.smartphonemouse.ui.home.HomeUnsupportedSubfragment;

public class HomeFragment extends Fragment {

    private BluetoothHandler bluetooth;
    private MainContext mainContext;

    private ImageView status;
    private TextView statusText;

    private Button button;

    public HomeFragment(BluetoothHandler bluetooth, MainContext mainContext) {
        super(R.layout.fragment_home);

        this.bluetooth = bluetooth;
        this.mainContext = mainContext;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loadComponents(view);

        if (!bluetooth.isSupported()) unsupported();
        else {
            if (bluetooth.isConnected()) connected();
            else disconnected();
        }
    }

    private void loadComponents(View view){
        status = view.findViewById(R.id.home_status);
        statusText = view.findViewById(R.id.home_status_text);
        button = view.findViewById(R.id.home_button);
    }

    private void unsupported(){
        ((GradientDrawable) status.getBackground()).setColor(getResources().getColor(R.color.home_status_unsupported, null));
        statusText.setText(R.string.home_status_unsupported);

        button.setEnabled(true);
        button.setText(R.string.home_button_unsupported);

        button.setOnClickListener(v -> mainContext.exitApp());

        getChildFragmentManager().beginTransaction().setReorderingAllowed(true).add(R.id.home_container, new HomeUnsupportedSubfragment()).commit();
    }

    private void disconnected(){
        ((GradientDrawable) status.getBackground()).setColor(getResources().getColor(R.color.home_status_disconnected, null));
        statusText.setText(R.string.home_status_disconnected);

        button.setEnabled(true);
        button.setText(R.string.home_button_disconnected);

        button.setOnClickListener(v -> mainContext.navigate(R.id.drawer_connect));
    }

    private void connected(){
        ((GradientDrawable) status.getBackground()).setColor(getResources().getColor(R.color.home_status_connected, null));
        statusText.setText(R.string.home_status_connected);

        button.setEnabled(true);
        button.setText(R.string.home_button_connected);

        button.setOnClickListener(v -> mainContext.navigate(R.id.drawer_mouse));

        getChildFragmentManager().beginTransaction().setReorderingAllowed(true).add(R.id.home_container, new HomeConnectedSubfragment(bluetooth)).commit();
    }
}
