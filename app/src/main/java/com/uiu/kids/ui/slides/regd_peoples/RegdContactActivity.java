package com.uiu.kids.ui.slides.regd_peoples;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.uiu.kids.BaseActivity;
import com.uiu.kids.Injection;
import com.uiu.kids.R;
import com.uiu.kids.util.PreferenceUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegdContactActivity extends BaseActivity {
    RegdContactFragment regdContactFragment;
    RegdContactPresenter regdContactPresenter;

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
        toolbar.findViewById(R.id.header_btn_right).setVisibility(View.GONE);
        loadContactFragment();
    }

    private void loadContactFragment() {
        regdContactFragment = regdContactFragment !=null? regdContactFragment : RegdContactFragment.newInstance();
        regdContactPresenter = regdContactPresenter !=null? regdContactPresenter : new RegdContactPresenter(regdContactFragment, PreferenceUtil.getInstance(this),Injection.provideRepository(this));
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, regdContactFragment);
        fragmentTransaction.commit();
    }

    @OnClick(R.id.header_btn_left)
    public void onBackClick(){
        onBackPressed();
    }
}
