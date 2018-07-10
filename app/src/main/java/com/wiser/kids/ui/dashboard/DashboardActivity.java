package com.wiser.kids.ui.dashboard;


import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;


import com.wiser.kids.BaseActivity;
import com.wiser.kids.Injection;
import com.wiser.kids.R;
import com.wiser.kids.util.PreferenceUtil;

public class DashboardActivity extends BaseActivity {

    DashboardFragment dashboardFragment;
    DashboardPresenter dashboardPresenter;


    @Override
    public int getID() {
        return R.layout.activity_dashboard;
    }

    @Override
    public void created(Bundle savedInstanceState) {

        loadDashboardFragment();
    }

    private void loadDashboardFragment() {
        dashboardFragment = dashboardFragment !=null? dashboardFragment : DashboardFragment.newInstance();
        dashboardPresenter = dashboardPresenter !=null? dashboardPresenter : new DashboardPresenter(dashboardFragment, PreferenceUtil.getInstance(this), Injection.provideRepository(this));
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, dashboardFragment);
        fragmentTransaction.commit();
    }
}
