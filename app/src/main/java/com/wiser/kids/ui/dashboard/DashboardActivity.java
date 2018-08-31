package com.wiser.kids.ui.dashboard;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import com.google.android.gms.location.Geofence;
import com.google.firebase.iid.FirebaseInstanceId;
import com.shashank.sony.fancydialoglib.Animation;
import com.shashank.sony.fancydialoglib.FancyAlertDialog;
import com.shashank.sony.fancydialoglib.Icon;
import com.wiser.kids.BaseActivity;
import com.wiser.kids.Injection;
import com.wiser.kids.Manifest;
import com.wiser.kids.R;
import com.wiser.kids.event.GeofenceEvent;
import com.wiser.kids.event.LocationUpdateEvent;
import com.wiser.kids.event.NotificationReceiveEvent;
import com.wiser.kids.location.BackgroundGeoFenceService;
import com.wiser.kids.util.PermissionUtil;
import com.wiser.kids.util.PreferenceUtil;
import com.wiser.kids.util.Util;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;


public class DashboardActivity extends BaseActivity implements PermissionUtil.PermissionCallback{



    DashboardFragment dashboardFragment;
    DashboardPresenter dashboardPresenter;


    @Override
    public int getID() {
        return R.layout.activity_dashboard;
    }

    @Override
    public void created(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(this, instanceIdResult -> {
            String deviceToken = instanceIdResult.getToken();
            PreferenceUtil.getInstance(this).savePreference(PREF_NOTIFICATION_TOKEN,deviceToken);
            loadDashboardFragment();

        });
        PermissionUtil.requestPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION,this);


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
        if(Util.isLocationServiceRunning(getApplicationContext(),BackgroundGeoFenceService.class)) {
            stopService(new Intent(getApplicationContext(), BackgroundGeoFenceService.class));
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(NotificationReceiveEvent receiveEvent) {
        showNotification(receiveEvent.getTitle(),receiveEvent.getMessage());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(GeofenceEvent receiveEvent) {
        String transition = receiveEvent.transition==Geofence.GEOFENCE_TRANSITION_ENTER?"Enter":"Exit";

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(LocationUpdateEvent receiveEvent) {
        HashMap<String,Object> params = new HashMap<>();
        params.put("user_id",PreferenceUtil.getInstance(this).getAccount().getId());
        params.put("location",receiveEvent.getLocation());
        dashboardPresenter.updateKidLocation(params);
    }

    public void showNotification(String title,String message){
        new FancyAlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setBackgroundColor(Color.parseColor("#378718"))  //Don't pass R.color.colorvalue
                .setPositiveBtnBackground(Color.parseColor("#2572D9"))  //Don't pass R.color.colorvalue
                .setPositiveBtnText("OK")
                .setNegativeBtnText("Cancel")
                .setAnimation(Animation.POP)
                .isCancellable(true)
                .setIcon(R.drawable.ic_done, Icon.Visible)
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
}
