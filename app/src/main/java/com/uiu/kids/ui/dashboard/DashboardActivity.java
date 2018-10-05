package com.uiu.kids.ui.dashboard;


import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
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
import android.widget.Toast;

import com.google.android.gms.location.Geofence;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.shashank.sony.fancydialoglib.Animation;
import com.shashank.sony.fancydialoglib.FancyAlertDialog;
import com.shashank.sony.fancydialoglib.Icon;
import com.uiu.kids.BaseActivity;
import com.uiu.kids.Constant;
import com.uiu.kids.Injection;
import com.uiu.kids.R;
import com.uiu.kids.event.GeofenceEvent;
import com.uiu.kids.event.LocationUpdateEvent;
import com.uiu.kids.event.NotificationReceiveEvent;
import com.uiu.kids.location.BackgroundGeoFenceService;
import com.uiu.kids.model.Slide;
import com.uiu.kids.model.User;
import com.uiu.kids.ui.floatingview.FloatingViewService;

import com.uiu.kids.util.PermissionUtil;
import com.uiu.kids.util.PreferenceUtil;
import com.uiu.kids.util.Util;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.HashMap;


public class DashboardActivity extends BaseActivity implements PermissionUtil.PermissionCallback{


    private static final int CODE_DRAW_OVER_OTHER_APP_PERMISSION = 2084;
    DashboardFragment dashboardFragment;
    DashboardPresenter dashboardPresenter;
    MediaPlayer mMediaPlayer = new MediaPlayer();

    int count=0;

    @Override
    public int getID() {
        return R.layout.activity_dashboard;
    }

    @Override
    public void created(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        loadDashboardFragment();
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(this, instanceIdResult -> {
            String deviceToken = instanceIdResult.getToken();
            PreferenceUtil.getInstance(this).savePreference(PREF_NOTIFICATION_TOKEN,deviceToken);
        });

        onBobblePermission();

    }

    public void onBobblePermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {

            //If the draw over permission is not available open the settings screen
            //to grant the permission.
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, CODE_DRAW_OVER_OTHER_APP_PERMISSION);
        }else
            PermissionUtil.requestPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION,this);

    }

    @Override
    protected void onStart() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
        filter.addAction(Intent.ACTION_BATTERY_LOW);
        filter.addAction(AudioManager.RINGER_MODE_CHANGED_ACTION);
        registerReceiver(mReceiver, filter);
        super.onStart();
    }
    @Override
    protected void onStop() {
        unregisterReceiver(mReceiver);
        super.onStop();
    }

    private void loadDashboardFragment() {
        dashboardFragment = dashboardFragment != null ? dashboardFragment : DashboardFragment.newInstance();
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
        if(Util.isServiceRunning(getApplicationContext(),BackgroundGeoFenceService.class)) {
            stopService(new Intent(getApplicationContext(), BackgroundGeoFenceService.class));
        }

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(NotificationReceiveEvent receiveEvent) {

        int notificationType = receiveEvent.getNotificationForSlideType();
        if(notificationType==REQ_BEEP) {
            findPhoneAsRinging();
            return;
        }
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

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(LocationUpdateEvent receiveEvent) {
        HashMap<String,Object> params = new HashMap<>();
        params.put("user_id", PreferenceUtil.getInstance(this).getAccount().getId());
        params.put("location",receiveEvent.getLocation());
        if(dashboardPresenter!=null)
            dashboardPresenter.updateKidLocation(params);
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
        if(permission.equals(android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            startService(new Intent(getApplicationContext(), BackgroundGeoFenceService.class));
        }


    }

    @Override
    public void onPermissionsGranted() {

    }

    @Override
    public void onPermissionDenied() {
        PermissionUtil.requestPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION,this);
    }

    private class BatteryBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 20);

            Log.e("batteryLevel==", String.valueOf(level));

        }
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
            if(action.equals(Intent.ACTION_BATTERY_CHANGED)){
                int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 25);

                int level1 = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

                float batteryPct = level1 / (float)scale;

                Log.e("batteryLevel==", String.valueOf(batteryPct));
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
        }
    };
    public void findPhoneAsRinging(){

        if(mMediaPlayer!=null && mMediaPlayer.isPlaying())
            return;
        try {
            Uri uri=Uri.parse("android.resource://"+getPackageName()+"/raw/beep");

            mMediaPlayer.setDataSource(getApplicationContext(), uri);
            final AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            if (audioManager.getStreamVolume(AudioManager.STREAM_RING) != 0) {
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_RING);
                mMediaPlayer.setLooping(false);
                mMediaPlayer.prepare();
                mMediaPlayer.start();
                mMediaPlayer.setOnCompletionListener(mp -> {

                    count++;
                    if(count<4)
                        mMediaPlayer.start();
                    else {
                        mMediaPlayer.reset();
                        count = 0;
                    }
                });
            }
        } catch(Exception e) {
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CODE_DRAW_OVER_OTHER_APP_PERMISSION) {
            //Check if the permission is granted or not.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
                PreferenceUtil.getInstance(this).savePreference("BobbleHeadOverlay",false);
            }else {
                PreferenceUtil.getInstance(this).savePreference("BobbleHeadOverlay",true);
            }
            PermissionUtil.requestPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION, this);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
