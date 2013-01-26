package com.cyanogenmod.settings.device;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceManager;

public class Charge implements OnPreferenceChangeListener {

    /**
     * Set location of sysfs parameter
     */
    private static final String FILE_PATH = "/sys/kernel/fast_charge/force_fast_charge";

    /**
     * Check whether the kernel supports this specific sysfs parameter
     */
    public static boolean isSupported() {
        boolean supported = true;
        if (!Utils.fileExists(FILE_PATH)) {
            supported = false;
        }
        return supported;
    }

    /**
     * Restore sysfs settings from SharedPreferences (write to sysfs).
     */
    public static void restore(Context context) {
        if (!isSupported()) {
            return;
        }

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        int value = sharedPrefs.getBoolean(DeviceSettings.KEY_FAST_CHARGE, false) ? 1 : 0;
        Utils.writeValue(FILE_PATH, String.valueOf(value));

    }

    /**
     * Change sysfs setting if checkbox is checked.
     */
    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        Utils.writeValue(FILE_PATH, ((CheckBoxPreference)preference).isChecked() ? "0" : "1");
        return true;
    }

}
