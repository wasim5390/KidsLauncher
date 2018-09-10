package com.uiu.kids.ui.home.camera;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.uiu.kids.BaseActivity;
import com.uiu.kids.Injection;
import com.uiu.kids.R;
import com.uiu.kids.util.PreferenceUtil;


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
