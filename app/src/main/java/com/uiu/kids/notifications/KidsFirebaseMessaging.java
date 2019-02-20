package com.uiu.kids.notifications;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.uiu.kids.Constant;
import com.uiu.kids.R;
import com.uiu.kids.event.ShareEvent;
import com.uiu.kids.event.notification.NotificationReceiveEvent;
import com.uiu.kids.model.LocalNotificationModel;
import com.uiu.kids.model.NotificationSender;
import com.uiu.kids.model.Setting;
import com.uiu.kids.source.RetrofitHelper;

import com.uiu.kids.ui.SleepActivity;
import com.uiu.kids.util.NotificationUtil;
import com.uiu.kids.util.PreferenceUtil;
import com.uiu.kids.util.Util;


import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.Calendar;

public class KidsFirebaseMessaging extends FirebaseMessagingService implements Constant {
    public static String TAG="MyFirebaseMessagingService";

    int count=0;
    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.d(TAG, "Refreshed token: " + s);
        PreferenceUtil.getInstance(getApplicationContext()).savePreference(PREF_NOTIFICATION_TOKEN,s);
    }


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            // handle data here
            try {
                String image=null;
                String title = remoteMessage.getData().get("title");
                if(remoteMessage.getData().containsKey("image"))
                     image= remoteMessage.getData().get("image");
                JSONObject sender = new JSONObject(remoteMessage.getData().get("sender"));
                NotificationSender notificationSender = new Gson().fromJson(sender.toString(), NotificationSender.class);

                if(remoteMessage.getData().containsKey("file_type")){
                    int fileType = Integer.valueOf(remoteMessage.getData().get("file_type"));
                    String fileUrl = remoteMessage.getData().get("file_url");
                    String created_at = remoteMessage.getData().get("created_at");
                    ShareEvent event = new ShareEvent(fileType,fileUrl,title,created_at);
                    event.setSender(notificationSender);
                    if(remoteMessage.getData().containsKey("thumbnail"))
                        event.setThumbnailUrl(remoteMessage.getData().get("thumbnail"));
                    EventBus.getDefault().postSticky(event);
                   // NotificationUtil.create(getApplicationContext(), R.mipmap.ic_kid_launcher,title, );
                }

                else if(remoteMessage.getData().containsKey("object")) {
                    JSONObject jsonObject = new JSONObject(remoteMessage.getData().get("object"));
                    String message = remoteMessage.getData().get("message");
                    int status = Integer.valueOf(jsonObject.getInt("request_status"));
                    int notificationType = Integer.valueOf(remoteMessage.getData().get("notification_type"));
                    if(notificationType==REQ_BEEP) {
                        findPhoneAsRinging();
                        return;
                    }
                    if(notificationType == Constant.REQ_SETTINGS){
                        Setting setting =  new Gson().fromJson(jsonObject.toString(),Setting.class);
                        Util.updateSystemSettings(getApplicationContext(),setting);
                       // EventBus.getDefault().postSticky(new SleepModeEvent(setting.isSleepMode(),setting.getSleepTime()));
                        if(setting.isSleepMode()) {
                            Intent intent = new Intent(getApplicationContext(), SleepActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(new Intent(getApplicationContext(), SleepActivity.class));
                        }
                        if(setting.getSleepTime()!=null && setting.isTimedSleepEnable())
                            setSleepAlarm(setting.getSleepTime(),getApplicationContext());
                        return;
                    }
                    LocalNotificationModel notificationModel = new LocalNotificationModel(notificationSender,jsonObject,message,image,status);

                    SlidesDataUpdater.getInstance().update(notificationType,notificationModel);

                }

            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

    }


    public void findPhoneAsRinging(){

        if(mMediaPlayerForService !=null && mMediaPlayerForService.isPlaying())
            return;
        try {
            Uri uri=Uri.parse("android.resource://"+getPackageName()+"/raw/beep");
            mMediaPlayerForService.reset();
            mMediaPlayerForService.setDataSource(getApplicationContext(), uri);
            final AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

            final int originalVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC),
                    0);


                mMediaPlayerForService.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mMediaPlayerForService.setLooping(false);
                mMediaPlayerForService.prepare();
                mMediaPlayerForService.start();
                mMediaPlayerForService.setOnCompletionListener(mp -> {

                    count++;
                    if(count<4)
                        mMediaPlayerForService.start();
                    else {
                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, originalVolume, 0);
                        mMediaPlayerForService.reset();
                        count = 0;
                    }
                });
        } catch(Exception e) {
        }
    }



    public void setSleepAlarm(String time, Context context) {
        AlarmManager alarmManager;

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.valueOf(time));
        Intent intent = new Intent("sleep_timer_action");
        Bundle bundle = new Bundle();
        intent.putExtras(bundle);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,0x123, intent, 0);
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTime().getTime(),
                pendingIntent);


            /*    if (entities.get(i).getIs_repeated()) {

                    alarmManager[i].setInexactRepeating(AlarmManager.RTC_WAKEUP, entities.get(i).getdate().getTime(), 24 * 60 * 60 * 1000, pendingIntent);

                } else {
                    alarmManager.set(AlarmManager.RTC_WAKEUP, entities.get(i).getdate().getTime(),
                            pendingIntent);
                }*/

    }

}
