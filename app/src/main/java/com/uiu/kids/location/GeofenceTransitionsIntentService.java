package com.uiu.kids.location;

import android.app.IntentService;
import android.content.Intent;
import android.location.Location;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;
import com.uiu.kids.Constant;
import com.uiu.kids.R;
import com.uiu.kids.event.GeofenceEvent;
import com.uiu.kids.util.PreferenceUtil;
import com.uiu.kids.util.Util;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;




public class GeofenceTransitionsIntentService extends IntentService {

    public static String TAG = "GeofenceTransitionsIntentService";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     *
     */
    public GeofenceTransitionsIntentService() {
        super(TAG);
    }


    protected void onHandleIntent(Intent intent) {
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {
            String errorMessage = GeofenceErrorMessages.getErrorString(this,
                    geofencingEvent.getErrorCode());
            Log.e(TAG, errorMessage);
            return;
        }

        // Get the transition type.
        int geofenceTransition = geofencingEvent.getGeofenceTransition();
        Location geoLocation = geofencingEvent.getTriggeringLocation();
       // if(geoLocation.getSpeed()<1) // To make sure device is actually moving or changing location
       //     return;
        // Test that the reported transition was of interest.
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER ||
                geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {

            // Get the geofences that were triggered. A single event can trigger
            // multiple geofences.
            List<Geofence> triggeringGeofences = geofencingEvent.getTriggeringGeofences();

            // Get the transition details as a String.
            String geofenceTransitionDetails = getGeofenceTransitionDetails(
                    geofenceTransition,
                    triggeringGeofences
            );

            for(Geofence geofence:triggeringGeofences){
               com.uiu.kids.model.Location location= PreferenceUtil.getInstance(this).getSafePlaces(geofence.getRequestId());
                if(location!=null)
                    EventBus.getDefault().post(new GeofenceEvent(geofenceTransition,location));
            }

            Log.i(TAG, geofenceTransitionDetails);
        } else {
            // Log the error.
            Log.e(TAG, getString(R.string.geofence_transition_invalid_type)+" :"+ geofenceTransition);
        }
    }

    private String getGeofenceTransitionDetails(
            int geofenceTransition,
            List<Geofence> triggeringGeofences) {

        String geofenceTransitionString = getTransitionString(geofenceTransition);
        ArrayList triggeringGeofencesIdsList = new ArrayList();
        for (Geofence geofence : triggeringGeofences) {
            triggeringGeofencesIdsList.add(geofence.getRequestId());
        }
        String triggeringGeofencesIdsString = TextUtils.join(", ", triggeringGeofencesIdsList);

        return geofenceTransitionString + ": " + triggeringGeofencesIdsString;
    }

    private String getTransitionString(int transitionType) {
        switch (transitionType) {
            case Geofence.GEOFENCE_TRANSITION_ENTER:
                return getString(R.string.entering_geofence);
            case Geofence.GEOFENCE_TRANSITION_EXIT:
                return getString(R.string.exiting_geofence);
            default:
                return "Unknown Geo points";
        }
    }
}