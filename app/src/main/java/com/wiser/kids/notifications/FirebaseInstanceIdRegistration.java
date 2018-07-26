package com.wiser.kids.notifications;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.wiser.kids.Constant;
import com.wiser.kids.util.PreferenceUtil;

public class FirebaseInstanceIdRegistration extends FirebaseInstanceIdService implements Constant {
    private static final String TAG = "FirebaseInstanceIdRged";

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        PreferenceUtil.getInstance(getApplicationContext()).savePreference(PREF_NOTIFICATION_TOKEN,refreshedToken);
        Log.d(TAG, "Refreshed token: " + refreshedToken);
    }

}
