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


import com.wiser.kids.ui.ProgressFragmentDialog;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Callback;

/**
 * Created by sidhu on 4/12/2018.
 */

public abstract class BaseActivity extends AppCompatActivity implements Constant{

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
