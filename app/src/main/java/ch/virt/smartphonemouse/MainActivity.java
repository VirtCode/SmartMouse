package ch.virt.smartphonemouse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

import ch.virt.smartphonemouse.fragments.ConnectFragment;
import ch.virt.smartphonemouse.fragments.HomeFragment;

public class MainActivity extends AppCompatActivity {

    private MaterialToolbar bar;
    private DrawerLayout drawerLayout;
    private NavigationView drawer;

    private Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadComponents();
        setupNavigation();

        navigate(R.id.drawer_home);
        drawer.setCheckedItem(R.id.drawer_home);
    }

    /**
     * Switches the Fragment displayed on the app
     * @param fragment fragment that is displayed
     */
    private void switchFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction().setReorderingAllowed(true);

        if (currentFragment != null) transaction.remove(currentFragment);

        transaction.add(R.id.container, fragment).commit();

        currentFragment = fragment;
    }

    /**
     * Loads the UI Components into their variables
     */
    private void loadComponents(){
        drawerLayout = findViewById(R.id.drawer_layout);
        drawer = findViewById(R.id.drawer);
        bar = findViewById(R.id.bar);
    }

    /**
     * Sets the navigation up so it is ready to be used
     */
    private void setupNavigation(){
        bar.setNavigationOnClickListener(v -> drawerLayout.open());

        drawer.setNavigationItemSelectedListener(item -> {
            if (navigate(item.getItemId())){
                drawerLayout.close();
                item.setChecked(true);
                return true;
            }

            return false;
        });
    }

    /**
     * Navigates to the respective sites
     * @param entry entry to navigate to
     * @return whether that entry is navigated
     */
    private boolean navigate(int entry){
        switch (entry) {
            case R.id.drawer_connect:
                switchFragment(new ConnectFragment());
                bar.setTitle(R.string.title_connect);
                return true;
            case R.id.drawer_home:
                switchFragment(new HomeFragment());
                bar.setTitle(R.string.title_home);
                return true;

            default:
                Toast.makeText(this, "Not yet implemented!", Toast.LENGTH_SHORT).show();
                return false;
        }
    }
}