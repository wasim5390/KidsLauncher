package com.uiu.kids.ui.share;

import android.content.IntentFilter;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.uiu.kids.BaseActivity;
import com.uiu.kids.Constant;
import com.uiu.kids.Injection;
import com.uiu.kids.R;
import com.uiu.kids.util.PreferenceUtil;


import butterknife.ButterKnife;

public class ShareActivity extends BaseActivity {

    public ShareFragment shareFragment;
    public SharePresenter sharePresenter;

    int fileType =4;
    String filePath=null;

    @Override
    public int getID() {
        return R.layout.activity_message2;
    }

    @Override
    public void created(Bundle savedInstanceState) {

        ButterKnife.bind(this);

        fileType = getIntent().getIntExtra(Constant.RECORDED_FILE_TYPE,MEDIA_FILE);
        filePath = getIntent().getStringExtra(Constant.RECORDED_FILE_PATH);
        loadMessageFragment();

    }



    private void loadMessageFragment() {
        shareFragment = shareFragment != null ? shareFragment : ShareFragment.newInstance(filePath,fileType);
        sharePresenter = sharePresenter != null ? sharePresenter : new SharePresenter(shareFragment,
                PreferenceUtil.getInstance(this), Injection.provideRepository(this));
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.chatframeLayout, shareFragment,"shareFragment");
        fragmentTransaction.commit();
    }


    @Override
    protected void onStart() {

        registerReceiver(mGpsSwitchStateReceiver, new IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION));
        super.onStart();
    }

    @Override
    protected void onStop() {

        unregisterReceiver(mGpsSwitchStateReceiver);
        super.onStop();

    }


}
