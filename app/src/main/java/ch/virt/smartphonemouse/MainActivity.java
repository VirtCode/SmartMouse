package ch.virt.smartphonemouse;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import ch.virt.smartphonemouse.customization.DefaultSettings;
import ch.virt.smartphonemouse.mouse.MouseInputs;
import ch.virt.smartphonemouse.mouse.MovementHandler;
import ch.virt.smartphonemouse.transmission.BluetoothHandler;
import ch.virt.smartphonemouse.transmission.DebugTransmitter;
import ch.virt.smartphonemouse.ui.*;
import ch.virt.smartphonemouse.ui.mouse.MouseCalibrateDialog;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

/**
 * This class is the main activity of this app.
 */
public class MainActivity extends AppCompatActivity implements PreferenceFragmentCompat.OnPreferenceStartFragmentCallback {

    private MaterialToolbar bar;
    private DrawerLayout drawerLayout;
    private NavigationView drawer;

    private BluetoothHandler bluetooth;

    private MovementHandler movement;
    private MouseInputs inputs;

    private DebugTransmitter debug;

    private boolean mouseActive;

    private boolean instanceSaved = false; // Used to avoid ui changes if the activity is not rendered.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DefaultSettings.check(PreferenceManager.getDefaultSharedPreferences(this)); // Check whether new settings must be restored

        loadComponents();
        setupNavigation();

        loadContent();

        navigate(R.id.drawer_home);
        drawer.setCheckedItem(R.id.drawer_home);
    }


    /**
     * Loads the components into their variables.
     */
    private void loadComponents() {
        drawerLayout = findViewById(R.id.drawer_layout);
        drawer = findViewById(R.id.drawer);
        bar = findViewById(R.id.bar);
    }

    /**
     * Sets the navigation up so it is ready to be used.
     */
    private void setupNavigation() {
        bar.setNavigationOnClickListener(v -> drawerLayout.open());

        drawer.setNavigationItemSelectedListener(item -> {
            if (navigate(item.getItemId())) {
                drawerLayout.close();
                return false;
            }

            return false;
        });

        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                checkNavItems();
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
            }

            @Override
            public void onDrawerStateChanged(int newState) {
            }
        });
    }

    /**
     * Sets the correct state of the nav items in the nav drawer.
     */
    private void checkNavItems() {
        if (bluetooth.isSupported()) {
            setNavItemEnable(R.id.drawer_connect, true);
            setNavItemEnable(R.id.drawer_mouse, bluetooth.isConnected());
        } else {
            setNavItemEnable(R.id.drawer_connect, false);
            setNavItemEnable(R.id.drawer_mouse, false);
        }

        drawer.getMenu().findItem(R.id.drawer_debug).setVisible(PreferenceManager.getDefaultSharedPreferences(this).getBoolean("debugEnabled", false));
    }

    /**
     * Enables / disables a certain nav item.
     *
     * @param item   item to change
     * @param enable enabled state
     */
    private void setNavItemEnable(int item, boolean enable) {
        drawer.getMenu().findItem(item).setEnabled(enable);
    }


    /**
     * Loads the contents of the app
     */
    private void loadContent() {

        bluetooth = new BluetoothHandler(this);

        inputs = new MouseInputs(bluetooth, this);

        movement = new MovementHandler(this, inputs);
    }

    /**
     * Updates the bluetooth status.
     */
    public void updateBluetoothStatus() {
        if (instanceSaved || getCurrentFragment() == null) return;

        if (getCurrentFragment() instanceof ConnectFragment || getCurrentFragment() instanceof MouseFragment) {
            if (!bluetooth.isEnabled() || !bluetooth.isSupported()) navigate(R.id.drawer_home);
            else if (!bluetooth.isConnected() && getCurrentFragment() instanceof MouseFragment)
                navigate(R.id.drawer_connect);
        }

        this.runOnUiThread(() -> {
            if (getCurrentFragment() instanceof HomeFragment)
                ((HomeFragment) getCurrentFragment()).update();
            else if (getCurrentFragment() instanceof ConnectFragment)
                ((ConnectFragment) getCurrentFragment()).update();
        });
    }



    @Override
    protected void onStart() {
        super.onStart();
        instanceSaved = false;

        bluetooth.reInit();
        updateBluetoothStatus(); // Current status is unknown
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        instanceSaved = true;
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        instanceSaved = false;
        updateBluetoothStatus(); // Current status is unknown
    }


    
    /**
     * Navigates to the respective sites.
     *
     * @param entry entry to navigate to
     * @return whether that entry is navigated
     */
    public boolean navigate(int entry) {
        if (entry == R.id.drawer_mouse) {

            if (!PreferenceManager.getDefaultSharedPreferences(this).getBoolean("movementSamplingCalibrated", false)) { // Make sure that the sampling rate is calibrated

                MouseCalibrateDialog dialog = new MouseCalibrateDialog();
                dialog.show(getSupportFragmentManager(), null);

                return true;
            }

            bar.setVisibility(View.GONE);
            switchFragment(new MouseFragment(inputs, movement), false);

            mouseActive = true;

            debug = new DebugTransmitter(true, "192.168.1.226", 8003);
            movement.create(debug);
            debug.connect();

            debug.startTransmission();
            movement.register();
            inputs.start();

        } else {
            bar.setVisibility(View.VISIBLE);

            if (mouseActive) {
                movement.unregister();
                debug.endTransmission();
                inputs.stop();
                mouseActive = false;
            }

            switch (entry) {
                case R.id.drawer_connect:

                    switchFragment(new ConnectFragment(bluetooth), false);
                    bar.setTitle(R.string.title_connect);

                    break;

                case R.id.drawer_home:

                    switchFragment(new HomeFragment(bluetooth), false);
                    bar.setTitle(R.string.title_home);

                    break;

                case R.id.drawer_settings:

                    switchFragment(new SettingsFragment(), false);
                    bar.setTitle(R.string.title_settings);

                    break;

                case R.id.drawer_about:

                    switchFragment(new AboutFragment(), false);
                    bar.setTitle(R.string.title_about);

                    break;

                case R.id.drawer_debug:
                    switchFragment(new DebugFragment(), false);
                    bar.setTitle(R.string.title_debug);
                    bar.setVisibility(View.GONE);
                    break;

                default:
                    Toast.makeText(this, "Not yet implemented!", Toast.LENGTH_SHORT).show();
                    return false;
            }
        }

        drawer.setCheckedItem(entry);
        return true;
    }

    /**
     * Switches the Fragment displayed on the app.
     *
     * @param fragment fragment that is displayed
     * @param stack    whether that fragment should be added to the back stack
     */
    private void switchFragment(Fragment fragment, boolean stack) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (stack) transaction.addToBackStack(null);
        transaction.setReorderingAllowed(true);
        transaction.replace(R.id.container, fragment);

        transaction.commit();

    }

    /**
     * Returns the currently shown fragment.
     *
     * @return currently shown fragment
     */
    @Nullable
    private Fragment getCurrentFragment() {
        return getSupportFragmentManager().findFragmentById(R.id.container);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0)
            super.onBackPressed(); // If something is on the backstack proceed
        else {

            if (!(getCurrentFragment() instanceof HomeFragment)) { // Navigate to home if not in sub fragment and not in home
                if (!(getCurrentFragment() instanceof MouseFragment))
                    navigate(R.id.drawer_home); // Make exception for mouse fragment
            } else super.onBackPressed();
        }
    }


    @Override
    public boolean onPreferenceStartFragment(PreferenceFragmentCompat caller, Preference pref) {
        final Bundle args = pref.getExtras();
        final Fragment fragment = getSupportFragmentManager().getFragmentFactory().instantiate(
                getClassLoader(),
                pref.getFragment());
        fragment.setArguments(args);
        fragment.setTargetFragment(caller, 0);

        switchFragment(fragment, true);

        return true;
    }
}