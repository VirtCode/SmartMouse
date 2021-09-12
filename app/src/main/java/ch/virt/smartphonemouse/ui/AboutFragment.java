package ch.virt.smartphonemouse.ui;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import ch.virt.smartphonemouse.R;

/**
 * This fragment shows information about the app in general.
 */
public class AboutFragment extends Fragment {

    private TextView aboutGithub;

    /**
     * Creates the fragment.
     */
    public AboutFragment() {
        super(R.layout.fragment_about);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        aboutGithub = view.findViewById(R.id.about_github);
        aboutGithub.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
