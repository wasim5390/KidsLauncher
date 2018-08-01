package com.wiser.kids.notifications;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.wiser.kids.Constant;
import com.wiser.kids.event.NotificationReceiveEvent;
import com.wiser.kids.util.PreferenceUtil;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class KidsFirebaseMessaging extends FirebaseMessagingService implements Constant{
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
                JSONObject jsonObject = new JSONObject(remoteMessage.getData().get("object"));
                String title = remoteMessage.getData().get("title");
                String message = remoteMessage.getData().get("message");
                int notificationType = Integer.valueOf(remoteMessage.getData().get("notification_type"));

                Log.e(TAG, "title: " + title);
                Log.e(TAG, "message: " + message);
                Log.e(TAG, "notificationType: " + notificationType);
                EventBus.getDefault().postSticky(new NotificationReceiveEvent(jsonObject,notificationType));

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
