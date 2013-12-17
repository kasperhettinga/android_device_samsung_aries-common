package com.cyanogenmod.settings.device;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceManager;

public class DeviceSettings extends PreferenceActivity  {

    public static final String KEY_COLOR_TUNING = "color_tuning";
    public static final String KEY_MDNIE = "mdnie";
    public static final String KEY_BACKLIGHT_TIMEOUT = "backlight_timeout";
    public static final String KEY_BLN = "bln";
    public static final String KEY_LED_CATEGORY = "category_led";
    public static final String KEY_HSPA = "hspa";
    public static final String KEY_HSPA_CATEGORY = "category_radio";
    public static final String KEY_VOLUME_BOOST = "volume_boost";
    public static final String KEY_VOLUME_CATEGORY = "category_volume_boost";
    public static final String KEY_CARDOCK_AUDIO = "cardock_audio";
    public static final String KEY_DESKDOCK_AUDIO = "deskdock_audio";
    public static final String KEY_DOCK_AUDIO_CATEGORY = "category_dock_audio";
    public static final String KEY_VIBRATION = "vibration";
    public static final String KEY_WIFI_SPEED = "wifi_speed";
    public static final String KEY_WIFI_CATEGORY = "category_wifi";
    public static final String KEY_BLX = "blx";
    public static final String KEY_NAVIGATION_CATEGORY = "category_navigation";
    public static final String KEY_NAVBAR = "navbar";
    public static final String KEY_RAM_CATEGORY = "category_ram";
    public static final String KEY_LOWRAM = "lowram";
    public static final String KEY_TOUCHWAKE_CATEGORY = "category_touchwake";
    public static final String KEY_TOUCHWAKEACTIVE = "touchwakeactive";
    public static final String KEY_TOUCHWAKEDELAY = "touchwakedelay";
    public static final String KEY_APPLY = "apply";

    private ColorTuningPreference mColorTuning;
    private ListPreference mMdnie;
    private ListPreference mBacklightTimeout;
    private CheckBoxPreference mBLN;
    private ListPreference mHspa;
    private VolumeBoostPreference mVolumeBoost;
    private CheckBoxPreference mCarDockAudio;
    private CheckBoxPreference mDeskDockAudio;
    private VibrationPreference mVibration;
    private CheckBoxPreference mWifiSpeed;
    private BLXPreference mBLX;
    private CheckBoxPreference mNavbar;
    private CheckBoxPreference mLowram;
    private CheckBoxPreference mTouchWakeActive;
    private TouchWakePreference mTouchWakeDelay;
    private CheckBoxPreference mApply;

    private static SharedPreferences preferences;

    private BroadcastReceiver mHeadsetReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            int state = intent.getIntExtra("state", 0);
        }

    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.main);

	preferences = PreferenceManager.getDefaultSharedPreferences(this);

        mColorTuning = (ColorTuningPreference) findPreference(KEY_COLOR_TUNING);
        mColorTuning.setEnabled(ColorTuningPreference.isSupported());

        mMdnie = (ListPreference) findPreference(KEY_MDNIE);
        mMdnie.setEnabled(Mdnie.isSupported());
        mMdnie.setOnPreferenceChangeListener(new Mdnie());

        mBacklightTimeout = (ListPreference) findPreference(KEY_BACKLIGHT_TIMEOUT);
        mBacklightTimeout.setEnabled(TouchKeyBacklightTimeout.isSupported());
        mBacklightTimeout.setOnPreferenceChangeListener(new TouchKeyBacklightTimeout());

        mBLN = (CheckBoxPreference) findPreference(KEY_BLN);
        if (Led.isSupported()) {
            mBLN.setOnPreferenceChangeListener(new Led());
        } else {
            PreferenceCategory category = (PreferenceCategory) getPreferenceScreen().findPreference(KEY_LED_CATEGORY);
            category.removePreference(mBLN);
            getPreferenceScreen().removePreference(category);
        }

        mHspa = (ListPreference) findPreference(KEY_HSPA);
        if (Hspa.isSupported()) {
           mHspa.setOnPreferenceChangeListener(new Hspa(this));
        } else {
           PreferenceCategory category = (PreferenceCategory) getPreferenceScreen().findPreference(KEY_HSPA_CATEGORY);
           category.removePreference(mHspa);
           getPreferenceScreen().removePreference(category);
        }

        mVolumeBoost = (VolumeBoostPreference) findPreference(KEY_VOLUME_BOOST);
        if (!VolumeBoostPreference.isSupported()) {
            PreferenceCategory category = (PreferenceCategory) getPreferenceScreen().findPreference(KEY_VOLUME_CATEGORY);
            category.removePreference(mVolumeBoost);
            getPreferenceScreen().removePreference(category);
        }

        mCarDockAudio = (CheckBoxPreference) findPreference(KEY_CARDOCK_AUDIO);
        mDeskDockAudio = (CheckBoxPreference) findPreference(KEY_DESKDOCK_AUDIO);
        if (DockAudio.isSupported()) {
            mCarDockAudio.setOnPreferenceChangeListener(new DockAudio());
            mDeskDockAudio.setOnPreferenceChangeListener(new DockAudio());
        } else {
            PreferenceCategory category = (PreferenceCategory) getPreferenceScreen().findPreference(KEY_DOCK_AUDIO_CATEGORY);
            category.removePreference(mCarDockAudio);
            category.removePreference(mDeskDockAudio);
            getPreferenceScreen().removePreference(category);
        }

        mVibration = (VibrationPreference) findPreference(KEY_VIBRATION);
        mVibration.setEnabled(VibrationPreference.isSupported());

        mWifiSpeed = (CheckBoxPreference) findPreference(KEY_WIFI_SPEED);
        if (Wifi.isSupported()) {
            mWifiSpeed.setOnPreferenceChangeListener(new Wifi());
        } else {
            PreferenceCategory category = (PreferenceCategory) getPreferenceScreen().findPreference(KEY_WIFI_CATEGORY);
            category.removePreference(mWifiSpeed);
            getPreferenceScreen().removePreference(category);
        }

        mBLX = (BLXPreference) findPreference(KEY_BLX);
        mBLX.setEnabled(BLXPreference.isSupported());

        mNavbar = (CheckBoxPreference) findPreference(KEY_NAVBAR);
        if (Navigation.isSupported()) {
            mNavbar.setOnPreferenceChangeListener(new Navigation());
        } else {
            PreferenceCategory category = (PreferenceCategory) getPreferenceScreen().findPreference(KEY_NAVIGATION_CATEGORY);
            category.removePreference(mNavbar);
            getPreferenceScreen().removePreference(category);
        }

        mLowram = (CheckBoxPreference) findPreference(KEY_LOWRAM);
        if (Ram.isSupported()) {
            mLowram.setOnPreferenceChangeListener(new Ram());
        } else {
            PreferenceCategory category = (PreferenceCategory) getPreferenceScreen().findPreference(KEY_RAM_CATEGORY);
            category.removePreference(mLowram);
            getPreferenceScreen().removePreference(category);
        }

        mTouchWakeActive = (CheckBoxPreference) findPreference(KEY_TOUCHWAKEACTIVE);
        if (TouchWake.isSupported()) {
            mTouchWakeActive.setOnPreferenceChangeListener(new TouchWake());
        } else {
            PreferenceCategory category = (PreferenceCategory) getPreferenceScreen().findPreference(KEY_TOUCHWAKE_CATEGORY);
            category.removePreference(mTouchWakeActive);
            getPreferenceScreen().removePreference(category);
        }

        mTouchWakeDelay = (TouchWakePreference) findPreference(KEY_TOUCHWAKEDELAY);
        mTouchWakeDelay.setEnabled(TouchWakePreference.isSupported());

        mApply = (CheckBoxPreference) findPreference(KEY_APPLY);
        mApply.setOnPreferenceChangeListener(new Apply());
   }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mHeadsetReceiver, new IntentFilter(Intent.ACTION_HEADSET_PLUG));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mHeadsetReceiver);
    }


    public static void setPreferenceString(String key, String value) {
	Editor ed = preferences.edit();
	ed.putString(key, value);
	ed.commit();
    }

    public static void setPreferenceInteger(String key, int value) {
	Editor ed = preferences.edit();
	ed.putInt(key, value);
	ed.commit();
    }

    public static void setPreferenceBoolean(String key, boolean value) {
	Editor ed = preferences.edit();
	ed.putBoolean(key, value);
	ed.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
