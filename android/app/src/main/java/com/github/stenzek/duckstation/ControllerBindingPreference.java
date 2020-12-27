package com.github.stenzek.duckstation;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;

public class ControllerBindingPreference extends Preference {
    private boolean mIsAxis;
    private String mBindingName;
    private String mValue;

    private static int getIconForButton(String buttonName) {
        if (buttonName.equals("Up")) {
            return R.drawable.ic_controller_up_button_pressed;
        } else if (buttonName.equals("Right")) {
            return R.drawable.ic_controller_right_button_pressed;
        } else if (buttonName.equals("Down")) {
            return R.drawable.ic_controller_down_button_pressed;
        } else if (buttonName.equals("Left")) {
            return R.drawable.ic_controller_left_button_pressed;
        } else if (buttonName.equals("Triangle")) {
            return R.drawable.ic_controller_triangle_button_pressed;
        } else if (buttonName.equals("Circle")) {
            return R.drawable.ic_controller_circle_button_pressed;
        } else if (buttonName.equals("Cross")) {
            return R.drawable.ic_controller_cross_button_pressed;
        } else if (buttonName.equals("Square")) {
            return R.drawable.ic_controller_square_button_pressed;
        } else if (buttonName.equals("Start")) {
            return R.drawable.ic_controller_start_button_pressed;
        } else if (buttonName.equals("Select")) {
            return R.drawable.ic_controller_select_button_pressed;
        } else if (buttonName.equals("L1")) {
            return R.drawable.ic_controller_l1_button_pressed;
        } else if (buttonName.equals("L2")) {
            return R.drawable.ic_controller_l2_button_pressed;
        } else if (buttonName.equals("R1")) {
            return R.drawable.ic_controller_r1_button_pressed;
        } else if (buttonName.equals("R2")) {
            return R.drawable.ic_controller_r2_button_pressed;
        }

        return R.drawable.ic_baseline_radio_button_unchecked_24;
    }

    private static int getIconForAxis(String axisName) {
        return R.drawable.ic_baseline_radio_button_checked_24;
    }

    public ControllerBindingPreference(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        setWidgetLayoutResource(R.layout.controller_binding_preference);
    }

    public ControllerBindingPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWidgetLayoutResource(R.layout.controller_binding_preference);
    }

    public ControllerBindingPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setWidgetLayoutResource(R.layout.controller_binding_preference);
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);

        ImageView iconView = ((ImageView)holder.findViewById(R.id.controller_binding_icon));
        TextView nameView = ((TextView)holder.findViewById(R.id.controller_binding_name));
        TextView valueView = ((TextView)holder.findViewById(R.id.controller_binding_value));

        iconView.setImageDrawable(ContextCompat.getDrawable(getContext(), getIconForButton(mBindingName)));
        nameView.setText(mBindingName);
        if (mValue != null)
            valueView.setText(mValue);
        else
            valueView.setText("<No Binding>");

        setIconSpaceReserved(false);
    }

    @Override
    protected void onClick() {
        if (mIsAxis) {
            ControllerAxisBindingDialog dialog = new ControllerAxisBindingDialog(getContext(), mBindingName, getKey());
            dialog.show();
        } else {
            ControllerButtonBindingDialog dialog = new ControllerButtonBindingDialog(getContext(), mBindingName, getKey());
            dialog.show();
        }
    }

    public void init(int controllerIndex, String buttonName, String key, String currentBinding) {
        mBindingName = buttonName;
        setKey(key);
        mValue = currentBinding;
    }
}
