package com.uiu.kids;

import android.app.Application;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.uiu.kids.event.LoginFailEvent;
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Settings.canDrawOverlays(this)
                && PreferenceUtil.getInstance(this).getBooleanPreference("BobbleHeadOverlay")
                )
            startService(new Intent(this, FloatingViewService.class));

    }

    @Override
    // App in foreground
    public void onAppForegrounded() {
        isForeground = true;
        if(Util.isServiceRunning(this,FloatingViewService.class))
            stopService(new Intent(this,FloatingViewService.class));
    }


    public boolean isForeground() {
        return isForeground;
    }

    private void registerLifecycleHandler(AppLifecycleHandler lifeCycleHandler) {
        registerActivityLifecycleCallbacks(lifeCycleHandler);
        registerComponentCallbacks(lifeCycleHandler);
    }
}
