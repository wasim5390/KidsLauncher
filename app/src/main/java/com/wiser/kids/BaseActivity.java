package com.wiser.kids;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.clicksend.directmail.model.Job;
import com.clicksend.directmail.model.Status;
import com.clicksend.directmail.ui.BottomSheetFragment;
import com.clicksend.directmail.ui.ProgressFragmentDialog;
import com.clicksend.directmail.ui.appmode.BottomSheetChangeModeFragment;
import com.clicksend.directmail.ui.appmode.ModeSelectionCallback;
import com.clicksend.directmail.ui.dashboard.job_tracking.JobStatusDialogCallback;
import com.clicksend.directmail.ui.dashboard.job_tracking.JobStatusDialogConfirmationCallback;
import com.crowdfire.cfalertdialog.CFAlertDialog;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sidhu on 4/12/2018.
 */

public abstract class BaseActivity extends AppCompatActivity implements Constant{

    public abstract int getID();

    public abstract void created(Bundle savedInstanceState);

    private ProgressFragmentDialog pd;


    Map<Integer,Status> statusDictionary;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getID());
        created(savedInstanceState);
        statusDictionary = new HashMap<>();
        populateDictionary();
    }

    public Map getStatusDictionary(){
        if(statusDictionary==null){
            statusDictionary = new HashMap<>();
            populateDictionary();
        }
        return statusDictionary;
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @SuppressLint("ResourceType")
    private void populateDictionary(){
        statusDictionary.clear();

        statusDictionary.put(UNASSIGNED,new Status(UNASSIGNED,getString(R.string.unassigned),getString(R.color.unassigned)));
        statusDictionary.put(ASSIGNED,new Status(ASSIGNED,getString(R.string.assigned),getString(R.color.assigned)));
        statusDictionary.put(STARTED, new Status(STARTED,getString(R.string.started),getString(R.color.start)));
        statusDictionary.put(PAUSED, new Status(PAUSED, getString(R.string.paused), getString(R.color.paused)));
        statusDictionary.put(STOPPED,new Status(STOPPED,getString(R.string.stopped),getString(R.color.stop)));
        statusDictionary.put(COMPLETED,new Status(COMPLETED,getString(R.string.completed),getString(R.color.complete)));
        statusDictionary.put(CANCELLED,new Status(CANCELLED,getString(R.string.cancelled),getString(R.color.cancel)));
    }



    public void setToolBar(Toolbar toolbar,CharSequence title, boolean showToolbar) {
        setSupportActionBar(toolbar);
        showToolbar(showToolbar);
        if(showToolbar)
        {
            setToolBarTitle(toolbar,title);
        }
    }

    public void setToolBarTitle(Toolbar toolbar,CharSequence title){
        TextView tvTitle = toolbar.findViewById(R.id.toolbar_title);
        tvTitle.setText(title);
    }


    public void showToolbar(boolean show){
        if(getSupportActionBar()!=null){
            if(show)
                getSupportActionBar().show();
            else
                getSupportActionBar().hide();
        }
    }



    /**
     * Showing Alert Dialog with Settings option
     * Navigates user to app settings
     */
    public void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.location_dialog_title);
        builder.setMessage(R.string.location_dialog_message);
        builder.setPositiveButton("GOTO SETTINGS", (dialog, which) -> {
            dialog.cancel();
            openSettings();
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
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

        if (pd==null) {
            pd = ProgressFragmentDialog.newInstance();
        }
        pd.show(getSupportFragmentManager(), "TAG");

    }

    /******************* hide progress ***********************/

    public void hideProgress() {
        if (pd != null) {
            pd.dismiss();
        }

    }


    public void showAlertDialog(String message,boolean alert){
        String title = alert ? alertTitle : "" ;
        // Create Alert using Builder
        CFAlertDialog.Builder builder = new CFAlertDialog.Builder(this)
                .setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT).setCornerRadius(50)
                .setTitle(title)
                .setMessage(message+"\n")
                .setCancelable(true)
                .setAutoDismissAfter(4000)
                .setTextGravity(Gravity.CENTER)
                .addButton("   OK   ", -1,
                        ContextCompat.getColor(this,R.color.blue),
                        CFAlertDialog.CFAlertActionStyle.POSITIVE,
                        CFAlertDialog.CFAlertActionAlignment.CENTER,
                        (dialog, which) -> {
                            dialog.dismiss();
                        });

        builder.show();
    }

    public void showAlertDialogWithConfirmation(String title,String message,boolean isComplete,final JobStatusDialogConfirmationCallback confirmationCallback){
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setMessage(message)
                .setTitle(title)
                .setCancelable(true);

        builder.setPositiveButton(
                "Yes", (dialog, id) -> {
                    if(isComplete) {
                        confirmationCallback.onCompleteConfirmed();
                    }
                    else {
                        confirmationCallback.onCanceledConfirmed();
                    }
                    dialog.cancel();
                });

        builder.setNegativeButton(
                "No", (dialog, id) -> dialog.cancel());

        builder.create().show();

    }

    public void showAlertSheet(Job job, JobStatusDialogCallback mCallback){
        BottomSheetFragment bottomSheetFragment = BottomSheetFragment.newInstance(job);
        bottomSheetFragment.setCallback(mCallback);
        bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
    }

    public void showModeChangeSheet(ModeSelectionCallback mCallback){
        BottomSheetChangeModeFragment bottomSheetFragment = BottomSheetChangeModeFragment.newInstance();
        bottomSheetFragment.setCallback(mCallback);
        bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
        bottomSheetFragment.setCancelable(false);
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
}
