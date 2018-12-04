package com.uiu.kids.ui.slides.reminder;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import static com.google.android.gms.stats.GCoreWakefulBroadcastReceiver.startWakefulService;

public class AlarmReceiver
     extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

        //Stop sound service to play sound for alarm
        context.startService(new Intent(context, AlarmSoundService.class));

        //This will send a notification message and show notification in notification tray
        ComponentName comp = new ComponentName(context.getPackageName(),
                AlarmNotificationService.class.getName());
        startWakefulService(context, (intent.setComponent(comp)));

    }
}
