package com.wiser.kids.ui.home.dialer;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.widget.Button;

import com.wiser.kids.BaseActivity;
import com.wiser.kids.Injection;
import com.wiser.kids.R;
import com.wiser.kids.ui.home.contact.ContactFragment;
import com.wiser.kids.ui.home.contact.ContactPresenter;
import com.wiser.kids.util.PreferenceUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DialerActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.header_btn_right)
    Button btnRight;

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
        btnRight.setText("Call History");
        loadContactFragment();
    }

    private void loadContactFragment() {
        dialerFragment = dialerFragment !=null? dialerFragment : DialerFragment.newInstance();
        dialerPresenter = dialerPresenter !=null? dialerPresenter : new DialerPresenter(dialerFragment, PreferenceUtil.getInstance(this), Injection.provideRepository(this));
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, dialerFragment);
        fragmentTransaction.commit();
    }

    @OnClick(R.id.header_btn_left)
    public void onBackClick(){
        onBackPressed();
    }

    @OnClick(R.id.header_btn_right)
    public void onRightBtnClick(){

    }
}
