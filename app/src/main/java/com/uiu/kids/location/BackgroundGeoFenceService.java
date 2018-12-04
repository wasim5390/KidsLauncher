package com.uiu.kids.location;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.uiu.kids.event.LocationUpdateEvent;
import com.uiu.kids.model.User;
import com.uiu.kids.model.response.BaseResponse;
import com.uiu.kids.source.DataSource;
import com.uiu.kids.source.Repository;
import com.uiu.kids.util.PreferenceUtil;
import com.uiu.kids.util.Util;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by sidhu on 4/23/2018.
 */

public class BackgroundGeoFenceService extends Service implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    protected static final String TAG = "LocationService";
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 5000;
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 3000;
    public static final String KEY_GEOFENCE_EXTRA = "geofence_extra";
    protected GoogleApiClient mGoogleApiClient;
    protected LocationRequest mLocationRequest;

    private LocationCallback mLocationCallback;
    private FusedLocationProviderClient mLocationProviderClient;


    private GeofencingClient mGeofencingClient;


    private List<Geofence> mGeofenceList = new ArrayList<>();
    private PendingIntent mGeofencePendingIntent;
    public static BackgroundGeoFenceService instance;

    public static BackgroundGeoFenceService getInstance() {
        return instance;

    }

    private GeofencingRequest getGeofencingRequest(List<Geofence> geofence) {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(geofence);
        return builder.build();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate()");
        instance = this;
        buildGoogleApiClient();
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        if (mGoogleApiClient.isConnected()) {
            Log.i(" onStartCommand", "GoogleApiClient Connected");
            return START_STICKY;
        }

        if (!mGoogleApiClient.isConnected() || !mGoogleApiClient.isConnecting()) {
            Log.i(" onStartCommand", "GoogleApiClient not Connected");
            mGoogleApiClient.connect();
        }
        return START_STICKY;
    }

    protected synchronized void buildGoogleApiClient() {
        Log.i(TAG, "Building GoogleApiClient");

        mGeofencingClient = LocationServices.getGeofencingClient(this);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        createLocationRequest();


    }


    @SuppressLint("RestrictedApi")
    public void createLocationRequest() {
        Log.i(TAG, "createLocationRequest()");
        if(mLocationRequest==null)
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(1f); // 1 meter
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);
        builder.setAlwaysShow(true);
        Task<LocationSettingsResponse> task =
                LocationServices.getSettingsClient(getApplicationContext()).checkLocationSettings(builder.build());
        task.addOnSuccessListener(locationSettingsResponse -> {
            startLocationUpdates();
        });
        task.addOnFailureListener(e -> {
            if (e instanceof ResolvableApiException) {
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                // Show the dialog by calling startResolutionForResult(),
                // and check the result in onActivityResult().
                ResolvableApiException resolvable = (ResolvableApiException) e;
                LocationServiceEnableEvent locationServiceEnableEvent = new LocationServiceEnableEvent(true);
                locationServiceEnableEvent.setException(resolvable);
                EventBus.getDefault().post(locationServiceEnableEvent);

            }
        });

    }

    public void startLocationUpdates() {
        Log.i(TAG, "Started Location Updates");

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    onLocationChanged(location);
                }
            }

            @Override
            public void onLocationAvailability(LocationAvailability locationAvailability) {
                super.onLocationAvailability(locationAvailability);
            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null);
    }


    public void addGeoFences(List<com.uiu.kids.model.Location> locations) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return;
        }
            mGeofenceList.clear();
        for (com.uiu.kids.model.Location location : locations) {
           Geofence geofence = Util.createGeofence(location.getId(),location.getLatitude(), location.getLongitude());
            mGeofenceList.add(geofence);
        }
        mGeofencingClient.addGeofences(getGeofencingRequest(mGeofenceList), getGeofencePendingIntent(locations))
                .addOnSuccessListener(aVoid -> {
                    // Geofences added
                    // ...
                    // Toast.makeText(instance, "GeoFences Added", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    // Failed to add geofences
                    // ...
                    //  Toast.makeText(instance, "GeoFences Not added Added: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private PendingIntent getGeofencePendingIntent(List<com.uiu.kids.model.Location> location) {
        // Reuse the PendingIntent if we already have it.
        if (mGeofencePendingIntent != null) {
            return mGeofencePendingIntent;
        }
        Intent intent = new Intent(this, GeofenceTransitionsIntentService.class);
       // intent.putExtra(KEY_GEOFENCE_EXTRA, (Serializable) location);
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when
        // calling addGeofences() and removeGeofences().
       mGeofencePendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.
                FLAG_UPDATE_CURRENT);
        return mGeofencePendingIntent;
    }


    protected void stopLocationUpdates() {
        Log.i(TAG,"Stopped Location Updates");
        if(mLocationCallback!=null) {
            mLocationProviderClient.removeLocationUpdates(mLocationCallback);
        }
    }



    @Override
    public void onConnected(Bundle connectionHint) {
        Log.i(TAG, "Connected to GoogleApiClient");
        startLocationUpdates();
    }

    /**
     * Callback that fires when the location changes.
     */
    @Override
    public void onLocationChanged(Location location) {

        float hAccuracy = location.getAccuracy();
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        float speed = location.getSpeed();
        float course = location.getBearing();
        long time = location.getTime();
        com.uiu.kids.model.Location updatedLocation = new com.uiu.kids.model.Location( longitude, latitude, course, speed, time,hAccuracy);

        updateLocationOnServer(updatedLocation);
    }


    @Override
    public void onConnectionSuspended(int cause) {
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopLocationUpdates();
    }

    private void updateLocationOnServer(com.uiu.kids.model.Location location){
        User  user = PreferenceUtil.getInstance(this).getAccount();
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