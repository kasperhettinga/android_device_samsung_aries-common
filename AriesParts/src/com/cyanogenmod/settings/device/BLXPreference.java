package com.cyanogenmod.settings.device;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.DialogPreference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class BLXPreference extends DialogPreference implements OnClickListener {

    private static final int SEEKBAR_ID = R.id.blx_seekbar;

    private static final int VALUE_DISPLAY_ID = R.id.blx_value;

    private static final String FILE_PATH = "/sys/class/misc/batterylifeextender/charging_limit";

    private BLXSeekBar mSeekBar = new BLXSeekBar();

    private static final int MAX_VALUE = 100;

    // Track instances to know when to restore original value
    // (when the orientation changes, a new dialog is created before the old one is destroyed)
    private static int sInstances = 0;

    public BLXPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        setDialogLayoutResource(R.layout.preference_dialog_blx);
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);

        sInstances++;

        SeekBar seekBar = (SeekBar) view.findViewById(SEEKBAR_ID);
        TextView valueDisplay = (TextView) view.findViewById(VALUE_DISPLAY_ID);
        mSeekBar = new BLXSeekBar(seekBar, valueDisplay, FILE_PATH);

        SetupButtonClickListener(view);
    }

    private void SetupButtonClickListener(View view) {
        Button mResetButton = (Button)view.findViewById(R.id.blx_reset);
        mResetButton.setOnClickListener(this);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);

        sInstances--;

        if (positiveResult) {
            mSeekBar.save();
        } else if (sInstances == 0) {
            mSeekBar.reset();
        }
    }

    public static void restore(Context context) {
        if (!isSupported()) {
            return;
        }

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        int value = sharedPrefs.getInt(FILE_PATH, MAX_VALUE);
        Utils.writeValue(FILE_PATH, String.valueOf(value));
    }

    public static boolean isSupported() {
        boolean supported = true;
        if (!Utils.fileExists(FILE_PATH)) {
            supported = false;
        }

        return supported;
    }

    class BLXSeekBar implements SeekBar.OnSeekBarChangeListener {

        protected String mFilePath;
        protected int mOriginal;
        protected SeekBar mSeekBar;
        protected TextView mValueDisplay;

        public BLXSeekBar(SeekBar seekBar, TextView valueDisplay, String filePath) {
            mSeekBar = seekBar;
            mValueDisplay = valueDisplay;
            mFilePath = filePath;

            // Read original value
            SharedPreferences sharedPreferences = getSharedPreferences();
            mOriginal = sharedPreferences.getInt(mFilePath, MAX_VALUE);

            seekBar.setMax(MAX_VALUE);
            reset();
            seekBar.setOnSeekBarChangeListener(this);
        }

        // For inheriting class
        protected BLXSeekBar() {
        }

        public void reset() {
            mSeekBar.setProgress(mOriginal);
            updateValue(mOriginal);
        }

        public void save() {
            Editor editor = getEditor();
            editor.putInt(mFilePath, mSeekBar.getProgress());
            editor.commit();
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress,
                boolean fromUser) {
            Utils.writeValue(mFilePath, String.valueOf(progress));
            updateValue(progress);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            // Do nothing
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // Do nothing
        }

        protected void updateValue(int progress) {
            mValueDisplay.setText(String.valueOf(progress) + "%");
        }

        public void resetDefault() {
            mSeekBar.setProgress(MAX_VALUE);
            updateValue(MAX_VALUE);
            Utils.writeValue(FILE_PATH, String.valueOf(MAX_VALUE));
        }
    }

    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.blx_reset:
                mSeekBar.resetDefault();
                break;
        }
    }
}
