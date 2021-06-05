package ch.virt.smartphonemouse.helper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;

import androidx.activity.result.ActivityResultLauncher;

public interface MainContext {
    void exitApp();
    void navigate(int element);
    ActivityResultLauncher<Intent> registerActivityForResult(ResultListener result);
    void toast(String content, int duration);
    void snack(String content, int duration);
    Resources getResources();
    Context getContext();
    void refresh();
    void registerReceiver(BroadcastReceiver receiver, IntentFilter filter);
    void unregisterReceiver(BroadcastReceiver receiver);

}
