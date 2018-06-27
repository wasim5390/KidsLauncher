package com.wiser.kids.ui.home;


import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;


import com.wiser.kids.BaseActivity;
import com.wiser.kids.Injection;
import com.wiser.kids.R;
import com.wiser.kids.ui.home.HomeFragment;
import com.wiser.kids.ui.home.HomePresenter;
import com.wiser.kids.util.PreferenceUtil;

public class HomeActivity extends BaseActivity {

    HomeFragment homeFragment;
    HomePresenter homePresenter;


    @Override
    public int getID() {
        return R.layout.activity_home;
    }

    @Override
    public void created(Bundle savedInstanceState) {

        getSupportActionBar().hide();
        loadHomeFragment();
    }

    private void loadHomeFragment() {
        homeFragment = homeFragment!=null?homeFragment: HomeFragment.newInstance();
        homePresenter = homePresenter!=null?homePresenter: new HomePresenter(homeFragment, PreferenceUtil.getInstance(this), Injection.provideRepository(this));
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,homeFragment);
        fragmentTransaction.commit();
    }
}
