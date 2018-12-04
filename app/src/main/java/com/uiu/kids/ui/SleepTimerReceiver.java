package com.uiu.kids.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.uiu.kids.Constant;
import com.uiu.kids.event.SleepModeEvent;
import com.uiu.kids.util.PreferenceUtil;

import org.greenrobot.eventbus.EventBus;

public class SleepTimerReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle bundle = intent.getExtras();
        if (intent.getAction() == "sleep_timer_action") {
            PreferenceUtil.getInstance(context).savePreference(Constant.PREF_KEY_SLEEP_MODE,false);
            EventBus.getDefault().postSticky(new SleepModeEvent(false,null));

        }

    }
}
