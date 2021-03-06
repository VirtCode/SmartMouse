package ch.virt.smartphonemouse.customization;

import android.content.SharedPreferences;

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
        SharedPreferences.Editor edit = preferences.edit();

        edit.putFloat("movementSensitivity", 13);

        edit.putBoolean("movementScaleEnable", true);

        edit.putBoolean("movementSamplingCalibrated", false);
        edit.putInt("movementSamplingRealRate", 200);

        edit.putInt("movementLowPassOrder", 1);
        edit.putFloat("movementLowPassCutoff", 0.05f);

        edit.putFloat("movementFreezerFreezingThreshold", 0.1f);
        edit.putFloat("movementFreezerUnfreezingThreshold", 0.04f);
        edit.putInt("movementFreezerUnfreezingSamples", 10);

        edit.putFloat("movementNoiseThreshold", 0.04f);
        edit.putInt("movementNoiseResetSamples", 20);

        edit.putInt("movementCacheDurationMinimal", 5);
        edit.putInt("movementCacheDurationMaximal", 10);
        edit.putFloat("movementCacheReleaseThreshold", 0.05f);

        edit.putInt("movementScalePower", 2);
        edit.putFloat("movementScaleSplit", 0.1f);

        edit.apply();
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
