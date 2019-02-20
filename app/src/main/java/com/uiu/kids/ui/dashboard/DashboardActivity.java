package com.uiu.kids.ui.dashboard;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.location.Location;
import android.location.LocationManager;
import android.media.AudioManager;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.shashank.sony.fancydialoglib.FancyAlertDialog;
import com.shashank.sony.fancydialoglib.Icon;
import com.squareup.picasso.Picasso;
import com.uiu.kids.BaseActivity;
import com.uiu.kids.Constant;
import com.uiu.kids.Injection;
import com.uiu.kids.KidsLauncherApp;
import com.uiu.kids.R;
import com.uiu.kids.event.GeofenceEvent;
import com.uiu.kids.event.InviteUpdatedEvent;
import com.uiu.kids.event.LocationUpdateEvent;
import com.uiu.kids.event.SettingsEvent;
import com.uiu.kids.event.SlideEvent;
import com.uiu.kids.event.notification.AppNotificationEvent;
import com.uiu.kids.event.notification.InviteNotificationEvent;
import com.uiu.kids.event.notification.LinkNotificationEvent;
import com.uiu.kids.event.notification.NotificationReceiveEvent;
import com.uiu.kids.event.ReminderRecieveEvent;
import com.uiu.kids.event.ShareEvent;
import com.uiu.kids.event.SleepModeEvent;
import com.uiu.kids.event.notification.PeopleNotificationEvent;
import com.uiu.kids.event.notification.ReminderNotificationEvent;
import com.uiu.kids.event.notification.SafePlacesNotificationEvent;
import com.uiu.kids.location.LocationServiceEnableEvent;
import com.uiu.kids.location.LocationUpdatesBroadcastReceiver;
import com.uiu.kids.model.Invitation;
import com.uiu.kids.model.NotificationSender;
import com.uiu.kids.model.User;
import com.uiu.kids.model.response.InvitationResponse;
import com.uiu.kids.source.DataSource;
import com.uiu.kids.source.Repository;
import com.uiu.kids.ui.SleepActivity;

import com.uiu.kids.ui.SmsReceiver;
import com.uiu.kids.ui.c_me.AudioPlayBackView;
import com.uiu.kids.ui.c_me.VideoPlaybackActivity;
import com.uiu.kids.ui.call.CallReceiver;
import com.uiu.kids.ui.slides.invitation.InvitationConfirmationCallback;
import com.uiu.kids.ui.share.UploadFileService;
import com.uiu.kids.util.PermissionUtil;
import com.uiu.kids.util.PreferenceUtil;
import com.uiu.kids.util.Util;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.IntentFilter.SYSTEM_HIGH_PRIORITY;


public class DashboardActivity extends BaseActivity implements PermissionUtil.PermissionCallback,
        SmsReceiver.Listener
{


    private static final int CODE_DRAW_OVER_OTHER_APP_PERMISSION = 2084;
    private static final String TAG = "DashboardActivity";

    DashboardFragment dashboardFragment;
    DashboardPresenter dashboardPresenter;


    int countToPush=0;
    int oldRingerMode=0;

    String activityLoadedFrom="Normal";

    @BindView(R.id.tick)
    ImageView tick;


    private WindowManager wmanager;

    private View view;




    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 30000;//Every 30 sec
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 10000; //Every 10 sec
    private static final long MAX_WAIT_TIME = UPDATE_INTERVAL_IN_MILLISECONDS*4; // 2 Minutes

    protected LocationRequest mLocationRequest;
    private LocationCallback locationCallback;

    private FusedLocationProviderClient mLocationProviderClient;
    private  BroadcastReceiver callReceiver;



    @Override
    public int getID() {
        return R.layout.activity_dashboard;
    }

    @Override
    public void created(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        // wmanager = ((WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE));
        // setFullScreenMode();
        ButterKnife.bind(this);

        String bg = PreferenceUtil.getInstance(this).getPreference(Constant.KEY_SELECTED_BG);
        if(bg.isEmpty())
            getWindow().setBackgroundDrawableResource(R.color.white);
        else
            applyBg(bg);

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(this, instanceIdResult -> {
            String deviceToken = instanceIdResult.getToken();
            PreferenceUtil.getInstance(this).savePreference(PREF_NOTIFICATION_TOKEN,deviceToken);
        });
        Intent intent = getIntent();
        if(intent!=null && intent.hasExtra("ActionFrom"))
            activityLoadedFrom = intent.getStringExtra("ActionFrom");
        onBobblePermission();
        if(KidsLauncherApp.getInstance().isSleepModeActive())
        {
            startActivity(new Intent(this, SleepActivity.class));
        }
        buildGoogleApiClient();
        createLocationRequest();
        callReceiver = new CallReceiver();
    }


    protected synchronized void buildGoogleApiClient() {
        Log.i(TAG, "Building FusedLocationClient");

        mLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


    }


    @SuppressLint("RestrictedApi")
    public void createLocationRequest() {
        Log.i(TAG, "createLocationRequest()");
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(1f); // 1 meter
        // mLocationRequest.setMaxWaitTime(MAX_WAIT_TIME);
    }


    private  void checkLocationServiceEnable(){
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);
        builder.setAlwaysShow(true);
        Task<LocationSettingsResponse> task =
                LocationServices.getSettingsClient(getApplicationContext()).checkLocationSettings(builder.build());
        task.addOnSuccessListener(locationSettingsResponse -> {
            requestLocationUpdates();
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


    private PendingIntent getPendingIntent() {
        // Note: for apps targeting API level 25 ("Nougat") or lower, either
        // PendingIntent.getService() or PendingIntent.getBroadcast() may be used when requesting
        // location updates. For apps targeting API level O, only
        // PendingIntent.getBroadcast() should be used. This is due to the limits placed on services
        // started in the background in "O".

        // TODO(developer): uncomment to use PendingIntent.getService().
//        Intent intent = new Intent(this, LocationUpdatesIntentService.class);
//        intent.setAction(LocationUpdatesIntentService.ACTION_PROCESS_UPDATES);
//        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent intent = new Intent(this, LocationUpdatesBroadcastReceiver.class);
        intent.setAction(LocationUpdatesBroadcastReceiver.ACTION_PROCESS_UPDATES);
        return PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

    }


    /**
     * Handles the Request Updates button and requests start of location updates.
     */
    public void requestLocationUpdates() {
        try {
            Log.i(TAG, "Starting location updates");
            mLocationProviderClient.requestLocationUpdates(mLocationRequest, getLocationCallback(),null);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    /**
     * get and register LocationCallback for Location request.
     * @return
     */
    public LocationCallback getLocationCallback() {
        if(locationCallback==null)
            locationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    if (locationResult == null) {
                        return;
                    }
                    for (Location location : locationResult.getLocations()) {
                        if (location != null) {
                            updateLocation(location);
                        }
                    }
                }
            };
        return locationCallback;
    }

    /**
     * update new Location on server
     * @param newLocation
     */
    private void updateLocation(Location newLocation){
        User user = PreferenceUtil.getInstance(KidsLauncherApp.getInstance()).getAccount();
        if(user.getId()==null)
            return;
        HashMap<String,Object> params = new HashMap<>();
        params.put("user_id", user.getId());
        params.put("location",composeLocationToUpdate(newLocation));
        dashboardPresenter.updateKidLocation(params);
      //  Util.vibrateDevice(DashboardActivity.this);
    }

    /**
     * Convert Location object to Local Location pojo
     * @param location
     * @return
     */
    private com.uiu.kids.model.Location composeLocationToUpdate(Location location){
        float hAccuracy = location.getAccuracy();
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        float speed = location.getSpeed();
        float course = location.getBearing();
        long time = location.getTime();
        return new com.uiu.kids.model.Location( longitude, latitude, course, speed, time,hAccuracy);

    }

    /**
     * Handles the Remove Updates button, and requests removal of location updates.
     */
    public void removeLocationUpdates(View view) {
        Log.i(TAG, "Removing location updates");

        mLocationProviderClient.removeLocationUpdates(getPendingIntent());
    }





    private void setFullScreenMode(){
        WindowManager.LayoutParams localLayoutParams = new WindowManager.LayoutParams();

        localLayoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;

        localLayoutParams.gravity = Gravity.BOTTOM;

        localLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |

                // this is to enable the notification to receive touch events

                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |

                // Draws over status bar

                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;

// set the size of statusbar blocker

        localLayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;

        localLayoutParams.height = (int) (30 * getResources().getDisplayMetrics().scaledDensity);

        localLayoutParams.format = PixelFormat.TRANSPARENT;

        view = new View(this);

        wmanager.addView(view, localLayoutParams);

//This Code is used to Disable Title bar and Notification Permanently

        wmanager = ((WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE));

        WindowManager.LayoutParams localLayoutParamsTop = new WindowManager.LayoutParams();

        localLayoutParamsTop.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;

        localLayoutParamsTop.gravity = Gravity.TOP;

        localLayoutParamsTop.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |

                // this is to enable the notification to receive touch events

                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |

                // Draws over status bar

                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;

// set the size of statusbar blocker

        localLayoutParamsTop.width = WindowManager.LayoutParams.MATCH_PARENT;

        localLayoutParamsTop.height = (int) (30 * getResources().getDisplayMetrics().scaledDensity);

        localLayoutParamsTop.format = PixelFormat.TRANSPARENT;

        view = new View(this);

        wmanager.addView(view, localLayoutParamsTop);
    }


    public void onBobblePermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {

            //If the draw over permission is not available open the settings screen
            //to grant the permission.
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, CODE_DRAW_OVER_OTHER_APP_PERMISSION);
        }else
            PermissionUtil.requestPermissions(this,this);
    }

    @Override
    protected void onStart() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
        filter.addAction(Intent.ACTION_BATTERY_LOW);
        filter.addAction(AudioManager.RINGER_MODE_CHANGED_ACTION);
        registerReceiver(mReceiver, filter);
        registerReceiver(mGpsSwitchStateReceiver, new IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION));
        registerReceiver(onDownloadComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        filter  = new IntentFilter();
        filter.addAction(TelephonyManager.ACTION_PHONE_STATE_CHANGED);
        filter.addAction(Intent.ACTION_NEW_OUTGOING_CALL);
        filter.setPriority(SYSTEM_HIGH_PRIORITY);
        registerReceiver(callReceiver,filter);
        LocalBroadcastManager.getInstance(this).registerReceiver(immersiveReceiver,new IntentFilter("immersive"));

        super.onStart();
    }
    @Override
    protected void onStop() {
        unregisterReceiver(mReceiver);
        unregisterReceiver(mGpsSwitchStateReceiver);
        super.onStop();
    }



    private void loadDashboardFragment() {
        dashboardFragment = dashboardFragment != null ? dashboardFragment : DashboardFragment.newInstance(activityLoadedFrom);
        dashboardPresenter = dashboardPresenter != null ? dashboardPresenter : new DashboardPresenter(dashboardFragment, PreferenceUtil.getInstance(this), Injection.provideRepository(this));
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, dashboardFragment);
        fragmentTransaction.commitAllowingStateLoss();
    }
    @Override
    public void onDestroy() {
      /*  if (view != null) {
            wmanager.removeView(view);

        }*/
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if(mLocationProviderClient!=null && locationCallback!=null)
        mLocationProviderClient.removeLocationUpdates(locationCallback);
        unregisterReceiver(onDownloadComplete);
        unregisterReceiver(callReceiver);

    }

    @Override
    protected void onResume() {
        super.onResume();
        gotoSleepMode();
    }

    private void gotoSleepMode(){
        if(KidsLauncherApp.getInstance().isSleepModeActive())
        {
            startActivity(new Intent(this, SleepActivity.class));
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(SleepModeEvent sleepModeEvent) {
        if(sleepModeEvent.isEnable())
            gotoSleepMode();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(NotificationReceiveEvent receiveEvent) {

        int notificationType = receiveEvent.getNotificationForSlideType();
        if(notificationType==REQ_BEEP) {
            return;
        }

        if(!receiveEvent.isSlideUpdate())
            showNotification(receiveEvent.getTitle(),receiveEvent.getMessage(),receiveEvent.getStatus());

        if(notificationType==SLIDE_CREATE_INDEX || notificationType==SLIDE_REMOVE_INDEX)
            dashboardPresenter.loadUpdatedSlides();

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(SettingsEvent receiveEvent) {
        updateBrightness(receiveEvent.getBrightness());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(PeopleNotificationEvent receiveEvent) {
        showNotification(receiveEvent.getMessage(),receiveEvent.getContactEntity().getName(),receiveEvent.getSender(),receiveEvent.getStatus(),receiveEvent.getImage());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(AppNotificationEvent receiveEvent) {
        showNotification(receiveEvent.getMessage(),receiveEvent.getAppsEntity().getName(),receiveEvent.getSender(),receiveEvent.getStatus(),receiveEvent.getImage());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(LinkNotificationEvent receiveEvent) {
        showNotification(receiveEvent.getMessage(),receiveEvent.getLinkEntity().getTitle(),receiveEvent.getSender(),receiveEvent.getStatus(),receiveEvent.getImage());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ReminderNotificationEvent receiveEvent) {
        showNotification(receiveEvent.getMessage(),receiveEvent.getReminderEntity().getTitle(),receiveEvent.getSender(),receiveEvent.getStatus(),receiveEvent.getImage());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(SafePlacesNotificationEvent receiveEvent) {
        showNotification(receiveEvent.getMessage(),receiveEvent.getLocationEntity().getTitle(),receiveEvent.getSender(),receiveEvent.getStatus(),receiveEvent.getImage());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(InviteNotificationEvent receiveEvent) {
        if(receiveEvent.getInvitation().getStatus() == INVITE.INVITED ){
            String title = getString(R.string.please_choose_an_option);
            String msg = getString(R.string.helper_invite,receiveEvent.getInvitation().getSender().getEmail());
            showInvitationActionsDialog(getApplicationContext(), title, msg, receiveEvent.getInvitation(), new InvitationConfirmationCallback() {
                @Override
                public void onAcceptInvitation(Invitation invitation) {
                    invitation.setStatus(INVITE.CONNECTED);
                    updateInvitation(invitation);
                }

                @Override
                public void onRejectInvitation(Invitation invitation) {
                    invitation.setStatus(INVITE.REJECTED);
                    updateInvitation(invitation);
                }

                @Override
                public void onDeleteInvitation(Invitation invitation) {

                }
            });
        }
    }

    public void updateInvitation(Invitation invitation){
        Repository.getInstance().updateInvite(invitation.getInviteId(), invitation.getStatus(), PreferenceUtil.getInstance(this).getAccount().getId(), new DataSource.GetResponseCallback<InvitationResponse>() {
            @Override
            public void onSuccess(InvitationResponse response) {
                if(response.isSuccess())
                    updateLocationInvitationList(response.getInvitation());
            }

            @Override
            public void onFailed(int code, String message) {
            }
        });
    }

    private void updateLocationInvitationList(Invitation invitation){
        User user = PreferenceUtil.getInstance(this).getAccount();
        List<Invitation> helpers = new ArrayList<>();
        for(Invitation mInvitation: user.getInvitations()){
            if(mInvitation.getInviteId().equals(invitation.getInviteId()))
                mInvitation=invitation;
            helpers.add(mInvitation);
        }

        user.setInvitations(helpers);
        PreferenceUtil.getInstance(this).saveAccount(user);
        PreferenceUtil.getInstance(this).saveInvitationList(helpers);
        EventBus.getDefault().post(new SlideEvent(SLIDE_INDEX_INVITE,false));
        EventBus.getDefault().post(new InviteUpdatedEvent());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(GeofenceEvent receiveEvent) {
        String transition = receiveEvent.transition==Geofence.GEOFENCE_TRANSITION_ENTER?"Entering":"Leaving";
        String message = "Your kid is "+transition+" "+receiveEvent.getLocation().getTitle();
        // String title = receiveEvent.getLocation().getTitle();
        HashMap<String,Object> params = new HashMap<>();
        params.put("latitude",receiveEvent.getLocation().getLatitude());
        params.put("longitude",receiveEvent.getLocation().getLongitude());
        params.put("title",PreferenceUtil.getInstance(this).getAccount().getEmail());
        params.put("message",message);
        params.put("request_status",receiveEvent.transition);
        dashboardPresenter.updateKidLocationRange(params);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(LocationUpdateEvent receiveEvent) {
        HashMap<String,Object> params = new HashMap<>();
        params.put("user_id", PreferenceUtil.getInstance(this).getAccount().getId());
        params.put("location",receiveEvent.getLocation());
        if(dashboardPresenter!=null)
            dashboardPresenter.updateKidLocation(params);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ReminderRecieveEvent receiveEvent) {
        if (receiveEvent.getType() == Constant.SLIDE_INDEX_REMINDERS) {
            int index = receiveEvent.getIndex();
            String title = receiveEvent.getTitle();
            String note = receiveEvent.getNote();
            Log.e("index", String.valueOf(index));
            showReminderAlert(title, note);
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ShareEvent shareEvent) {
        if(!shareEvent.isShared())
        {
            showMediaNotification(shareEvent);
            return;
        }
        tick.setVisibility(View.INVISIBLE);
        android.view.animation.Animation expandIn = AnimationUtils.loadAnimation(this, R.anim.pop_in_view);
        expandIn.setAnimationListener(new android.view.animation.Animation.AnimationListener() {
            @Override
            public void onAnimationStart(android.view.animation.Animation animation) {

                tick.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(android.view.animation.Animation animation) {
                tick.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(android.view.animation.Animation animation) {

            }
        });
        tick.startAnimation(expandIn);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(LocationServiceEnableEvent receiveEvent) {
        try {
            ResolvableApiException resolvableApiException=receiveEvent.getException();
            resolvableApiException.startResolutionForResult(this, 1122);
            // stopLocationService();
        } catch (IntentSender.SendIntentException e) {

        }

    }

    public void showNotification( String title, String message, int status){

        new FancyAlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setBackgroundColor(Color.parseColor((status== ACCEPTED || status==INVITE.CONNECTED)?"#378718":"#C82506"))  //Don't pass R.color.colorvalue
                .setPositiveBtnBackground(Color.parseColor("#2572D9"))  //Don't pass R.color.colorvalue
                .setPositiveBtnText("OK")
                .setNegativeBtnText("Cancel")
                .setAnimation(com.shashank.sony.fancydialoglib.Animation.POP)
                .isCancellable(true)
                .setIcon(status==ACCEPTED?R.drawable.ic_done:R.drawable.ic_close, Icon.Visible)
                .OnNegativeClicked(() -> { })
                .build();
    }



    public void showNotification( String message,String title,NotificationSender sender, int status,String image){
        int notificationHeader = (status== ACCEPTED || status==INVITE.CONNECTED)?R.drawable.notification_header_bg:R.drawable.notification_header_rejected;
        int btnColor = (status== ACCEPTED || status==INVITE.CONNECTED)?R.color.notification_accepted:R.color.notification_rejected;
        Dialog dialog = new Dialog(DashboardActivity.this,R.style.Theme_Dialog);
        dialog.setContentView(R.layout.notification_view);
        dialog.setCancelable(true);
        dialog.findViewById(R.id.header).setBackgroundResource(notificationHeader);
        TextView tvMessage,tvName;
        CircleImageView ivSender;
        ImageView itemImg;
        Button okBtn;
        ivSender =  dialog.findViewById(R.id.ivSender);
        itemImg =  dialog.findViewById(R.id.ivImage);
        tvName  = dialog.findViewById(R.id.tvName);
        tvMessage =  dialog.findViewById(R.id.tvNotificationTitle);
        okBtn =  dialog.findViewById(R.id.btnOk);
        okBtn.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),btnColor));
        tvMessage.setText(message);
        tvName.setText(title);
        Picasso.get().load(sender.getSenderImage()).placeholder(R.drawable.avatar_male2).into(ivSender);
        Picasso.get().load(image).placeholder(R.drawable.avatar_male2).into(itemImg);

        dialog.show();

        okBtn.setOnClickListener(v -> {
            dialog.dismiss();
        });

    }

    public void showMediaNotification( ShareEvent shareEvent){
        int type = shareEvent.getMediaType();

        int notificationHeader = R.drawable.notification_header_bg;
        int btnColor = R.color.notification_accepted;
        Dialog dialog = new Dialog(DashboardActivity.this,R.style.Theme_Dialog);
        dialog.setContentView(R.layout.notification_view_media);
        dialog.setCancelable(false);
        dialog.findViewById(R.id.header).setBackgroundResource(notificationHeader);
        if(type==Constant.MEDIA_IMAGE) {
            btnColor = R.color.green;
            dialog.findViewById(R.id.image).setVisibility(View.VISIBLE);
            dialog.findViewById(R.id.btnDownloadVideo).setVisibility(View.GONE);
            dialog.findViewById(R.id.btnDownloadAudio).setVisibility(View.GONE);
        }
        else if(type == Constant.MEDIA_AUDIO) {
            dialog.findViewById(R.id.image).setVisibility(View.GONE);
            dialog.findViewById(R.id.btnDownloadVideo).setVisibility(View.GONE);
            dialog.findViewById(R.id.btnDownloadAudio).setVisibility(View.VISIBLE);

        }else if(type == Constant.MEDIA_VIDEO){
            dialog.findViewById(R.id.image).setVisibility(View.VISIBLE);
            dialog.findViewById(R.id.btnDownloadVideo).setVisibility(View.VISIBLE);
            dialog.findViewById(R.id.btnDownloadAudio).setVisibility(View.GONE);
        }

        TextView tvMessage;
        CircleImageView ivSender;
        ImageView image;
        Button okBtn;
        ivSender =  dialog.findViewById(R.id.ivSender);
        tvMessage =  dialog.findViewById(R.id.tvNotificationTitle);
        image = dialog.findViewById(R.id.image);
        okBtn =  dialog.findViewById(R.id.btnOk);

        okBtn.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),btnColor));
        tvMessage.setText(shareEvent.getTitle());
        Picasso.get().load(shareEvent.getSender().getSenderImage()).placeholder(R.drawable.avatar_male2).into(ivSender);
        if(type==MEDIA_IMAGE)
            Picasso.get().load(shareEvent.getFileUrl()).placeholder(R.drawable.placeholder_sqr).into(image);
        if(type==MEDIA_VIDEO) {
            image.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Picasso.get().load(shareEvent.getThumbnailUrl()).placeholder(R.drawable.placeholder_sqr).into(image);
            okBtn.setText("Download");
        }
        if(type==MEDIA_AUDIO) {
            okBtn.setText("Download");
        }

        dialog.show();

        okBtn.setOnClickListener(v -> {
            if(type==MEDIA_AUDIO | type==MEDIA_VIDEO | type==MEDIA_IMAGE)
                UploadFileService.downloadMedia(getApplicationContext(), type, shareEvent.getFileUrl(), shareEvent.getCreatedAt());
            dialog.dismiss();
        });

    }


    public void showAudioNotification(String uri){

        Dialog dialog = new Dialog(DashboardActivity.this,R.style.Theme_Dialog);
        LayoutInflater inflater = getLayoutInflater();
        AudioPlayBackView view = (AudioPlayBackView) inflater.inflate(R.layout.audio_playback_view,null);
        view.setDataSource(Uri.parse(uri),false);
        dialog.setContentView(view);
        dialog.setCancelable(true);
        dialog.show();
        dialog.findViewById(R.id.btnCancel).setOnClickListener(v -> {
            dialog.dismiss();
        });
        dialog.setOnDismissListener(dialog1 -> {
            view.stopMediaPlayer();
        });
    }








    @Override
    public void onPermissionsGranted(String permission) {

    }

    @Override
    public void onPermissionsGranted() {
        if(PermissionUtil.isPermissionGranted(this, android.Manifest.permission.ACCESS_FINE_LOCATION))
            checkLocationServiceEnable();
        //startLocationService();
        loadDashboardFragment();
    }

    @Override
    public void onPermissionDenied() {
        PermissionUtil.requestPermissions(this,this);
    }



    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                switch (state) {
                    case BluetoothAdapter.STATE_OFF:
                        Log.e("Bluetooth state","off");
                        break;
                    case BluetoothAdapter.STATE_ON:
                        Log.e("Bluetooth state","on");
                        break;
                }
            }
            //======= Battery_Action gets called in 10 sec's interval.
            if(action.equals(Intent.ACTION_BATTERY_CHANGED)){
                countToPush++;
                int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 25);
                int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS,BatteryManager.BATTERY_STATUS_NOT_CHARGING);
                int level1 = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

                float batteryPct = level1 / (float)scale;
                if(batteryPct<0.15 && status==BatteryManager.BATTERY_STATUS_DISCHARGING && countToPush>4) {
                    countToPush=0;
                    dashboardPresenter.notifyBatteryAlert(PreferenceUtil.getInstance(getApplicationContext()).getAccount().getId());
                }else{
                    return;
                }
                Log.e("batteryLevel==", String.valueOf(batteryPct) +" "+countToPush);

            }



            if(action.equals(AudioManager.RINGER_MODE_CHANGED_ACTION)){

                int mode = intent.getIntExtra(AudioManager.EXTRA_RINGER_MODE,0);
                if(oldRingerMode == mode)
                    return;
                oldRingerMode = mode;
                switch (mode){
                    case AudioManager.RINGER_MODE_NORMAL:

                        Log.e("Ring tone sound","Normal");
                        break;
                    case AudioManager.RINGER_MODE_SILENT:

                        Log.e("Ring tone sound","Silent");
                        break;
                    case AudioManager.RINGER_MODE_VIBRATE:

                        Log.e("Ring tone sound","Vibrate");
                        break;
                }
            }
            if(dashboardPresenter!=null)
                dashboardPresenter.updateKidsSettings(createSettingObj());


        }
    };
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CODE_DRAW_OVER_OTHER_APP_PERMISSION) {
            //Check if the permission is granted or not.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
                PreferenceUtil.getInstance(this).savePreference("BobbleHeadOverlay",false);
            }else {
                PreferenceUtil.getInstance(this).savePreference("BobbleHeadOverlay",true);
            }
            PermissionUtil.requestPermissions(this, this);
        }
        else if(requestCode == 1122){
            checkLocationServiceEnable();
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }






    public void showReminderAlert(String title, String note) {


        Dialog dialog = new Dialog(this, android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth);
        dialog.setContentView(R.layout.alarm_alert_dialog);
        TextView tvTitle ;
        AudioPlayBackView audioPlayBackView = dialog.findViewById(R.id.audio_playback);
        ImageView cancel;

        tvTitle = (TextView) dialog.findViewById(R.id.alartTitle);
        cancel = (ImageView) dialog.findViewById(R.id.alertCancel);

        tvTitle.setText(title.toString());
        new Handler().post(() -> audioPlayBackView.setDataSource(Uri.parse(note),true));

        audioPlayBackView.findViewById(R.id.btnCancel).setVisibility(View.GONE);
        dialog.show();

        cancel.setOnClickListener(v -> {
            audioPlayBackView.stopMediaPlayer();
            dialog.dismiss();
        });


    }


    @Override
    public void onTextReceived(String text,String sender) {

    }

    BroadcastReceiver onDownloadComplete=new BroadcastReceiver() {
        public void onReceive(Context ctxt, Intent intent) {
            Bundle extras = intent.getExtras();
            DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            DownloadManager.Query q = new DownloadManager.Query();
            q.setFilterById(extras.getLong(DownloadManager.EXTRA_DOWNLOAD_ID));
            Cursor c = manager.query(q);

            if (c.moveToFirst()) {
                int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
                if (status == DownloadManager.STATUS_SUCCESSFUL) {
                    // process download
                    final String uriString = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                    final String mimeStr = c.getString(c.getColumnIndex(DownloadManager.COLUMN_MEDIA_TYPE));

                    String type = getMimeType(Uri.parse(uriString).getPath());

                    Log.i("Download URI: ",uriString);
                    Log.i("Download MIME: ",type);
                    if(type!=null){
                        if(type.toLowerCase().startsWith("audio")){
                            showAudioNotification(uriString);
                        }

                        if(type.toLowerCase().startsWith("video")){
                            intent = new Intent(getApplicationContext(),VideoPlaybackActivity.class);
                            intent.putExtra("uri",uriString);
                            startActivity(intent);
                        }
                    }

                }
            }


        }
    };

    // url = file path or whatever suitable URL you want.
    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }

    BroadcastReceiver immersiveReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equalsIgnoreCase("immersive")){
                hideNavigationBar();
            }
        }
    };

}
