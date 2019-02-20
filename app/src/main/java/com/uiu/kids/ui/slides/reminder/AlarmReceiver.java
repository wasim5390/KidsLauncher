package com.uiu.kids.ui.slides.reminder;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.uiu.kids.Constant;
import com.uiu.kids.event.ReminderRecieveEvent;
import com.uiu.kids.util.Util;

import org.greenrobot.eventbus.EventBus;

import static com.google.android.gms.stats.GCoreWakefulBroadcastReceiver.startWakefulService;

public class AlarmReceiver
     extends BroadcastReceiver {

    String nextAlarmTime;
    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle bundle = intent.getExtras();
        if (intent.getAction() == "sync_timer_action") {
           // nextAlarmTime = bundle.getString("time");
           // Util.vibrateDevice(context);
            Log.e("SyncTime:","Reached");
            EventBus.getDefault().post(new DataSyncEvent());
        }
    }

    public static class DataSyncEvent{

        public DataSyncEvent() {
        }
    }
}
