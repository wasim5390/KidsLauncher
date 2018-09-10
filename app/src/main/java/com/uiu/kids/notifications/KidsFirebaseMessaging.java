package com.uiu.kids.notifications;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.uiu.kids.Constant;
import com.uiu.kids.R;
import com.uiu.kids.event.NotificationReceiveEvent;
import com.uiu.kids.source.RetrofitHelper;
import com.uiu.kids.util.NotificationUtil;
import com.uiu.kids.util.PreferenceUtil;


import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

public class KidsFirebaseMessaging extends FirebaseMessagingService implements Constant {
    public static String TAG="MyFirebaseMessagingService";

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

}
