package com.uiu.kids.ui.home;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.squareup.picasso.Picasso;
import com.uiu.kids.BaseFragment;
import com.uiu.kids.Constant;
import com.uiu.kids.R;
import com.uiu.kids.event.LoginEvent;
import com.uiu.kids.model.User;
import com.uiu.kids.source.Repository;
import com.uiu.kids.ui.SosManager;
import com.uiu.kids.ui.c_me.CmeeSelectorActivity;
import com.uiu.kids.ui.camera.CustomCameraActivity;
import com.uiu.kids.ui.home.apps.AppsActivity;
import com.uiu.kids.ui.home.contact.ContactActivity;
import com.uiu.kids.ui.home.contact.ContactEntity;
import com.uiu.kids.ui.home.dialer.DialerActivity;
import com.uiu.kids.ui.home.gallery.BackgroundGalleryActivity;
import com.uiu.kids.util.PermissionUtil;
import com.uiu.kids.util.PreferenceUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

public class HomeFragment extends BaseFragment implements HomeContract.View, Constant, HomeSlideAdapter.Callback,
        PicModeSelectDialog.IPicModeSelectListener,
        PermissionUtil.PermissionCallback {

    private static final int REQ_CONTACT = 0x003;
    private static final int REQ_DIALER = 0x004;
    private static final int REQ_APPS = 0x005;
    private static final int REQ_CAMERA = 0x006;
    private static final int REQ_MESSAGING=0x007;
    public static String TAG = "HomeFragment";
    private String profileImagePath=null;

    int counter = 1;

    @BindView(R.id.btnDialHome)
    ImageButton btnDialHome;


    @BindView(R.id.btnSosHome)
    ImageButton btnSosHome;


    @BindView(R.id.btnCameraHome)
    ImageButton btnCameraHome;


    @BindView(R.id.btnCmeHome)
    ImageButton btnCmeHome;

    @BindView(R.id.single_contact_avatar)
    ImageView mProfileImg;

    @BindView(R.id.tvCounter)
    TextView tvCounter;

    long then = 0;
    boolean animCancel=false;
    // private HomeSlideAdapter adapterHomeSlide;
    private HomeContract.Presenter presenter;

    SosManager manager;

    Animation animation;


    @Override
    public int getID() {
        return R.layout.home_layout;
    }

    @Override
    public void initUI(View view) {
        EventBus.getDefault().register(this);
        loadProfileImage();
        if(presenter==null)
            presenter = new HomePresenter(this,PreferenceUtil.getInstance(getActivity()), Repository.getInstance());
        presenter.start();
        setColorOnBtn(Color.parseColor("#edeecb"),getView().findViewById(R.id.btnProfile));
        setColorOnBtn(Color.parseColor(PreferenceUtil.getInstance(getContext()).getColorPreference(HOME_DIAL_BG,"#c3d400")),btnDialHome);
        setColorOnBtn(Color.parseColor(PreferenceUtil.getInstance(getContext()).getColorPreference(HOME_CAMERA_BG,"#0fabec")),btnCameraHome);
        setColorOnBtn(Color.parseColor(PreferenceUtil.getInstance(getContext()).getColorPreference(HOME_CME_BG,"#fc5400")),btnCmeHome);
        setColorOnBtn(Color.parseColor(PreferenceUtil.getInstance(getContext()).getColorPreference(HOME_SOS_BG,"#ffe100")),btnSosHome);

        animation = new ScaleAnimation(0,1,0,1,Animation.RELATIVE_TO_SELF,0.65f,Animation.RELATIVE_TO_SELF,0.65f);
        animation.setFillAfter(false);
        animation.setInterpolator(new OvershootInterpolator());

        onSOSTouch();
    }

    public void onSOSTouch(){
        btnSosHome.setOnTouchListener((v, event) -> {
            List<ContactEntity> allSos = PreferenceUtil.getInstance(getContext()).getAllSosList();
            if(event.getAction() == MotionEvent.ACTION_DOWN){

                then = (Long) System.currentTimeMillis();
                allSos = allSos==null?new ArrayList<>():allSos;
                if(allSos.isEmpty()) {
                    showCenterToast("You don't have any SOS");
                }else {
                    manager = new SosManager(allSos, getContext(), () -> {
                    });
                    animateSosCounter();
                }
            }
            else if(event.getAction() == MotionEvent.ACTION_UP){
                if(allSos.isEmpty()) {
                    showCenterToast("You don't have any SOS");
                }else{
                    if(((Long) System.currentTimeMillis() - then) < 3000){
                        animCancel=true;
                        tvCounter.getAnimation().cancel();
                        showCenterToast("Press and hold for 3 seconds");
                    }}
            }
            else if(event.getAction() == MotionEvent.ACTION_CANCEL){
                if(tvCounter.getAnimation()!=null) {
                    animCancel=true;
                    tvCounter.getAnimation().cancel();
                }
            }

            return false;
        });
    }




    public static HomeFragment newInstance() {
        Bundle args = new Bundle();
        HomeFragment homeFragment = new HomeFragment();
        homeFragment.setArguments(args);
        return homeFragment;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Unregister");
        EventBus.getDefault().unregister(this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(LoginEvent account) {
        User user = account.getAccount();
        try {
            Picasso.get().load(user.getImageLink()).fit().placeholder(R.mipmap.avatar_male2).error(R.mipmap.avatar_male2).into(mProfileImg);
        } catch (Exception e) {
            Picasso.get().load(R.mipmap.avatar_male2).fit().into(mProfileImg);
        }
    }


    @Override
    public void showMessage(String message) {
        Toast.makeText(mBaseActivity, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void slideItemsLoaded(List<String> list) {

    }

    @Override
    public void setPresenter(HomeContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showNoInternet() {

    }

    @OnClick({R.id.btnCmeHome,R.id.btnCameraHome,R.id.btnDialHome})
    public void onItemClick(View view) {
        switch (view.getId()) {

            case R.id.btnDialHome:
                if (PermissionUtil.isPermissionGranted(mBaseActivity, Manifest.permission.CALL_PHONE)) {
                    gotoDialer();
                } else
                    PermissionUtil.requestPermission(mBaseActivity, Manifest.permission.CALL_PHONE, this);
                break;

            case R.id.btnCameraHome:
                gotoCamera(false);
                break;
            case R.id.btnCmeHome:
                goToCmee();
                break;
        }

    }

    private void goToCmee() {
        new Handler().postDelayed(() -> {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.RECORD_AUDIO,
                                Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        10);
            }
            else {
                startActivityForResult(new Intent(getContext(), CmeeSelectorActivity.class), REQ_MESSAGING);
            }

        }, 230);
    }


    @Override
    public void onPermissionsGranted(String permission) {
        switch (permission) {
            case Manifest.permission.WRITE_CONTACTS:
                gotoContact();
                break;
            case Manifest.permission.WRITE_CALL_LOG:
                break;
        }
    }

    @Override
    public void onPermissionsGranted() {

    }

    @Override
    public void onPermissionDenied() {

    }

    private void gotoContact() {
        new Handler().postDelayed(() -> {

            startActivityForResult(new Intent(getContext(), ContactActivity.class), REQ_CONTACT);

        }, 230);
    }

    private void gotoDialer() {
        new Handler().postDelayed(() -> {
            startActivityForResult(new Intent(getContext(), DialerActivity.class), REQ_DIALER);

        }, 230);
    }


    private void gotoApplication() {
        new Handler().postDelayed(() -> {
            startActivityForResult(new Intent(getContext(), AppsActivity.class), REQ_APPS);

        }, 230);

    }

    private void gotoCamera(boolean cameraType) {
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(getActivity(),CustomCameraActivity.class);
            intent.putExtra(key_camera_type,cameraType);
            startActivityForResult(intent, REQ_CAMERA);
        }, 230);

    }


    @OnClick(R.id.btn_change_home_bg)
    public void changeBg(){
        startActivityForResult(new Intent(getActivity(), BackgroundGalleryActivity.class),0x85);
    }

    @OnClick(R.id.btnExit)
    public void logout(){
        // EventBus.getDefault().post(new LoginFailEvent());
    }

    @OnClick(R.id.ivUpdatePic)
    public void onEditPic(){
        showAddPicDialog(false);
    }



    /**
     * Dialog Pic chooser
     */
    private void showAddPicDialog(boolean isIdentityPic) {
        PicModeSelectDialog dialog = new PicModeSelectDialog(getContext(),isIdentityPic);
        dialog.setiPicModeSelectListener(this);
        dialog.show();
    }

    /**
     * Navigate to ImageCropperActivity with provided action {camera-action,gallery-action}
     * @param action
     */
    private void actionPic(String action,boolean isIdentityPic) {
        if(action.equals( Constant.IntentExtras.ACTION_CAMERA)){
            Intent intent = new Intent(getContext(), CustomCameraActivity.class);
            intent.putExtra(key_camera_type, CustomCameraActivity.CameraMode.CAPTURE);
            intent.putExtra(key_camera_for_result, true);
            startActivityForResult(intent, REQUEST_CODE_UPDATE_PIC);
        }else {
            Intent intent = new Intent(getContext(), ImageCropperActivity.class);
            intent.putExtra("ACTION", action);
            intent.putExtra("IDENTITY", isIdentityPic);
            startActivityForResult(intent, isIdentityPic ? REQUEST_CODE_UPDATE_PIC_ID : REQUEST_CODE_UPDATE_PIC);
        }
    }


    /**
     * show updated image
     * @param mImagePath
     */
    private Bitmap getCroppedImageBitmap(String mImagePath) {

        return  BitmapFactory.decodeFile(mImagePath);
    }

    @Override
    public void onPicModeSelected(String mode,boolean isIdentityPic) {
        if( mode.equalsIgnoreCase(PicModes.CANCEL))
            return;
        String action = mode.equalsIgnoreCase(Constant.PicModes.CAMERA) ? Constant.IntentExtras.ACTION_CAMERA : Constant.IntentExtras.ACTION_GALLERY;
        actionPic(action,isIdentityPic);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent result) {

        if (resultCode == RESULT_OK) {
            switch (requestCode){
                case REQUEST_CODE_UPDATE_PIC:
                    profileImagePath = result.getStringExtra(Constant.IntentExtras.IMAGE_PATH);
                    if(profileImagePath!=null) {
                        Picasso.get().load(new File(profileImagePath)).into(mProfileImg);
                        presenter.updateProfilePic(new File(profileImagePath));
                    }
                    break;
                case 900:

                    // Call Action belongs to SosManager
                    manager.callNext();
                    break;

                case 0x85:
                    String bg = result.getStringExtra(Constant.KEY_SELECTED_BG);
                    mBaseActivity.changeBackground(bg);

                    break;
            }

        }

    }


    public void loadProfileImage(){
        try {
            Picasso.get().load(PreferenceUtil.getInstance(getContext()).getAccount().getImageLink()).fit()
                    .placeholder(R.mipmap.avatar_male2).error(R.mipmap.avatar_male2).into(mProfileImg);
        } catch (Exception e) {
            Picasso.get().load(R.mipmap.avatar_male2).fit().into(mProfileImg);
        }
    }

    @Override
    public void onSlideItemClick(String slideItem) {

    }

    @OnClick({R.id.ivUpdateDialHome,R.id.ivUpdateCameraHome,R.id.ivUpdateCmeHome,R.id.ivUpdateSosHome})
    public void onBgChange(View view){
        switch (view.getId()){
            case R.id.ivUpdateDialHome:
                chooseColor(btnDialHome,Color.parseColor(PreferenceUtil.getInstance(getContext()).getColorPreference(HOME_DIAL_BG,"#c3d400")));
                break;
            case R.id.ivUpdateCameraHome:
                chooseColor(btnCameraHome,Color.parseColor(PreferenceUtil.getInstance(getContext()).getColorPreference(HOME_CAMERA_BG,"#0fabec")));
                break;
            case R.id.ivUpdateCmeHome:
                chooseColor(btnCmeHome,Color.parseColor(PreferenceUtil.getInstance(getContext()).getColorPreference(HOME_CME_BG,"#fc5400")));
                break;
            case R.id.ivUpdateSosHome:
                chooseColor(btnSosHome,Color.parseColor(PreferenceUtil.getInstance(getContext()).getColorPreference(HOME_SOS_BG,"#ffe100")));
                break;
        }

    }


    public void  chooseColor(View btn,int currentBackgroundColor){
        ColorPickerDialogBuilder
                .with(getContext())
                .setTitle("Choose color")
                .initialColor(currentBackgroundColor)
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                .density(12)
                .setOnColorSelectedListener(selectedColor -> {
                    String newColor = Integer.toHexString(selectedColor);
                })
                .setPositiveButton("ok", (dialog, selectedColor, allColors) -> changeBackgroundColor(btn,selectedColor))
                .setNegativeButton("cancel", (dialog, which) -> {
                })
                .build()
                .show();
    }

    void changeBackgroundColor(View view,int selectedColor){
        String pref_key="";

        switch (view.getId()){
            case R.id.btnDialHome:
                pref_key=Constant.HOME_DIAL_BG;
                break;
            case R.id.btnCameraHome:
                pref_key=Constant.HOME_CAMERA_BG;
                break;
            case R.id.btnCmeHome:
                pref_key=Constant.HOME_CME_BG;
                break;
            case R.id.btnSosHome:
                pref_key=Constant.HOME_SOS_BG;
                break;
        }
        String colorHex = Integer.toHexString(selectedColor);
        if(colorHex.equals("0"))
            colorHex = "000000";
        PreferenceUtil.getInstance(getContext()).savePreference(pref_key,"#"+colorHex);
        setColorOnBtn(selectedColor,view);
    }

    public void animateSosCounter(){
        animCancel=false;
        animation.setRepeatCount(2);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                tvCounter.setVisibility(View.VISIBLE);

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                tvCounter.setVisibility(View.INVISIBLE);
                if(!animCancel)
                    manager.start();
                counter=1;
                tvCounter.setText(String.valueOf(counter));


            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                counter +=1;
                new Handler().postDelayed(() -> tvCounter.setText(String.valueOf(counter)),100);
            }
        });
        animation.setDuration(1000);
        animation.setRepeatMode(Animation.RESTART);
        tvCounter.startAnimation(animation);


    }

}
