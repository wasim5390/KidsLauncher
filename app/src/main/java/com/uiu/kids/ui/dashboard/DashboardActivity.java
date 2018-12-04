package com.uiu.kids.ui.dashboard;


import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.Geofence;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.shashank.sony.fancydialoglib.Animation;
import com.shashank.sony.fancydialoglib.FancyAlertDialog;
import com.shashank.sony.fancydialoglib.Icon;
import com.uiu.kids.BaseActivity;
import com.uiu.kids.Constant;
import com.uiu.kids.Injection;
import com.uiu.kids.KidsLauncherApp;
import com.uiu.kids.R;
import com.uiu.kids.event.GeofenceEvent;
import com.uiu.kids.event.LocationUpdateEvent;
import com.uiu.kids.event.NotificationReceiveEvent;
import com.uiu.kids.event.ReminderRecieveEvent;
import com.uiu.kids.event.SleepModeEvent;
import com.uiu.kids.location.BackgroundGeoFenceService;
import com.uiu.kids.location.LocationServiceEnableEvent;
import com.uiu.kids.model.Setting;
import com.uiu.kids.model.Slide;
import com.uiu.kids.ui.SleepActivity;

import com.uiu.kids.util.PermissionUtil;
import com.uiu.kids.util.PreferenceUtil;
import com.uiu.kids.util.SettingData;
import com.uiu.kids.util.Util;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;


public class DashboardActivity extends BaseActivity implements PermissionUtil.PermissionCallback{


    private static final int CODE_DRAW_OVER_OTHER_APP_PERMISSION = 2084;
    DashboardFragment dashboardFragment;
    DashboardPresenter dashboardPresenter;


    int countToPush=0;

    String activityLoadedFrom="Normal";


    @Override
    public int getID() {
        return R.layout.activity_dashboard;
    }

    @Override
    public void created(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);

        String bg = PreferenceUtil.getInstance(this).getPreference(Constant.KEY_SELECTED_BG);
        if(bg.isEmpty())
            getWindow().setBackgroundDrawableResource(R.color.white);
        else
            applyBg(bg);

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(this, instanceIdResult -> {
            String deviceToken = instanceIdResult.getToken();
            PreferenceUtil.getInstance(this).savePreference(PREF_NOTIFICATION_TOKEN,deviceToken);
        });
        Intent intent = getIntent();
        if(intent!=null && intent.hasExtra("ActionFrom"))
            activityLoadedFrom = intent.getStringExtra("ActionFrom");
        onBobblePermission();
        if(KidsLauncherApp.getInstance().isSleepModeActive())
        {
            startActivity(new Intent(this, SleepActivity.class));
        }
    }


    public void onBobblePermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {

            //If the draw over permission is not available open the settings screen
            //to grant the permission.
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, CODE_DRAW_OVER_OTHER_APP_PERMISSION);
        }else
            PermissionUtil.requestPermissions(this,this);
    }

    @Override
    protected void onStart() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
        filter.addAction(Intent.ACTION_BATTERY_LOW);
        filter.addAction(AudioManager.RINGER_MODE_CHANGED_ACTION);
        registerReceiver(mReceiver, filter);
        registerReceiver(mGpsSwitchStateReceiver, new IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION));

        super.onStart();
    }
    @Override
    protected void onStop() {
        unregisterReceiver(mReceiver);
        unregisterReceiver(mGpsSwitchStateReceiver);
        super.onStop();
    }

    private void loadDashboardFragment() {
        dashboardFragment = dashboardFragment != null ? dashboardFragment : DashboardFragment.newInstance(activityLoadedFrom);
        dashboardPresenter = dashboardPresenter != null ? dashboardPresenter : new DashboardPresenter(dashboardFragment, PreferenceUtil.getInstance(this), Injection.provideRepository(this));
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, dashboardFragment);
        fragmentTransaction.commitAllowingStateLoss();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        stopLocationService();

    }

    @Override
    protected void onResume() {
        super.onResume();
        gotoSleepMode();
    }

    private void gotoSleepMode(){
        if(KidsLauncherApp.getInstance().isSleepModeActive())
        {
            startActivity(new Intent(this, SleepActivity.class));
        }
    }

    public void startLocationService(){
        startService(new Intent(getApplicationContext(), BackgroundGeoFenceService.class));
    }

    public void stopLocationService(){
        if(Util.isServiceRunning(getApplicationContext(),BackgroundGeoFenceService.class)) {
            stopService(new Intent(getApplicationContext(), BackgroundGeoFenceService.class));
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(SleepModeEvent sleepModeEvent) {
        if(sleepModeEvent.isEnable())
            gotoSleepMode();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(NotificationReceiveEvent receiveEvent) {

        int notificationType = receiveEvent.getNotificationForSlideType();
        if(notificationType==REQ_BEEP) {
            return;
        }

        if(!receiveEvent.isSlideUpdate())
        showNotification(receiveEvent.getTitle(),receiveEvent.getMessage(),receiveEvent.getStatus());
        if(notificationType==Constant.SLIDE_CREATE_INDEX||notificationType==Constant.SLIDE_REMOVE_INDEX){
            JSONObject jsonObject = receiveEvent.getNotificationResponse();
            Slide entity =  new Gson().fromJson(jsonObject.toString(),Slide.class);
            if(notificationType==SLIDE_CREATE_INDEX)
                dashboardPresenter.addSlide(entity);
            if(notificationType==SLIDE_REMOVE_INDEX)
                dashboardPresenter.removeSlide(entity);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(GeofenceEvent receiveEvent) {
        String transition = receiveEvent.transition==Geofence.GEOFENCE_TRANSITION_ENTER?"Enter":"Exit";
        // String title = receiveEvent.getLocation().getTitle();
        HashMap<String,Object> params = new HashMap<>();
        params.put("latitude",receiveEvent.getLocation().getLatitude());
        params.put("longitude",receiveEvent.getLocation().getLongitude());
        params.put("title","Title");
        params.put("tracking_status",receiveEvent.transition);
        dashboardPresenter.updateKidLocationRange(params);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(LocationUpdateEvent receiveEvent) {
        HashMap<String,Object> params = new HashMap<>();
        params.put("user_id", PreferenceUtil.getInstance(this).getAccount().getId());
        params.put("location",receiveEvent.getLocation());
        if(dashboardPresenter!=null)
            dashboardPresenter.updateKidLocation(params);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ReminderRecieveEvent receiveEvent) {
        if (receiveEvent.getType() == Constant.SLIDE_INDEX_REMINDERS) {
            int index = receiveEvent.getIndex();
            String title = receiveEvent.getTitle();
            String note = receiveEvent.getNote();
            Log.e("index", String.valueOf(index));
            setAlarmAlert(title, note);
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(LocationServiceEnableEvent receiveEvent) {
        try {
            ResolvableApiException resolvableApiException=receiveEvent.getException();
            resolvableApiException.startResolutionForResult(this, 1122);
            // stopLocationService();
        } catch (IntentSender.SendIntentException e) {

        }

    }

    public void showNotification(String title,String message,int status){
        new FancyAlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setBackgroundColor(Color.parseColor((status== ACCEPTED || status==INVITE.CONNECTED)?"#378718":"#C82506"))  //Don't pass R.color.colorvalue
                .setPositiveBtnBackground(Color.parseColor("#2572D9"))  //Don't pass R.color.colorvalue
                .setPositiveBtnText("OK")
                .setNegativeBtnText("Cancel")
                .setAnimation(Animation.POP)
                .isCancellable(true)
                .setIcon(status==ACCEPTED?R.drawable.ic_done:R.drawable.ic_close, Icon.Visible)
                .OnNegativeClicked(() -> { })
                .build();
    }

    @Override
    public void onPermissionsGranted(String permission) {

    }

    @Override
    public void onPermissionsGranted() {
        if(PermissionUtil.isPermissionGranted(this, android.Manifest.permission.ACCESS_FINE_LOCATION))
            startLocationService();
        loadDashboardFragment();
    }

    @Override
    public void onPermissionDenied() {
        PermissionUtil.requestPermissions(this,this);
    }



    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                switch (state) {
                    case BluetoothAdapter.STATE_OFF:
                        Log.e("Bluetooth state","off");
                        break;
                    case BluetoothAdapter.STATE_ON:
                        Log.e("Bluetooth state","on");
                        break;
                }
            }
            //======= Battery_Action gets called in 10 sec's interval.
            if(action.equals(Intent.ACTION_BATTERY_CHANGED)){
                countToPush++;
                int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 25);
                int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS,BatteryManager.BATTERY_STATUS_NOT_CHARGING);
                int level1 = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

                float batteryPct = level1 / (float)scale;
                if(batteryPct<0.15 && status==BatteryManager.BATTERY_STATUS_DISCHARGING && countToPush>4) {
                    countToPush=0;
                    dashboardPresenter.notifyBatteryAlert(PreferenceUtil.getInstance(getApplicationContext()).getAccount().getId());
                }else{
                    return;
                }
                Log.e("batteryLevel==", String.valueOf(batteryPct) +" "+countToPush);

            }



            if(action.equals(AudioManager.RINGER_MODE_CHANGED_ACTION)){
                int mode = intent.getIntExtra(AudioManager.EXTRA_RINGER_MODE,0);
                switch (mode){
                    case AudioManager.RINGER_MODE_NORMAL:

                        Log.e("Ring tone sound","Normal");
                        break;
                    case AudioManager.RINGER_MODE_SILENT:

                        Log.e("Ring tone sound","Silent");
                        break;
                    case AudioManager.RINGER_MODE_VIBRATE:

                        Log.e("Ring tone sound","Vibrate");
                        break;
                }
            }
            if(dashboardPresenter!=null)
                dashboardPresenter.updateKidsSettings(createSettingObj());



        }
    };
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CODE_DRAW_OVER_OTHER_APP_PERMISSION) {
            //Check if the permission is granted or not.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
                PreferenceUtil.getInstance(this).savePreference("BobbleHeadOverlay",false);
            }else {
                PreferenceUtil.getInstance(this).savePreference("BobbleHeadOverlay",true);
            }
            PermissionUtil.requestPermissions(this, this);
        }
        else if(requestCode == 1122){
            BackgroundGeoFenceService.getInstance().startLocationUpdates();
            // startLocationService();
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public Setting createSettingObj(){
        Setting setting = new Setting();
        setting.setBatteryLevel(SettingData.getBatteryLevel(getApplicationContext()));
        setting.setBlueToothOn(SettingData.isBluetoothOn());
        setting.setBrightnessLevel(SettingData.getBrightnessLevel(getApplicationContext()));
        setting.setLocationEnable(SettingData.isGpsOn(getApplicationContext()));
        setting.setSoundState(SettingData.getSoundState(getApplicationContext()));
        setting.setWifiEnable(SettingData.isWifiConnected(getApplicationContext()));
        setting.setSleepMode(PreferenceUtil.getInstance(getApplicationContext()).getBooleanPreference(Constant.PREF_KEY_SLEEP_MODE,false));

        return setting;
    }




    public void setAlarmAlert(String title, String note) {
        MediaPlayer mp;
        mp = new MediaPlayer();
        try {
            Uri uri=Uri.parse("android.resource://"+getPackageName()+"/raw/beep");
            mp.reset();
            mp.setDataSource(this, uri);
            final AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            if (audioManager.getStreamVolume(AudioManager.STREAM_RING) != 0) {
                mp.setAudioStreamType(AudioManager.STREAM_RING);
                mp.setVolume(20,20);
                mp.prepare();
                mp.setLooping(true);
                mp.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
///////////////set alart/////////

        Dialog dialog = new Dialog(this, android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth);
        dialog.setContentView(R.layout.alarm_alert_dialog);
        TextView tvTitle, tvNote;
        Button cancel;
        tvNote = (TextView) dialog.findViewById(R.id.alertNote);
        tvTitle = (TextView) dialog.findViewById(R.id.alartTitle);
        cancel = (Button) dialog.findViewById(R.id.alartCancel);

        tvTitle.setText(title.toString());
        tvNote.setText(note.toString());

        dialog.show();

        cancel.setOnClickListener(v -> {
            mp.stop();
            dialog.dismiss();
        });


    }




}
