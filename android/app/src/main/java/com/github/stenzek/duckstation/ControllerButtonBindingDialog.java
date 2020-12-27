package com.github.stenzek.duckstation;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.KeyEvent;

public class ControllerButtonBindingDialog extends AlertDialog {
    private String mSettingKey;
    private String mCurrentBinding;

    public ControllerButtonBindingDialog(Context context, String buttonName, String settingKey) {
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

        setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (onKeyDown(keyCode, event))
                    return true;

                return false;
            }
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (!EmulationSurfaceView.isDPadOrButtonEvent(event))
            return super.onKeyUp(keyCode, event);

        int buttonIndex = EmulationSurfaceView.getButtonIndexForKeyCode(keyCode);
        if (buttonIndex < 0)
            return super.onKeyUp(keyCode, event);

        // TODO: Multiple controllers
        final int controllerIndex = 0;
        mCurrentBinding = String.format("Controller%d/Button%d", controllerIndex, buttonIndex);
        updateMessage();
        updateBinding();
        dismiss();
        return true;
    }
}
