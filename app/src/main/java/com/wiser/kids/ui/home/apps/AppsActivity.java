package com.wiser.kids.ui.home.apps;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Button;

import com.wiser.kids.BaseActivity;
import com.wiser.kids.Injection;
import com.wiser.kids.R;
import com.wiser.kids.ui.home.dialer.DialerFragment;
import com.wiser.kids.ui.home.dialer.DialerPresenter;
import com.wiser.kids.util.PreferenceUtil;

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
        btnRight.setText("New App");
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

    @OnClick(R.id.header_btn_left)
    public void onBackClick() {
        onBackPressed();
    }

    @OnClick(R.id.header_btn_right)
    public void onRightBtnClick() {
        final Uri marketUri = Uri.parse("https://play.google.com/store/apps?id=com.google.android.instantapps.supervisor");
        startActivity(new Intent(Intent.ACTION_VIEW,marketUri ));
        }


}
