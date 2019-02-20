package com.uiu.kids.ui.home.setting;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.uiu.kids.Constant;
import com.uiu.kids.KidsLauncherApp;
import com.uiu.kids.R;
import com.uiu.kids.event.SettingsEvent;
import com.uiu.kids.ui.slides.reminder.AlarmReceiver;
import com.uiu.kids.util.PreferenceUtil;
import com.uiu.kids.util.SettingData;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

import static com.uiu.kids.BaseActivity.setSystemUiVisibility;
import static com.uiu.kids.BaseActivity.showImmersiveDialog;

public class SettingsDialogFragment extends DialogFragment implements SeekBar.OnSeekBarChangeListener {

    @BindView(R.id.swFlashLight)
    SwitchCompat swFlashLight;
    @BindView(R.id.swWifi)
    SwitchCompat swWifi;
    @BindView(R.id.swBluetooth)
    SwitchCompat swBluetooth;
    @BindView(R.id.sbBrightness)
    AppCompatSeekBar sbBrightness;
    @BindView(R.id.sbVolume)
    AppCompatSeekBar sbVolume;

    @BindView(R.id.tvBrightnessPercent)
    TextView tvBrightnessPercent;
    @BindView(R.id.tvVolumePercent)
    TextView tvVolumePercent;
    @BindView(R.id.tvBatteryPercent)
    TextView tvBatteryPercent;

    @BindView(R.id.action_bar_battery_icon)
    public ImageView ivBattery;

    int mBatteryPrecentage;
    int mVolume;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.MyDialog);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        showImmersiveDialog(this.getDialog(),getActivity());
        View v = inflater.inflate(R.layout.fragment_settings, container, false);
        ButterKnife.bind(this,v);
        String prefBrightness =  PreferenceUtil.getInstance(getContext()).getPreference(Constant.KEY_BRIGHTNESS);
        String prefVolume = PreferenceUtil.getInstance(getContext()).getPreference(Constant.KEY_VOLUME);
        float  brightness = prefBrightness.isEmpty()?(int)SettingData.getBrightnessLevel(getContext()):Float.parseFloat(prefBrightness);

        mBatteryPrecentage =(int) SettingData.getBatteryLevel(getContext());
        mVolume = prefVolume.isEmpty()?SettingData.getVolume(getContext()):Integer.parseInt(prefVolume);
        sbVolume.setProgress(mVolume);
        if (mVolume >= 97) {
            mVolume = 100;
        }
        tvVolumePercent.setText(mVolume + "%");
        sbBrightness.setProgress((int)(brightness/255*100));
        swBluetooth.setChecked(SettingData.isBluetoothOn());
        swWifi.setChecked(SettingData.isWifiConnected(getContext()));
        swFlashLight.setChecked(SettingData.TORCH_ENABLED);
        sbVolume.setOnSeekBarChangeListener(this);
        sbBrightness.setOnSeekBarChangeListener(this);

        tvBrightnessPercent.setText(Math.round(brightness/255*100)+"".concat("%"));
        tvBatteryPercent.setText(mBatteryPrecentage + " %");

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sbBrightness.getProgressDrawable().setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(getContext(),R.color.green), PorterDuff.Mode.MULTIPLY));
        sbVolume.getProgressDrawable().setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(getContext(),R.color.green), PorterDuff.Mode.MULTIPLY));
        sbBrightness.getThumb().setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(getContext(),R.color.green), PorterDuff.Mode.SRC_ATOP));
        sbVolume.getThumb().setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(getContext(),R.color.green), PorterDuff.Mode.SRC_ATOP));
        setBatteryLevel(mBatteryPrecentage);
    }

    private void setBatteryLevel(int level){
        if (level >= 0 && level <= 25){
            ivBattery.setBackgroundResource(R.drawable.ic_setting_battery_1);
        }else if (level >= 26 && level <= 50){
            ivBattery.setBackgroundResource(R.drawable.ic_setting_battery_2);
        }else if (level >= 51 && level <= 75){
            ivBattery.setBackgroundResource(R.drawable.ic_setting_battery_3);
        }else if (level >= 76 && level <= 100){
            ivBattery.setBackgroundResource(R.drawable.ic_setting_battery_4);
        }
    }


    @OnCheckedChanged({R.id.swFlashLight,R.id.swBluetooth,R.id.swWifi})
    public void onCheck(CompoundButton btn,boolean checked){
        switch (btn.getId()){
            case R.id.swFlashLight:
                SettingData.setFlashOnOff(KidsLauncherApp.getInstance(),checked);
                break;
            case R.id.swBluetooth:
                SettingData.setBluetoothOnOff(checked);
                break;
            case R.id.swWifi:
                SettingData.setWifiConnected(getContext(),checked);
                break;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()){
            case R.id.sbBrightness:
                // SettingData.setBrightnessLevel(getContext(),(int)(2.55*progress));
                PreferenceUtil.getInstance(getContext()).savePreference(Constant.KEY_BRIGHTNESS,String.valueOf(2.55*progress));
                EventBus.getDefault().postSticky(new SettingsEvent((int)(2.55*progress)));
                tvBrightnessPercent.setText(progress+"".concat("%"));
                break;
            case R.id.sbVolume:
                mVolume = (int) (progress);
                tvVolumePercent.setText(mVolume+"%");
                SettingData.setVolume(getContext(),mVolume);
                PreferenceUtil.getInstance(getContext()).savePreference(Constant.KEY_VOLUME,String.valueOf(mVolume));

                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onDestroy() {

        super.onDestroy();

    }

    @Override
    public void onDismiss(DialogInterface dialog) {

        super.onDismiss(dialog);
    }

    @OnClick(R.id.ivClose)
    public void onClose(){
        dismiss();
    }

    @OnClick(R.id.ivSettings)
    public void updateSlides(){
        EventBus.getDefault().post(new AlarmReceiver.DataSyncEvent());
    }
}