package com.wiser.kids.ui.home.camera;

import android.os.Bundle;
import android.view.View;

import com.wiser.kids.BaseFragment;
import com.wiser.kids.R;


public class CameraFragment extends BaseFragment implements CameraContract.View {

    private CameraContract.Presenter presenter;

    @Override
    public int getID() {
        return R.layout.fragment_camera;
    }

    @Override
    public void initUI(View view) {

    }

    public static CameraFragment newInstance()
    {
        Bundle args =new Bundle();
        CameraFragment instance=new CameraFragment();
        instance.setArguments(args);
        return instance;
    }

    @Override
    public void setPresenter(CameraContract.Presenter presenter) {
        this.presenter=presenter;
    }

    @Override
    public void showNoInternet(){

    }

    @Override
    public void cameraLoaded(){

    }
}
