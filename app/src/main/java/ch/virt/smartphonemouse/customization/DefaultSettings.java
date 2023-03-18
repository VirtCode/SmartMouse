package ch.virt.smartphonemouse.customization;

import android.content.SharedPreferences;
import ch.virt.smartphonemouse.mouse.Parameters;

/**
 * This class is used for restoring the settings to their factory defaults.
 */
public class DefaultSettings {

    /**
     * Checks whether the preferences have once been populated.
     * If not, they get initialized to the default settings.
     *
     * @param preferences preferences to check in
     */
    public static void check(SharedPreferences preferences) {
        if (!preferences.getBoolean("populated", false)) set(preferences);
    }

    /**
     * Overwrites the settings to the defaults
     *
     * @param preferences preferences to write in
     */
    public static void set(SharedPreferences preferences) {
        SharedPreferences.Editor edit = preferences.edit();

        edit.putBoolean("populated", true);
        edit.putBoolean("showUsage", true);
        edit.putBoolean("debugEnabled", false);

        edit.apply();

        defaultInterface(preferences);
        defaultMovement(preferences);
        defaultCommunication(preferences);
    }

    /**
     * Writes the default interface settings
     *
     * @param preferences preferences to write in
     */
    private static void defaultInterface(SharedPreferences preferences) {
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

    /**
     * Writes the default movement settings
     *
     * @param preferences preferences to write in
     */
    private static void defaultMovement(SharedPreferences preferences) {
        new Parameters(preferences).reset();
    }

    /**
     * Writes the default communication settings
     *
     * @param preferences preferences to write in
     */
    private static void defaultCommunication(SharedPreferences preferences) {
        SharedPreferences.Editor edit = preferences.edit();

        edit.putInt("communicationTransmissionRate", 100);

        edit.apply();
    }
}
