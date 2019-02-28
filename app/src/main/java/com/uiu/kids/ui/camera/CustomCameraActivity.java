package com.uiu.kids.ui.camera;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.otaliastudios.cameraview.AspectRatio;
import com.otaliastudios.cameraview.Audio;
import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.CameraUtils;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.Facing;
import com.otaliastudios.cameraview.Flash;
import com.otaliastudios.cameraview.Gesture;
import com.otaliastudios.cameraview.GestureAction;
import com.otaliastudios.cameraview.SessionType;
import com.otaliastudios.cameraview.SizeSelector;
import com.otaliastudios.cameraview.SizeSelectors;
import com.otaliastudios.cameraview.VideoQuality;
import com.uiu.kids.BaseActivity;
import com.uiu.kids.Constant;
import com.uiu.kids.R;
import com.uiu.kids.ui.Chronometer;
import com.uiu.kids.ui.camera.editor.PhotoEditorActivity;
import com.uiu.kids.ui.share.ShareActivity;
import com.uiu.kids.util.Util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CustomCameraActivity extends BaseActivity {


    private static final String TAG = CustomCameraActivity.class.getSimpleName();


    @BindView(R.id.btnCamera)
    ImageView btnCamera;

    @BindView(R.id.btnVideoCamera)
    ImageButton btnVideoCamera;

    @BindView(R.id.camera_mode_switch)
    ImageView btnCameraSwitch;

    @BindView(R.id.btnFlash)
    ImageView btnFlash;

    @BindView(R.id.btnFacing)
    ImageView btnFacing;

    @BindView(R.id.camera)
    CameraView camera;

    @BindView(R.id.chronometer)
    Chronometer chronometer;

    boolean VIDEO_CAMERA=true;
    boolean CAMERA_FOR_RESULT=false;
    boolean flashMode=false;
    File videoFile ;
    CameraMode cameraMode;

    @Override
    public int getID() {
        return R.layout.activity_custom_camera;
    }

    @Override
    public void created(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        VIDEO_CAMERA = getIntent().getBooleanExtra(key_camera_type,false);
        CAMERA_FOR_RESULT = getIntent().getBooleanExtra(key_camera_for_result,false);
        cameraMode=VIDEO_CAMERA?CameraMode.RECORD:CameraMode.CAPTURE;
        try {
            videoFile = Util.createVideoFile(this);
        } catch (IOException e) {
            e.printStackTrace();
        }

        camera.setLifecycleOwner(this);
        camera.setVideoQuality(VideoQuality.MAX_720P);
        camera.setPlaySounds(true);
        camera.setAudio(Audio.ON);
       // camera.setVideoMaxDuration(10*600);
        camera.setVideoMaxSize(1000*1000*100); // 100 MB
        camera.mapGesture(Gesture.PINCH, GestureAction.ZOOM); // Pinch to zoom!
        camera.mapGesture(Gesture.TAP, GestureAction.FOCUS_WITH_MARKER); // Tap to focus!
        changeCameraMode(cameraMode);


        camera.addCameraListener(new CameraListener() {
            @Override
            public void onPictureTaken(byte[] picture) {
                // Create a bitmap or a file...
                // CameraUtils will read EXIF orientation for you, in a worker thread.
                CameraUtils.decodeBitmap(picture, bitmap -> {
                    File imageFile = createImageFile();
                    writeBitmapOnFile(imageFile,bitmap);
                    if(CAMERA_FOR_RESULT){
                        Util.saveOnGallery(CustomCameraActivity.this,imageFile);
                        Intent intent = getIntent();
                        intent.putExtra(Constant.IntentExtras.IMAGE_PATH,imageFile.getAbsolutePath());
                        setResult(RESULT_OK,intent);
                        finish();
                    }else {
                        Intent intent = new Intent(CustomCameraActivity.this, PhotoEditorActivity.class);
                        intent.putExtra(Constant.CAMERA_IMG_PATH, imageFile.getAbsolutePath());
                        startActivityForResult(intent, 0);
                    }
                    camera.stop();
                });
            }

            @Override
            public void onVideoTaken(File video) {
                super.onVideoTaken(video);
                btnVideoCamera.setImageResource(R.drawable.ic_video_recrod);
                camera.setFlash(Flash.OFF);
                chronometer.stop();
                chronometer.setVisibility(View.GONE);

                try {
                    FileOutputStream fileOutputStream = new FileOutputStream(videoFile);
                    InputStream inputStream = getContentResolver().openInputStream(Uri.fromFile(video));
                    Util.copyStream(inputStream, fileOutputStream);
                    fileOutputStream.close();
                    inputStream.close();

                    Intent intent = new Intent();
                    intent.setClass(CustomCameraActivity.this, ShareActivity.class);
                    intent.putExtra(Constant.RECORDED_FILE_PATH,videoFile.getPath());
                    intent.putExtra(Constant.RECORDED_FILE_TYPE,MEDIA_VIDEO);
                    startActivityForResult(intent,0);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        });

        camera.setFlash(Flash.AUTO);
        if(CAMERA_FOR_RESULT){
            btnCameraSwitch.setVisibility(View.GONE);
        }


    }

    public void changeCameraMode(CameraMode cameraMode){
        btnVideoCamera.setVisibility(cameraMode==CameraMode.RECORD? View.VISIBLE:View.GONE);
        btnCamera.setVisibility(cameraMode!=CameraMode.RECORD? View.VISIBLE:View.GONE);
        btnCameraSwitch.setImageResource(cameraMode==CameraMode.RECORD?R.drawable.ic_camera_e:R.drawable.ic_videocam);
    }

    public File writeBitmapOnFile(File imageFile,Bitmap bitmap){
        new Handler().post(() -> {
            try {
                OutputStream fOut = new FileOutputStream(imageFile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fOut); // saving the Bitmap to a file compressed as a JPEG with 85% compression rate
                fOut.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return imageFile;
    }

    public File createImageFile(){
        File photoFile = null;
        try {
            photoFile = Util.createImageFile(this);
        } catch (IOException ex) {
            // Error occurred while creating the File

        }
        // Continue only if the File was successfully created
        if (photoFile != null) {
            // Save a file: path for use with ACTION_VIEW intents
            //  mCurrentPhotoPath = photoFile.getAbsolutePath();
            //   Uri photoURI = FileProvider.getUriForFile(this,
            //           "com.uiu.kids.fileprovider",
            //           photoFile);

        }

        return photoFile;
    }

    public void changeFlashMode(){
        Flash flash =camera.getFlash();
        switch (flash){
            case TORCH:
            case ON:
                camera.setFlash(Flash.OFF);
                btnFlash.setImageResource(R.drawable.ic_flash_off);
                flashMode=false;
                break;
            case OFF:
                camera.setFlash(Flash.AUTO);
                flashMode=false;
                btnFlash.setImageResource(R.drawable.ic_flash_auto);
                break;
            case AUTO:
                if(cameraMode==CameraMode.RECORD) {
                    camera.setFlash(Flash.TORCH);
                    flashMode=true;
                }
                else
                    camera.setFlash(Flash.ON);

                btnFlash.setImageResource(R.drawable.ic_flash_on);
                break;
        }

    }



    @OnClick(R.id.btnCamera)
    public void onCaptureClick(){

        camera.setSessionType(SessionType.PICTURE);
        SizeSelector width = SizeSelectors.maxWidth(1440);
        SizeSelector height = SizeSelectors.maxHeight(2560);
        SizeSelector dimensions = SizeSelectors.and(width, height); // Matches sizes bigger than 1000x2000.
        SizeSelector ratio = SizeSelectors.aspectRatio(AspectRatio.of(1, 1), 0); // Matches 1:1 sizes.

        SizeSelector result = SizeSelectors.or(
                SizeSelectors.and(dimensions), // Try to match both constraints
                ratio, // If none is found, at least try to match the aspect ratio
                SizeSelectors.biggest() // If none is found, take the biggest
        );
        camera.setPictureSize(result);
        camera.capturePicture();
    }

    @OnClick(R.id.btnVideoCamera)
    public void onCaptureVideoClick(){
        if(camera.isCapturingVideo()) {
            btnVideoCamera.setImageResource(R.drawable.ic_video_recrod);
            camera.stopCapturingVideo();
            return;
        }

        btnVideoCamera.setImageResource(R.drawable.ic_video_recrod_stop);
        camera.setSessionType(SessionType.VIDEO);
        camera.startCapturingVideo(null);
        if(flashMode)
            camera.setFlash(Flash.TORCH);
        chronometer.setVisibility(View.VISIBLE);
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
    }

    @OnClick(R.id.btnFlash)
    public void onFlashChange()
    {
        if(camera.getFacing()==Facing.BACK)
            changeFlashMode();

    }

    @OnClick(R.id.btnFacing)
    public void onFacingChange()
    {
        camera.toggleFacing();
        switch (camera.getFacing()){
            case FRONT:
                btnFlash.setVisibility(View.GONE);
                break;
            case BACK:
                btnFlash.setVisibility(View.VISIBLE);
                break;
        }
    }

    @OnClick(R.id.camera_mode_switch)
    public void onCameraModeChangeClick(){
        cameraMode=cameraMode==CameraMode.CAPTURE?CameraMode.RECORD:CameraMode.CAPTURE;
        changeCameraMode(cameraMode);
    }

    @OnClick(R.id.btnBack)
    public void onBackClick(){
        setResult(RESULT_CANCELED);
        onBackPressed();
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
        camera.stop();
    }



    @Override
    protected void onResume() {
        super.onResume();
        camera.start();
    }

    @Override
    protected void onPause() {
        camera.stop();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        camera.destroy();
        super.onDestroy();

    }

    public enum  CameraMode{
        CAPTURE,
        RECORD;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        {
            if(resultCode==RESULT_OK){
                setResult(RESULT_OK);
                finish();
            }
        }
    }
}
