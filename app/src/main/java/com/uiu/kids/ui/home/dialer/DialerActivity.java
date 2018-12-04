package com.uiu.kids.ui.home.dialer;

import android.content.Intent;
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
import com.uiu.kids.ui.home.dialer.callhistory.CallHistoryFragment;
import com.uiu.kids.ui.home.dialer.callhistory.CallHistoryPresenter;
import com.uiu.kids.util.PreferenceUtil;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DialerActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private boolean isHistoryLoaded=false;
    private CallHistoryFragment callHistoryFragment;
    private CallHistoryPresenter callHistoryPresenter;

    private DialerFragment dialerFragment;
    private DialerPresenter dialerPresenter;

    @Override
    public int getID() {
        return R.layout.activity_dialer;
    }

    @Override
    public void created(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        setToolBar(toolbar,"",true);
        loadContactFragment();
    }

    private void loadContactFragment() {
        isHistoryLoaded=false;
        dialerFragment = dialerFragment !=null? dialerFragment : DialerFragment.newInstance();
        dialerPresenter = dialerPresenter !=null? dialerPresenter : new DialerPresenter(dialerFragment, PreferenceUtil.getInstance(this), Injection.provideRepository(this));
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, dialerFragment);
        fragmentTransaction.commit();
    }

    private void loadContactHistoryFragment() {
        isHistoryLoaded=true;
        callHistoryFragment = callHistoryFragment !=null? callHistoryFragment : CallHistoryFragment.newInstance();
        callHistoryPresenter = callHistoryPresenter !=null? callHistoryPresenter : new CallHistoryPresenter(callHistoryFragment, PreferenceUtil.getInstance(this), Injection.provideRepository(this));
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, callHistoryFragment);
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
    public void onBackClick(){
        onBackPressed();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1)
        {

        }


    }
}
