package com.wiser.kids;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.wiser.kids.event.LoginFailEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import io.fabric.sdk.android.Fabric;


/**
 * Created by sidhu on 4/11/2018.
 */

public class KidsLauncherApp extends Application implements AppLifecycleHandler.LifeCycleDelegate {
    private static KidsLauncherApp instance;
    private AppLifecycleHandler lifeCycleHandler;
    private boolean isForeground=true;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        EventBus.getDefault().register(this);
        Fabric.with(this, new Crashlytics());
        lifeCycleHandler = new AppLifecycleHandler(this);
        registerLifecycleHandler(lifeCycleHandler);

    }

    public static KidsLauncherApp getInstance() {
        return instance==null?new KidsLauncherApp():instance;
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(LoginFailEvent event) {
        /*if (PreferenceUtil.getInstance(this).isSignIn()) {
            logout();
        }*/
    }



    public void logout() {
     /*   PreferenceUtil.getInstance(this).saveEmail(null);
        PreferenceUtil.getInstance(this).savePassword(null);
        PreferenceUtil.getInstance(this).setSignIn(false);
        PreferenceUtil.getInstance(this).clearAllPreferences();

        Intent intent = new Intent(this, LoginMainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);*/
    }

    @Override
    // App in background
    public void onAppBackgrounded() {
        isForeground=false;
    }

    @Override
    // App in foreground
    public void onAppForegrounded() {
        isForeground = true;
    }

    public boolean isForeground() {
        return isForeground;
    }

    private void registerLifecycleHandler(AppLifecycleHandler lifeCycleHandler) {
        registerActivityLifecycleCallbacks(lifeCycleHandler);
        registerComponentCallbacks(lifeCycleHandler);
    }
}
