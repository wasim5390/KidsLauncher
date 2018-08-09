package com.wiser.kids.ui.reminder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.wiser.kids.Constant;
import com.wiser.kids.event.ReminderRecieveEvent;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

public class ReminderReciever extends BroadcastReceiver {

    public MediaPlayer mp = new MediaPlayer();
    private int index = 0;

    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle bundle = intent.getExtras();

        index = bundle.getInt("index");


        if (intent.getAction() == "alarm_action") {
            if (index != 0) {

                EventBus.getDefault().post(new ReminderRecieveEvent(index, Constant.SLIDE_INDEX_REMINDERS));

//                Uri ringToneUri = RingtoneManager.getActualDefaultRingtoneUri(context, RingtoneManager.TYPE_NOTIFICATION);
//                Log.e("Alert uri", String.valueOf(ringToneUri));
//                mp = new MediaPlayer();
//                try {
//                    mp.setDataSource(context, ringToneUri);
//                    mp.setAudioStreamType(AudioManager.STREAM_RING);
//                    mp.prepare();
//                    mp.start();
//                } catch (IOException e) {
//                    Log.e("Media Error", String.valueOf(e));
//                    e.printStackTrace();
//                }

            }

        }


    }
}
