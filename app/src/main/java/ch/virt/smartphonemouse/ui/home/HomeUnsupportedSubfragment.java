package ch.virt.smartphonemouse.ui.home;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import ch.virt.smartphonemouse.R;

/**
 * This sub fragment of the home page is shown when the smartphone does not support the bluetooth id profile.
 */
public class HomeUnsupportedSubfragment extends Fragment {

    private Button playstoreLink;

    /**
     * Creates this sub fragment.
     */
    public HomeUnsupportedSubfragment() {
        super(R.layout.subfragment_home_unsupported);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        playstoreLink = view.findViewById(R.id.home_unsupported_playstore);
        playstoreLink.setOnClickListener(v -> Toast.makeText(view.getContext(), "Currently not published", Toast.LENGTH_SHORT).show());
    }
}
