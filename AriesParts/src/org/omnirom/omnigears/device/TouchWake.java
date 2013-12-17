package org.omnirom.omnigears.device;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceManager;

public class TouchWake implements OnPreferenceChangeListener {

    /**
     * Set location of TouchWake activity parameter
     */
    private static final String FILE_PATH = "/sys/devices/virtual/misc/touchwake/enabled";

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
        int value = sharedPrefs.getBoolean(DeviceSettings.KEY_TOUCHWAKEACTIVE, false) ? 1 : 0;
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
