package com.wiser.kids.ui.home.contact;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;

import com.wiser.kids.BaseActivity;
import com.wiser.kids.Injection;
import com.wiser.kids.R;
import com.wiser.kids.util.PreferenceUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ContactActivity extends BaseActivity {
    ContactFragment contactFragment;
    ContactPresenter contactPresenter;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    public int getID() {
        return R.layout.activity_contact;
    }

    @Override
    public void created(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        setToolBar(toolbar,"",true);
        loadContactFragment();
    }

    private void loadContactFragment() {
        contactFragment = contactFragment !=null? contactFragment : ContactFragment.newInstance();
        contactPresenter = contactPresenter !=null? contactPresenter : new ContactPresenter(contactFragment, PreferenceUtil.getInstance(this), Injection.provideRepository(this));
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, contactFragment);
        fragmentTransaction.commit();
    }

    @OnClick(R.id.header_btn_left)
    public void onBackClick(){
        onBackPressed();
    }
}
