package com.uiu.kids;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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

import com.uiu.kids.model.Invitation;
import com.uiu.kids.ui.ProgressFragmentDialog;
import com.uiu.kids.ui.dashboard.GoogleLoginDialog;
import com.uiu.kids.ui.invitation.InvitationConfirmationCallback;
import com.uiu.kids.util.Util;


/**
 * Created by sidhu on 4/12/2018.
 */

public abstract class BaseActivity extends AppCompatActivity implements Constant {
    public static String primaryParentId;

    public abstract int getID();

    public abstract void created(Bundle savedInstanceState);

    private ProgressFragmentDialog pd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getID());
        created(savedInstanceState);
    }


    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();

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
            if (!pd.isAdded())
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
}
