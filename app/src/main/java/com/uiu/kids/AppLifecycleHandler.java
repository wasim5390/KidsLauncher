package com.uiu.kids;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks2;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.Toast;

import com.uiu.kids.ui.dashboard.DashboardActivity;


/**
 * Created by sidhu on 4/19/2018.
 */

public class AppLifecycleHandler implements Application.ActivityLifecycleCallbacks, ComponentCallbacks2 {



    private LifeCycleDelegate lifeCycleDelegate;

    private boolean appInForeground = false;

    public AppLifecycleHandler(LifeCycleDelegate lifeCycleDelegate) {
        this.lifeCycleDelegate = lifeCycleDelegate;
    }


    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        if (!appInForeground) {
            appInForeground = true;
            lifeCycleDelegate.onAppForegrounded();
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }

    @Override
    public void onTrimMemory(int level) {
        if (level == ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN) {
            appInForeground = false;
            lifeCycleDelegate.onAppBackgrounded();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration configuration) {

    }

    @Override
    public void onLowMemory() {

    }

    interface LifeCycleDelegate {
        void onAppBackgrounded();
        void onAppForegrounded();
    }
}
