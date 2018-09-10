package com.uiu.kids.ui.home.camera;

import android.content.Intent;

import com.uiu.kids.source.Repository;
import com.uiu.kids.util.PreferenceUtil;

public class CameraPresenter implements CameraContract.Presenter {

    private CameraContract.View view;
    private Repository repository;
    private PreferenceUtil preferenceUtil;


    public CameraPresenter(CameraContract.View view, PreferenceUtil util, Repository repository) {
        this.view = view;
        this.preferenceUtil = util;
        this.repository = repository;
        this.view.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void cameraLoad() {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
       // startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }
}
