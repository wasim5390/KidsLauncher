package com.wiser.kids.ui.home.camera;

import android.content.Intent;

import com.wiser.kids.source.Repository;
import com.wiser.kids.ui.home.dialer.DialerContract;
import com.wiser.kids.util.PermissionUtil;
import com.wiser.kids.util.PreferenceUtil;

import static android.support.v4.app.ActivityCompat.startActivityForResult;

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
