package com.uiu.kids;


import android.app.AlarmManager;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.uiu.kids.location.BackgroundGeoFenceService;
import com.uiu.kids.model.Invitation;
import com.uiu.kids.ui.ProgressFragmentDialog;
import com.uiu.kids.ui.dashboard.GoogleLoginDialog;
import com.uiu.kids.ui.invitation.InvitationConfirmationCallback;
import com.uiu.kids.ui.slides.reminder.AlarmNotificationService;
import com.uiu.kids.ui.slides.reminder.AlarmSoundService;
import com.uiu.kids.util.Util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;


/**
 * Created by sidhu on 4/12/2018.
 */

public abstract class BaseActivity extends AppCompatActivity implements Constant {
    //Pending intent instance
    public PendingIntent pendingIntent;
    //Alarm Request Code
    public static final int ALARM_REQUEST_CODE = 133;
    boolean calledForLocationSettings=false;

    public abstract int getID();

    public abstract void created(Bundle savedInstanceState);

    private ProgressFragmentDialog pd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getID());
        created(savedInstanceState);
        checkWriteSettingPermission();
    }


    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    public void setToolBar(Toolbar toolbar, CharSequence title, boolean showToolbar) {
        setSupportActionBar(toolbar);
        showToolbar(showToolbar);
        if(showToolbar)
        {
            setToolBarTitle(toolbar,title);
        }
    }

    public void setToolBarTitle(Toolbar toolbar,CharSequence title){
        TextView tvTitle = toolbar.findViewById(R.id.toolbar_title);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(title);
    }
    public void makeFullScreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public void showToolbar(boolean show){
        if(getSupportActionBar()!=null){
            if(show)
                getSupportActionBar().show();
            else
                getSupportActionBar().hide();
        }
    }


    /************************************* App Settings dialog********************/
    /**
     * Showing Alert Dialog with Settings option
     * Navigates user to app settings
     * NOTE: Keep proper title and message depending on your app
     */
    protected void showSettingsDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }


    // navigating user to app settings
    public void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    /**** show progress *******************/

    public void showProgress() {
        try {
            if (pd == null) {
                pd = ProgressFragmentDialog.newInstance();
            }
            if (!pd.getUserVisibleHint())
                pd.show(getSupportFragmentManager(), "TAG");
        }catch (IllegalStateException e){
            Log.e("ProgressBar",e.getMessage());
        }
    }

    /******************* hide progress ***********************/

    public void hideProgress() {
        try {
            if (pd != null) {
                pd.dismiss();
            }
        }catch (IllegalStateException e){
            Log.e("ProgressBarDismiss",e.getMessage());
        }
    }

    public void loginWithGoogle(GoogleLoginDialog mCallback) {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_login_google, null);
        b.setView(dialogView);
        b.setCancelable(false);
        final AlertDialog dialog = b.create();

        dialogView.findViewById(R.id.login_with_google_btn).setOnClickListener(view -> {
            mCallback.onGoogleLoginClicked();
            dialog.dismiss();
        });
        dialog.show();
    }



    protected void updateBottomNavigationViewIconSize(BottomNavigationView bottomNavigationView){
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) bottomNavigationView.getChildAt(0);
        for (int i = 0; i < menuView.getChildCount(); i++) {
            final View iconView = menuView.getChildAt(i).findViewById(android.support.design.R.id.icon);
            final ViewGroup.LayoutParams layoutParams = iconView.getLayoutParams();
            final DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            layoutParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 32, displayMetrics);
            layoutParams.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 32, displayMetrics);
            iconView.setLayoutParams(layoutParams);
        }
    }

    /**
     * Show the user a dialog where he can resend an invitation, or remove the contact from the list
     */
    public void showInvitationActionsDialog(Context context,String title,String msg, Invitation invitation, InvitationConfirmationCallback callback){
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_invitation_action, null);
        Button accept = dialogView.findViewById(R.id.confirmation_yes);
        Button reject = dialogView.findViewById(R.id.confirmation_no);
        TextView tvTitle = dialogView.findViewById(R.id.tv_confirmation_title);
        TextView tvMsg = dialogView.findViewById(R.id.tv_confirmation_msg);
        tvTitle.setText(title);
        tvMsg.setText(msg);
        if(invitation.getStatus()==INVITE.INVITED)
        {
            accept.setText(getString(R.string.accept));
            reject.setText(getString(R.string.reject));
        }
        final Dialog dialog = Util.showHeaderDialog(this, dialogView);

        accept.setOnClickListener(v -> {
            dialog.dismiss();
            callback.onAcceptInvitation(invitation);
        });

        reject.setOnClickListener(v -> {
            dialog.dismiss();
            callback.onRejectInvitation(invitation);

        });

    }

    public boolean checkWriteSettingPermission(){
        // Check whether has the write settings permission or not.
        boolean settingsCanWrite = false;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            settingsCanWrite = Settings.System.canWrite(getApplicationContext());
        }

        if(!settingsCanWrite) {
            // If do not have write settings permission then open the Can modify system settings panel.
            Intent intent = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:com.uiu.kids"));
                startActivity(intent);
            }

            return false;
        }
        return true;
    }



    //Trigger alarm manager with entered time interval
    public void triggerAlarmManager(int alarmTriggerTime) {
        // get a Calendar object with current time
        Calendar cal = Calendar.getInstance();
        // add alarmTriggerTime seconds to the calendar object
        cal.add(Calendar.SECOND, alarmTriggerTime);

        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);//get instance of alarm manager
        manager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);//set alarm manager with entered timer by converting into milliseconds

        Toast.makeText(this, "Alarm Set for " + alarmTriggerTime + " seconds.", Toast.LENGTH_SHORT).show();
    }

    //Stop/Cancel alarm manager
    public void stopAlarmManager() {

        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        manager.cancel(pendingIntent);//cancel the alarm manager of the pending intent


        //Stop the Media Player Service to stop sound
        stopService(new Intent(this, AlarmSoundService.class));

        //remove the notification from notification tray
        NotificationManager notificationManager = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(AlarmNotificationService.NOTIFICATION_ID);

        Toast.makeText(this, "Alarm Canceled/Stop by User.", Toast.LENGTH_SHORT).show();
    }


    /**
     * Following broadcast receiver is to listen the Location button toggle state in Android.
     */
    protected BroadcastReceiver mGpsSwitchStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (LocationManager.PROVIDERS_CHANGED_ACTION.equals(intent.getAction())) {
                boolean gps,network;
                // Make an action or refresh an already managed state.
                LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                network=locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                if(!gps && !calledForLocationSettings) {
                    calledForLocationSettings=true;
                    BackgroundGeoFenceService.getInstance().createLocationRequest();
                }
                if(gps){
                    calledForLocationSettings=false;
                }

                //  Toast.makeText(context, "GPS: "+String.valueOf(gps)+"---"+" Network: "+String.valueOf(network), Toast.LENGTH_SHORT).show();

            }
        }
    };

    public Bitmap getBitmapFromAsset(Context context, String strName) {
        AssetManager assetManager = context.getAssets();
        InputStream istr = null;
        try {
            istr = assetManager.open(strName);
            return BitmapFactory.decodeStream(istr);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void applyBg(String imageName){
        Bitmap bitmap = getBitmapFromAsset(this,"bg_images/"+imageName);
        BitmapDrawable ob = new BitmapDrawable(getResources(), bitmap);
        getWindow().setBackgroundDrawable(ob);
    }

}
