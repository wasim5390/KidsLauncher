package com.uiu.kids.ui.message;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.widget.ImageView;

import com.otaliastudios.cameraview.AspectRatio;
import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.CameraUtils;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.Facing;
import com.otaliastudios.cameraview.Flash;
import com.otaliastudios.cameraview.SessionType;
import com.otaliastudios.cameraview.Size;
import com.otaliastudios.cameraview.SizeSelector;
import com.otaliastudios.cameraview.SizeSelectors;
import com.uiu.kids.BaseActivity;
import com.uiu.kids.Constant;
import com.uiu.kids.Injection;
import com.uiu.kids.R;
import com.uiu.kids.ui.camera.CustomCameraActivity;
import com.uiu.kids.ui.camera.editor.PhotoEditorActivity;
import com.uiu.kids.util.PreferenceUtil;
import com.uiu.kids.util.Util;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MessageActivity extends BaseActivity {

    public MessageFragment messageFragment;
    public MessagePresenter messagePresenter;

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
        messageFragment = messageFragment != null ? messageFragment : MessageFragment.newInstance();
        messagePresenter = messagePresenter != null ? messagePresenter : new MessagePresenter(messageFragment,
                PreferenceUtil.getInstance(this), Injection.provideRepository(this));
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.chatframeLayout, messageFragment,"messageFragment");
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
