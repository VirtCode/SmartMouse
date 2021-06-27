package ch.virt.smartphonemouse;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

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

import ch.virt.smartphonemouse.mouse.MouseInputs;
import ch.virt.smartphonemouse.mouse.MovementHandler;
import ch.virt.smartphonemouse.transmission.BluetoothHandler;
import ch.virt.smartphonemouse.ui.ConnectFragment;
import ch.virt.smartphonemouse.ui.CustomFragment;
import ch.virt.smartphonemouse.ui.HomeFragment;
import ch.virt.smartphonemouse.helper.MainContext;
import ch.virt.smartphonemouse.helper.ResultListener;
import ch.virt.smartphonemouse.ui.MouseFragment;

public class MainActivity extends AppCompatActivity {

    private MaterialToolbar bar;
    private DrawerLayout drawerLayout;
    private NavigationView drawer;

    private CustomFragment currentFragment;
    private MainContext mainContext;

    private BluetoothHandler bluetooth;

    private MovementHandler movement;
    private MouseInputs inputs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                return MainActivity.this.getSharedPreferences("smartphonemouse", Context.MODE_PRIVATE);
            }
        };

        bluetooth = new BluetoothHandler(mainContext);

        inputs = new MouseInputs(bluetooth);

        movement = new MovementHandler(mainContext, inputs);
    }

    /**
     * Switches the Fragment displayed on the app
     *
     * @param fragment fragment that is displayed
     */
    private void switchFragment(CustomFragment fragment) {
        getSupportFragmentManager().beginTransaction().setReorderingAllowed(true).replace(R.id.container, fragment).commit();
        currentFragment = fragment;
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
                return true;
            }

            return false;
        });
    }

    /**
     * Rerenders the current fragment
     */
    public void reRender() {
        if (currentFragment != null) this.runOnUiThread(() -> currentFragment.render());
    }

    /**
     * Navigates to the respective sites
     *
     * @param entry entry to navigate to
     * @return whether that entry is navigated
     */
    private boolean navigate(int entry) {
        switch (entry) {
            case R.id.drawer_connect:
                switchFragment(new ConnectFragment(mainContext, bluetooth));
                bar.setVisibility(View.VISIBLE);
                bar.setTitle(R.string.title_connect);
                drawer.setCheckedItem(entry);
                return true;
            case R.id.drawer_home:
                switchFragment(new HomeFragment(bluetooth, mainContext));
                bar.setVisibility(View.VISIBLE);
                bar.setTitle(R.string.title_home);
                drawer.setCheckedItem(entry);
                return true;

            case R.id.drawer_mouse:
                switchFragment(new MouseFragment(mainContext, inputs));
                drawer.setCheckedItem(entry);
                bar.setVisibility(View.GONE);
                movement.register();
                inputs.start();
                return true;

            case R.id.drawer_settings:

            default:
                Toast.makeText(this, "Not yet implemented!", Toast.LENGTH_SHORT).show();
                return false;
        }
    }
}