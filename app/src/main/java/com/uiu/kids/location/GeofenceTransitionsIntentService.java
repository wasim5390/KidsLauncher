package com.uiu.kids.location;

import android.app.IntentService;
import android.content.Intent;
import android.location.Location;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;
import com.uiu.kids.R;
import com.uiu.kids.event.GeofenceEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import static com.uiu.kids.location.BackgroundGeoFenceService.KEY_GEOFENCE_EXTRA;


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
            Toast.makeText(getApplicationContext(), geofenceTransitionDetails, Toast.LENGTH_SHORT).show();

           // com.uiu.kids.model.Location trackerLocation =(com.uiu.kids.model.Location) intent.getSerializableExtra(KEY_GEOFENCE_EXTRA);
            // Send notification and log the transition details.
            // sendNotification(geofenceTransitionDetails);
          //  EventBus.getDefault().post(new GeofenceEvent(geofenceTransition,trackerLocation));

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