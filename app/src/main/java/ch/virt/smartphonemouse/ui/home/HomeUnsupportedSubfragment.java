package ch.virt.smartphonemouse.ui.home;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import ch.virt.smartphonemouse.R;

/**
 * @author VirtCode
 * @version 1.0
 */
public class HomeUnsupportedSubfragment extends Fragment {

    private Button playstoreLink;

    public HomeUnsupportedSubfragment(){
        super(R.layout.subfragment_home_unsupported);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        playstoreLink = view.findViewById(R.id.home_unsupported_playstore);
        playstoreLink.setOnClickListener(v -> Toast.makeText(view.getContext(), "Not yet published", Toast.LENGTH_SHORT).show());
    }
}
