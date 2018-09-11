package com.uiu.kids.notifications;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.uiu.kids.Constant;
import com.uiu.kids.R;
import com.uiu.kids.event.NotificationReceiveEvent;
import com.uiu.kids.model.User;
import com.uiu.kids.source.RetrofitHelper;
import com.uiu.kids.ui.home.helper.HelperEntity;
import com.uiu.kids.util.NotificationUtil;
import com.uiu.kids.util.PreferenceUtil;


import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

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

                String title = remoteMessage.getData().get("title");

                if(remoteMessage.getData().containsKey("file_type")){
                    String fileType = remoteMessage.getData().get("file_type");
                    String fileUrl = remoteMessage.getData().get("file_url");
                    NotificationUtil.create(getApplicationContext(), R.mipmap.ic_kid_launcher,title, RetrofitHelper.BASE_URL.concat(fileUrl));
                }else if(remoteMessage.getData().containsKey("object")) {
                    JSONObject jsonObject = new JSONObject(remoteMessage.getData().get("object"));
                    String message = remoteMessage.getData().get("message");
                    int status = Integer.valueOf(jsonObject.getInt("request_status"));
                    int notificationType = Integer.valueOf(remoteMessage.getData().get("notification_type"));
                    onPrimaryActions(notificationType,status,jsonObject);
                    EventBus.getDefault().postSticky(new NotificationReceiveEvent(title,message,jsonObject, notificationType,status));
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

    public void onPrimaryActions(int notificationType, int status, JSONObject jsonObject){
        if(notificationType== Constant.PRIMARY_PARENT_ADD || notificationType== PRIMARY_PARENT_REMOVE){
            if(status==ACCEPTED) {
                HelperEntity entity =  new Gson().fromJson(jsonObject.toString(),HelperEntity.class);
                User user = PreferenceUtil.getInstance(this).getAccount();
                user.setPrimaryHelper(notificationType==PRIMARY_PARENT_REMOVE?null:entity);
                PreferenceUtil.getInstance(this).saveAccount(user);
            }
        }
    }



}
