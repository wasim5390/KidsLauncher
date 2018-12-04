package com.uiu.kids.ui.home.apps;

import android.content.IntentFilter;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.uiu.kids.BaseActivity;
import com.uiu.kids.Injection;
import com.uiu.kids.R;
import com.uiu.kids.util.PreferenceUtil;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AppsActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.header_btn_right)
    Button btnRight;


    private AppsPresenter appsPresenter;
    private AppsFragment appsFragment;


    @Override
    public int getID() {
        return R.layout.activity_apps;
    }

    @Override
    public void created(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        setToolBar(toolbar, "", true);
        btnRight.setVisibility(View.GONE);
        loadAppsFragment();
    }

    private void loadAppsFragment() {
        appsFragment = appsFragment != null ? appsFragment : AppsFragment.newInstance();
        appsPresenter = appsPresenter != null ? appsPresenter : new AppsPresenter(appsFragment, PreferenceUtil.getInstance(this), Injection.provideRepository(this));
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, appsFragment);
        fragmentTransaction.commit();
    }
    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(mGpsSwitchStateReceiver, new IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION));
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(mGpsSwitchStateReceiver);
    }

    @OnClick(R.id.header_btn_left)
    public void onBackClick() {
        onBackPressed();
    }



}
