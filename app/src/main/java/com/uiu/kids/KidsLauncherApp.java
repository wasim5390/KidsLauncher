package com.uiu.kids;

import android.app.Application;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.provider.Telephony;
import android.support.multidex.MultiDexApplication;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.uiu.kids.event.LoginFailEvent;
import com.uiu.kids.ui.SleepActivity;
import com.uiu.kids.ui.SmsReceiver;
import com.uiu.kids.ui.dashboard.DashboardActivity;
import com.uiu.kids.ui.floatingview.FloatingViewService;
import com.uiu.kids.util.PreferenceUtil;
import com.uiu.kids.util.Util;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import io.fabric.sdk.android.Fabric;


/**
 * Created by sidhu on 4/11/2018.
 */

public class KidsLauncherApp extends MultiDexApplication implements AppLifecycleHandler.LifeCycleDelegate {
    private static KidsLauncherApp instance;
    private AppLifecycleHandler lifeCycleHandler;
    private boolean isForeground=true;
    private SmsReceiver smsBroadcastReceiver;

    private static boolean sleepModeActive=false;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        EventBus.getDefault().register(this);

        Fabric.with(this, new Crashlytics());
        lifeCycleHandler = new AppLifecycleHandler(this);
        registerLifecycleHandler(lifeCycleHandler);
        smsBroadcastReceiver = new SmsReceiver();
        registerReceiver(smsBroadcastReceiver, new IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION));



    }
    @Override
    public void onTerminate() {
        unregisterReceiver(smsBroadcastReceiver);
        super.onTerminate();
    }


    public static KidsLauncherApp getInstance() {
        return instance==null?new KidsLauncherApp():instance;
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(LoginFailEvent event) {
        logout();
    }



    public void logout() {

        GoogleSignInClient client = GoogleSignIn.getClient(getApplicationContext()
                ,new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build());
        client.signOut();
        PreferenceUtil.getInstance(this).clearAllPreferences();

        Intent intent = new Intent(this, DashboardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    // App in background
    public void onAppBackgrounded() {
        isForeground=false;
       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && PreferenceUtil.getInstance(this).getBooleanPreference("BobbleHeadOverlay",true)) {
            if( Settings.canDrawOverlays(getApplicationContext()))
            startService(new Intent(this, FloatingViewService.class));
        }*/
        if(isSleepModeActive()) {
            Intent intent = new Intent();
            intent.setClass(getApplicationContext(),SleepActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(new Intent(getApplicationContext(), SleepActivity.class));
        }
    }

    @Override
    // App in foreground
    public void onAppForegrounded() {
        isForeground = true;/*
        if(Util.isServiceRunning(this,FloatingViewService.class))
            stopService(new Intent(this,FloatingViewService.class));*/
    }


    public boolean isForeground() {
        return isForeground;
    }
    public static boolean isSleepModeActive() {
        sleepModeActive = PreferenceUtil.getInstance(instance).getBooleanPreference(Constant.PREF_KEY_SLEEP_MODE,false);
        return sleepModeActive;
    }

    public static void setSleepModeActive(boolean sleepModeActive) {
        KidsLauncherApp.sleepModeActive = sleepModeActive;
    }
    private void registerLifecycleHandler(AppLifecycleHandler lifeCycleHandler) {
        registerActivityLifecycleCallbacks(lifeCycleHandler);
        registerComponentCallbacks(lifeCycleHandler);
    }
}
