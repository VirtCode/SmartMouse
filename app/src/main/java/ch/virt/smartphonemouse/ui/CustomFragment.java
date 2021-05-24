package ch.virt.smartphonemouse.ui;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import ch.virt.smartphonemouse.helper.MainContext;

public abstract class CustomFragment extends Fragment {

    protected MainContext main;

    public CustomFragment(int contentLayoutId, MainContext context) {
        super(contentLayoutId);
        this.main = context;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loadComponents(view);

        render();
    }

    public abstract void render();

    protected abstract void loadComponents(View view);
}
