package com.uiu.kids.ui;

import android.app.AppOpsManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.uiu.kids.BaseActivity;
import com.uiu.kids.Constant;
import com.uiu.kids.KidsLauncherApp;
import com.uiu.kids.R;
import com.uiu.kids.event.SleepModeEvent;
import com.uiu.kids.model.Setting;
import com.uiu.kids.model.response.GetSettingsResponse;
import com.uiu.kids.source.DataSource;
import com.uiu.kids.source.Repository;
import com.uiu.kids.ui.dashboard.DashboardActivity;
import com.uiu.kids.util.PreferenceUtil;
import com.uiu.kids.util.Util;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import static com.uiu.kids.Constant.PREF_KEY_SLEEP_TIME;

public class SleepActivity extends AppCompatActivity{


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        hideSystemUI();
        setContentView(R.layout.activity_sleep);

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        hideSystemUI();

    }

    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
        if(!PreferenceUtil.getInstance(this).getBooleanPreference(Constant.PREF_KEY_SLEEP_MODE,false))
            finish();
    }

    // Shows the system bars by removing all the flags
// except for the ones that make the content appear under the system bars.
    private void showSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    private boolean isAccessGranted() {
        try {
            PackageManager packageManager = getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(getPackageName(), 0);
            AppOpsManager appOpsManager = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
            int mode = 0;
            if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.KITKAT) {
                mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                        applicationInfo.uid, applicationInfo.packageName);
            }
            return (mode == AppOpsManager.MODE_ALLOWED);

        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }


    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(SleepModeEvent sleepModeEvent) {
        Setting setting = new Setting();
        setting.setSleepMode(PreferenceUtil.getInstance(getApplicationContext()).getBooleanPreference(Constant.PREF_KEY_SLEEP_MODE,false));
        setting.setTimedSleepEnable(PreferenceUtil.getInstance(getApplicationContext()).getBooleanPreference(PREF_KEY_SLEEP_TIME,false));
        HashMap<String,Object> params = new HashMap<>();
        params.put("kid_id",PreferenceUtil.getInstance(this).getAccount().getId());
        params.put("setting",setting);
        if(Util.isInternetAvailable())
        Repository.getInstance().updateKidSettings(params, new DataSource.GetDataCallback<GetSettingsResponse>() {
            @Override
            public void onDataReceived(GetSettingsResponse data) {

            }

            @Override
            public void onFailed(int code, String message) {

            }
        });
        if(!sleepModeEvent.isEnable()) {
            Intent intent = new Intent(this,DashboardActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        }
    }

}
