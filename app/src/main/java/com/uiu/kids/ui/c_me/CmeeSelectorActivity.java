package com.uiu.kids.ui.c_me;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageButton;

import com.uiu.kids.BaseActivity;
import com.uiu.kids.R;
import com.uiu.kids.ui.camera.CustomCameraActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CmeeSelectorActivity extends BaseActivity {

    private static final int REQ_CAMERA = 0x005;
    private static final int REQ_AUDIO = 0x006;
    @BindView(R.id.btnAudio)
    ImageButton btnAudio;

    @BindView(R.id.btnVideo)
    ImageButton btnVideo;

    @Override
    public int getID() {
        return R.layout.activity_c_me_selector;
    }

    @Override
    public void created(Bundle savedInstanceState) {
        ButterKnife.bind(this);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        setResult(resultCode);
        finish();

    }

    @OnClick(R.id.btnVideo)
    public void onCameraClick(){
        gotoCamera(true);

    }

    @OnClick(R.id.btnAudio)
    public void onAudioClick(){
        gotoAudio();

    }

    @OnClick(R.id.btnBack)
    public void onBackClick(){
        finish();
    }


    private void gotoCamera(boolean cameraType) {
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(this,CustomCameraActivity.class);
            intent.putExtra(key_camera_type,cameraType);
            startActivityForResult(intent, REQ_CAMERA);
        }, 1);

    }


    private void gotoAudio() {
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(this,CmeeAudioActivity.class);
            startActivityForResult(intent, REQ_AUDIO);
        }, 1);

    }
}
