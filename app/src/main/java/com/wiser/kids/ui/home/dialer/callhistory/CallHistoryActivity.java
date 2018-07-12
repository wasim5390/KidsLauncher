package com.wiser.kids.ui.home.dialer.callhistory;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.wiser.kids.BaseActivity;
import com.wiser.kids.Injection;
import com.wiser.kids.R;
import com.wiser.kids.ui.home.dialer.DialerFragment;
import com.wiser.kids.ui.home.dialer.DialerPresenter;
import com.wiser.kids.util.PreferenceUtil;

public class CallHistoryActivity extends BaseActivity {

    private CallHistoryFragment callHistoryFragment;
    private CallHistoryPresenter callHistoryPresenter;

    @Override
    public int getID() {
        return R.layout.activity_call_history;}

    @Override
    public void created(Bundle savedInstanceState) {


    loadContactFragment();
    }


    private void loadContactFragment() {
        callHistoryFragment = callHistoryFragment !=null? callHistoryFragment : CallHistoryFragment.newInstance();
        callHistoryPresenter = callHistoryPresenter !=null? callHistoryPresenter : new CallHistoryPresenter(callHistoryFragment, PreferenceUtil.getInstance(this), Injection.provideRepository(this));
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, callHistoryFragment);
        fragmentTransaction.commit();
    }

}
