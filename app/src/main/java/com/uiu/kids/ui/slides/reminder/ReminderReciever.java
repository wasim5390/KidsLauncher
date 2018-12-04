package com.uiu.kids.ui.slides.reminder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;

import com.uiu.kids.Constant;
import com.uiu.kids.KidsLauncherApp;
import com.uiu.kids.event.ReminderRecieveEvent;

import org.greenrobot.eventbus.EventBus;

public class ReminderReciever extends BroadcastReceiver {

    public MediaPlayer mp = new MediaPlayer();
    private int index;
    private String title;
    private String note;

    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle bundle = intent.getExtras();

        index = bundle.getInt("index");

        title=bundle.getString("title");

        note=bundle.getString("note");

        Log.e("title=",title+" note ="+note);


        if (intent.getAction() == "alarm_action") {
                EventBus.getDefault().post(new ReminderRecieveEvent(index, Constant.SLIDE_INDEX_REMINDERS,title,note));

        }

    }
}
