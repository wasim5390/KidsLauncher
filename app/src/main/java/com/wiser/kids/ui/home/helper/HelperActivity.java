package com.wiser.kids.ui.home.helper;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;

import com.wiser.kids.BaseActivity;
import com.wiser.kids.Injection;
import com.wiser.kids.R;
import com.wiser.kids.util.PreferenceUtil;

public class HelperActivity extends BaseActivity
{
    public HelperFragment helperFragment;
    public HelperPresenter helperPresenter;



    @Override
    public int getID() {
        return R.layout.activity_helper;
    }

    @Override
    public void created(Bundle savedInstanceState) {
        loadHelperFragment();
    }

    private void loadHelperFragment() {
        helperFragment = helperFragment != null ? helperFragment : helperFragment.newInstance();
        helperPresenter = helperPresenter != null ? helperPresenter : new HelperPresenter(helperFragment, PreferenceUtil.getInstance(this), Injection.provideRepository(this));
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.helerframeLayout, helperFragment);
        fragmentTransaction.commit();
    }
}
