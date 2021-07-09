package ch.virt.smartphonemouse;

import android.content.SharedPreferences;

public class DefaultSettings {

    public static void check(SharedPreferences preferences){
        if (!preferences.getBoolean("populated", false)) set(preferences);
    }

    public static void set(SharedPreferences preferences){
        SharedPreferences.Editor edit = preferences.edit();
        edit.putBoolean("populated", true);
        edit.apply();

        defaultInterface(preferences);
    }

    private static void defaultInterface(SharedPreferences preferences){
        SharedPreferences.Editor edit = preferences.edit();

        edit.putString("interfaceTheme", "dark");

        edit.putInt("interfaceBehaviourScrollStep", 50);
        edit.putInt("interfaceBehaviourSpecialWait", 300);

        edit.putBoolean("interfaceVisualsEnable", true);
        edit.putInt("interfaceVisualsStrokeWeight", 4);
        edit.putFloat("interfaceVisualsIntensity", 0.5f);

        edit.putBoolean("interfaceVibrationsEnable", true);
        edit.putInt("interfaceVibrationsButtonIntensity", 100);
        edit.putInt("interfaceVibrationsButtonLength", 30);
        edit.putInt("interfaceVibrationsScrollIntensity", 50);
        edit.putInt("interfaceVibrationsScrollLength", 20);
        edit.putInt("interfaceVibrationsSpecialIntensity", 100);
        edit.putInt("interfaceVibrationsSpecialLength", 50);

        edit.putFloat("interfaceLayoutHeight", 0.3f);
        edit.putFloat("interfaceLayoutMiddleWidth", 0.2f);

        edit.apply();
    }

}
