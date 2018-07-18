package com.wiser.kids.ui.home.camera;

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

public class CameraActivity extends BaseActivity {

    private CameraFragment cameraFragment;
    private CameraPresenter cameraPresenter;

    @Override
    public int getID() {
        return R.layout.activity_camera;
    }

    @Override
    public void created(Bundle savedInstanceState) {


        loadCameraFragment();
    }

    private void loadCameraFragment() {
        cameraFragment = cameraFragment !=null? cameraFragment : CameraFragment.newInstance();
        cameraPresenter = cameraPresenter !=null? cameraPresenter : new CameraPresenter(cameraFragment, PreferenceUtil.getInstance(this), Injection.provideRepository(this));
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, cameraFragment);
        fragmentTransaction.commit();


    }

}
