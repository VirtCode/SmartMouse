package ch.virt.smartphonemouse.ui;

import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import ch.virt.smartphonemouse.R;
import ch.virt.smartphonemouse.helper.MainContext;

public class AboutFragment extends CustomFragment{

    TextView aboutGithub;

    public AboutFragment(MainContext context) {
        super(R.layout.fragment_about, context);
    }

    @Override
    public void render() {

    }

    @Override
    protected void initComponents() {
        aboutGithub.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    protected void loadComponents(View view) {
        aboutGithub = view.findViewById(R.id.about_github);
    }
}
