package com.cyanogenmod.settings.device;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceManager;

public class Navigation implements OnPreferenceChangeListener {

    /**
     * Set location of navigation parameter
     */
    private static final String FILE_PATH = "/data/local/mackay/navbar";

    /**
     * Check whether the kernel supports this specific parameter
     */
    public static boolean isSupported() {
        boolean supported = true;
        if (!Utils.fileExists(FILE_PATH)) {
            supported = false;
        }
        return supported;
    }

    /**
     * Restore settings from SharedPreferences.
     */
    public static void restore(Context context) {
        if (!isSupported()) {
            return;
        }

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        int value = sharedPrefs.getBoolean(DeviceSettings.KEY_NAVBAR, false) ? 1 : 0;
        Utils.writeValue(FILE_PATH, String.valueOf(value));

    }

    /**
     * Change parameter if checkbox is checked.
     */
    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        Utils.writeValue(FILE_PATH, ((CheckBoxPreference)preference).isChecked() ? "0" : "1");
        return true;
    }

}
