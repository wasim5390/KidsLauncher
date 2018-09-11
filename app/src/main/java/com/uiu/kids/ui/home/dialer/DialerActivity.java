package com.uiu.kids.ui.home.dialer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
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

    @BindView(R.id.header_btn_right)
    Button btnRight;

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
        setToolBarRBtn();
        loadContactFragment();
    }

    private void loadContactFragment() {
        isHistoryLoaded=false;
        setToolBarRBtn();
        dialerFragment = dialerFragment !=null? dialerFragment : DialerFragment.newInstance();
        dialerPresenter = dialerPresenter !=null? dialerPresenter : new DialerPresenter(dialerFragment, PreferenceUtil.getInstance(this), Injection.provideRepository(this));
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, dialerFragment);
        fragmentTransaction.commit();
    }

    private void loadContactHistoryFragment() {
        isHistoryLoaded=true;
        setToolBarRBtn();
        callHistoryFragment = callHistoryFragment !=null? callHistoryFragment : CallHistoryFragment.newInstance();
        callHistoryPresenter = callHistoryPresenter !=null? callHistoryPresenter : new CallHistoryPresenter(callHistoryFragment, PreferenceUtil.getInstance(this), Injection.provideRepository(this));
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, callHistoryFragment);
        fragmentTransaction.commit();
    }

    private void setToolBarRBtn()
    {
        if(isHistoryLoaded) {
            btnRight.setText("Dialer");
        }
        else
        {
            btnRight.setText("Call History");

        }
    }

    @OnClick(R.id.header_btn_left)
    public void onBackClick(){
        onBackPressed();
    }

    @OnClick(R.id.header_btn_right)
    public void onRightBtnClick(){

        if(isHistoryLoaded)
        {
          loadContactFragment();
        }
        else
        {
            loadContactHistoryFragment();
        }




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1)
        {

        }


    }
}
