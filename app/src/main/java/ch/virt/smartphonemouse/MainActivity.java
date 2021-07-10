package ch.virt.smartphonemouse;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import ch.virt.smartphonemouse.customization.DefaultSettings;
import ch.virt.smartphonemouse.mouse.MouseInputs;
import ch.virt.smartphonemouse.mouse.MovementHandler;
import ch.virt.smartphonemouse.transmission.BluetoothHandler;
import ch.virt.smartphonemouse.ui.ConnectFragment;
import ch.virt.smartphonemouse.ui.CustomFragment;
import ch.virt.smartphonemouse.ui.HomeFragment;
import ch.virt.smartphonemouse.helper.MainContext;
import ch.virt.smartphonemouse.helper.ResultListener;
import ch.virt.smartphonemouse.ui.MouseFragment;
import ch.virt.smartphonemouse.ui.SettingsFragment;
import ch.virt.smartphonemouse.ui.mouse.MouseCalibrateDialog;
import ch.virt.smartphonemouse.ui.settings.CustomSettingsFragment;
import ch.virt.smartphonemouse.ui.settings.SettingsMovementSubfragment;
import ch.virt.smartphonemouse.ui.settings.dialog.CalibrateDialog;

public class MainActivity extends AppCompatActivity implements PreferenceFragmentCompat.OnPreferenceStartFragmentCallback {

    private MaterialToolbar bar;
    private DrawerLayout drawerLayout;
    private NavigationView drawer;

    private MainContext mainContext;

    private BluetoothHandler bluetooth;

    private MovementHandler movement;
    private MouseInputs inputs;

    private boolean mouseActive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DefaultSettings.check(PreferenceManager.getDefaultSharedPreferences(this));

        loadComponents();
        setupNavigation();

        loadContent();

        navigate(R.id.drawer_home);
        drawer.setCheckedItem(R.id.drawer_home);

    }


    @Override
    protected void onStart() {
        super.onStart();

        bluetooth.reInit();
    }

    /**
     * Loads the contents of the app
     */
    private void loadContent() {
        mainContext = new MainContext() {
            @Override
            public void exitApp() {
                finish();
            }

            @Override
            public void navigate(int element) {
                MainActivity.this.navigate(element);
            }

            @Override
            public ActivityResultLauncher<Intent> registerActivityForResult(ResultListener listener) {
                return registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> listener.result(result.getResultCode()));
            }

            @Override
            public void toast(String content, int duration) {
                Toast.makeText(MainActivity.this, content, duration).show();
            }

            @Override
            public void snack(String content, int duration) {
                Snackbar.make(MainActivity.this, MainActivity.this.findViewById(R.id.container), content, duration).show();
            }

            @Override
            public Resources getResources() {
                return MainActivity.this.getResources();
            }

            @Override
            public Context getContext() {
                return MainActivity.this;
            }

            @Override
            public void refresh() {
                reRender();
            }

            @Override
            public void registerReceiver(BroadcastReceiver receiver, IntentFilter filter) {
                MainActivity.this.registerReceiver(receiver, filter);
            }

            @Override
            public void unregisterReceiver(BroadcastReceiver receiver) {
                MainActivity.this.unregisterReceiver(receiver);
            }

            @Override
            public SharedPreferences getPreferences() {
                return PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
            }
        };

        bluetooth = new BluetoothHandler(mainContext);

        inputs = new MouseInputs(bluetooth, mainContext);

        movement = new MovementHandler(mainContext, inputs);
    }

    /**
     * Switches the Fragment displayed on the app
     *
     * @param fragment fragment that is displayed
     * @param stack whether that fragment should be added to the back stack
     */
    private void switchFragment(Fragment fragment, boolean stack) {
        Fragment current = getCurrentFragment();
        if (current instanceof CustomFragment) ((CustomFragment) current).restore();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (stack) transaction.addToBackStack(null);
        transaction.setReorderingAllowed(true);
        transaction.replace(R.id.container, fragment);

        transaction.commit();

    }

    /**
     * Loads the UI Components into their variables
     */
    private void loadComponents() {
        drawerLayout = findViewById(R.id.drawer_layout);
        drawer = findViewById(R.id.drawer);
        bar = findViewById(R.id.bar);
    }

    /**
     * Sets the navigation up so it is ready to be used
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
    }

    /**
     * Rerenders the current fragment
     */
    public void reRender() {
        if (getCurrentFragment() != null) {
            if (getCurrentFragment() instanceof CustomFragment) this.runOnUiThread(() -> ((CustomFragment) getCurrentFragment()).render());
        }
    }

    /**
     * Navigates to the respective sites
     *
     * @param entry entry to navigate to
     * @return whether that entry is navigated
     */
    private boolean navigate(int entry) {
        if (entry == R.id.drawer_mouse){

            if (!mainContext.getPreferences().getBoolean("movementSamplingCalibrated", false)) { // Make sure that the sampling rate is calibrated

                MouseCalibrateDialog dialog = new MouseCalibrateDialog(this.mainContext);
                dialog.show(getSupportFragmentManager(), null);

                return true;
            }

            bar.setVisibility(View.GONE);
            switchFragment(new MouseFragment(mainContext, inputs, movement), false);

            mouseActive = true;

            movement.create();
            movement.register();
            inputs.start();

        }else {
            bar.setVisibility(View.VISIBLE);

            if (mouseActive) {
                movement.unregister();
                inputs.stop();
                mouseActive = false;
            }

            switch (entry) {
                case R.id.drawer_connect:

                    switchFragment(new ConnectFragment(mainContext, bluetooth), false);
                    bar.setTitle(R.string.title_connect);

                    break;

                case R.id.drawer_home:

                    switchFragment(new HomeFragment(bluetooth, mainContext), false);
                    bar.setTitle(R.string.title_home);

                    break;

                case R.id.drawer_settings:

                    switchFragment(new SettingsFragment(mainContext), false);
                    bar.setTitle(R.string.title_settings);

                    break;

                default:
                    Toast.makeText(this, "Not yet implemented!", Toast.LENGTH_SHORT).show();
                    return false;
            }
        }

        drawer.setCheckedItem(entry);
        return true;
    }

    @Override
    public boolean onPreferenceStartFragment(PreferenceFragmentCompat caller, Preference pref) {
        final Bundle args = pref.getExtras();
        final Fragment fragment = getSupportFragmentManager().getFragmentFactory().instantiate(
                getClassLoader(),
                pref.getFragment());
        fragment.setArguments(args);
        fragment.setTargetFragment(caller, 0);

        if (fragment instanceof CustomSettingsFragment) ((CustomSettingsFragment) fragment).setMain(mainContext);

        switchFragment(fragment, true);

        return true;
    }

    @Nullable
    private Fragment getCurrentFragment(){
        return getSupportFragmentManager().findFragmentById(R.id.container);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) super.onBackPressed(); // If something is on the backstack proceed
        else {

            if (!(getCurrentFragment() instanceof HomeFragment)) { // Navigate to home if not in sub fragment and not in home
                if (!(getCurrentFragment() instanceof MouseFragment)) navigate(R.id.drawer_home); // Make exception for mouse fragment
            }

            else super.onBackPressed();
        }
    }
}