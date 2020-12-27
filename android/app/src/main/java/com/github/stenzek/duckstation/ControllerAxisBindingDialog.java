package com.github.stenzek.duckstation;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.KeyEvent;

public class ControllerAxisBindingDialog extends AlertDialog {
    private String mSettingKey;
    private String mCurrentBinding;
    private int mUpdatedKeyCode = -1;

    public ControllerAxisBindingDialog(Context context, String buttonName, String settingKey) {
        super(context);

        mSettingKey = settingKey;
        mCurrentBinding = PreferenceManager.getDefaultSharedPreferences(context).getString(settingKey, null);
        if (mCurrentBinding == null) {
            mCurrentBinding = "<No Binding>";
        }

        setTitle(buttonName);
        updateMessage();
        setButton(BUTTON_POSITIVE, "Cancel", (dialogInterface, button) -> dismiss());
        setButton(BUTTON_NEGATIVE, "Clear", (dialogInterface, button) -> {
            mCurrentBinding = null;
            updateBinding();
            dismiss();
        });
    }

    private void updateMessage() {
        setMessage("Press button on controller to set new binding.\n\nCurrent Binding: " + mCurrentBinding);
    }

    private void updateBinding() {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getContext()).edit();
        if (mCurrentBinding != null) {
            editor.putString(mSettingKey, mCurrentBinding);
        } else {
            try {
                editor.remove(mSettingKey);
            } catch (Exception e) {

            }
        }

        editor.commit();
    }
}
