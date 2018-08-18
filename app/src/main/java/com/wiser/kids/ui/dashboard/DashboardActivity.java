package com.wiser.kids.ui.dashboard;


import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import android.util.Log;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.shashank.sony.fancydialoglib.Animation;
import com.shashank.sony.fancydialoglib.FancyAlertDialog;
import com.shashank.sony.fancydialoglib.FancyAlertDialogListener;
import com.shashank.sony.fancydialoglib.Icon;
import com.wiser.kids.BaseActivity;
import com.wiser.kids.Constant;
import com.wiser.kids.Injection;
import com.wiser.kids.R;
import com.wiser.kids.event.NotificationReceiveEvent;
import com.wiser.kids.ui.home.apps.AppsEntity;
import com.wiser.kids.util.PreferenceUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;


public class DashboardActivity extends BaseActivity   {



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
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(NotificationReceiveEvent receiveEvent) {
       showNotification(receiveEvent.getTitle(),receiveEvent.getMessage());

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

}
