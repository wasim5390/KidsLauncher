package com.uiu.kids.ui.home;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Window;
import android.view.WindowManager;


import com.uiu.kids.BaseActivity;
import com.uiu.kids.Constant;
import com.uiu.kids.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.uiu.kids.BaseActivity.showImmersiveDialog;

/**
 * Created by sidhu on 2/7/2018.
 */

public class PicModeSelectDialog extends Dialog {


    private IPicModeSelectListener iPicModeSelectListener;
    private boolean isIdentityPic;

    public PicModeSelectDialog(Activity context,boolean isIdentityPic) {
        super(context);
        this.isIdentityPic=isIdentityPic;
        WindowManager manager = (WindowManager)context. getSystemService(Activity.WINDOW_SERVICE);
        int width, height;


        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.FROYO) {
            width = manager.getDefaultDisplay().getWidth();
            height = manager.getDefaultDisplay().getHeight();
        } else {
            Point point = new Point();
            manager.getDefaultDisplay().getSize(point);
            width = point.x;
            height = point.y;
        }

        Window window = getWindow();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        int bgColor = context.getResources().getColor(android.R.color.transparent);
        window.setBackgroundDrawable(new ColorDrawable(bgColor));
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        setContentView(getLayoutInflater().inflate(R.layout.pic_capture_dialog,null));
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(getWindow().getAttributes());
        lp.width = width;
        lp.height = height;
        getWindow().setAttributes(lp);
        changeBarcolor();
        setCancelable(true);
        showImmersiveDialog(this,context);
        ButterKnife.bind(this);

    }

    public void setiPicModeSelectListener(IPicModeSelectListener iPicModeSelectListener) {
        this.iPicModeSelectListener = iPicModeSelectListener;
    }

    public interface IPicModeSelectListener {
        void onPicModeSelected(String mode, boolean isIdentityPic);
    }

    public void changeBarcolor() {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(Color.parseColor("#80000000"));
        }
    }

    @OnClick(R.id.view_camera)
    public void onCamera(){
        if (iPicModeSelectListener != null)
            iPicModeSelectListener.onPicModeSelected(Constant.PicModes.CAMERA,isIdentityPic);
        this.dismiss();
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(new Intent("immersive"));
    }
    @OnClick(R.id.view_gallery)
    public void onGallery(){
        if (iPicModeSelectListener != null)
            iPicModeSelectListener.onPicModeSelected(Constant.PicModes.GALLERY,isIdentityPic);
        this.dismiss();
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(new Intent("immersive"));
    }
    @OnClick(R.id.view_cancel)
    public void onCancel(){
        if (iPicModeSelectListener != null)
            iPicModeSelectListener.onPicModeSelected(Constant.PicModes.CANCEL,isIdentityPic);
        this.cancel();
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(new Intent("immersive"));
    }


}