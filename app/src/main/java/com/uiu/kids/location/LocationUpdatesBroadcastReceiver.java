package com.uiu.kids.location;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.location.LocationResult;
import com.uiu.kids.KidsLauncherApp;
import com.uiu.kids.model.User;
import com.uiu.kids.model.response.BaseResponse;
import com.uiu.kids.source.DataSource;
import com.uiu.kids.source.Repository;
import com.uiu.kids.util.PreferenceUtil;
import com.uiu.kids.util.Util;

import java.util.HashMap;
import java.util.List;

/**
 * Receiver for handling location updates.
 *
 * For apps targeting API level O
 * {@link android.app.PendingIntent#getBroadcast(Context, int, Intent, int)} should be used when
 * requesting location updates. Due to limits on background services,
 * {@link android.app.PendingIntent#getService(Context, int, Intent, int)} should not be used.
 *
 *  Note: Apps running on "O" devices (regardless of targetSdkVersion) may receive updates
 *  less frequently than the interval specified in the
 *  {@link com.google.android.gms.location.LocationRequest} when the app is no longer in the
 *  foreground.
 */
public class LocationUpdatesBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "LUBroadcastReceiver";

    public static final String ACTION_PROCESS_UPDATES =
            "com.uiu.kids.location.action" +
                    ".PROCESS_UPDATES";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_PROCESS_UPDATES.equals(action)) {
                LocationResult result = LocationResult.extractResult(intent);
                if (result != null) {
                    List<Location> locations = result.getLocations();
                    for(Location location:locations){
                        onLocationChanged(location);
                        Util.vibrateDevice(context);
                    }

                }
            }
        }
    }

    /**
     * Callback that fires when the location changes.
     */
    public void onLocationChanged(Location location) {

        float hAccuracy = location.getAccuracy();
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        float speed = location.getSpeed();
        float course = location.getBearing();
        long time = location.getTime();
        com.uiu.kids.model.Location updatedLocation = new com.uiu.kids.model.Location( longitude, latitude, course, speed, time,hAccuracy);
        // if(speed>0)
        updateLocationOnServer(updatedLocation);
    }

    private void updateLocationOnServer(com.uiu.kids.model.Location location){
        User user = PreferenceUtil.getInstance(KidsLauncherApp.getInstance()).getAccount();
        if(user.getId()==null)
            return;
        HashMap<String,Object> params = new HashMap<>();
        params.put("user_id", user.getId());
        params.put("location",location);
        Repository.getInstance().updateKidsLocation(params, new DataSource.GetResponseCallback<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse response) {

            }

            @Override
            public void onFailed(int code, String message) {

            }
        });
    }
}
