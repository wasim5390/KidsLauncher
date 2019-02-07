package com.uiu.kids;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.SearchView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.uiu.kids.ui.dashboard.GoogleLoginDialog;
import com.uiu.kids.ui.slides.reminder.AlarmReceiver;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.ButterKnife;

import static android.content.Context.ALARM_SERVICE;

/**
 * Created by sidhu on 4/12/2018.
 */

public abstract class BaseFragment  extends Fragment implements Constant {
    public BaseActivity mBaseActivity;
    public View view;
    public Animation animScale;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BaseActivity) {
            mBaseActivity = (BaseActivity) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(getID(), container, false);
        ButterKnife.bind(this,view);
        this.view = view;
        animScale = AnimationUtils.loadAnimation(getContext(), com.uiu.kids.R.anim.anim_scale);
        initUI(view);
        return view;
    }

    public abstract int getID();

    public abstract void initUI(View view);

    public View getView(){
        return this.view;
    }

    public void showProgress() {
        if(mBaseActivity!=null && KidsLauncherApp.getInstance().isForeground())
        mBaseActivity.showProgress();
    }

    public void hideProgress() {
        mBaseActivity.hideProgress();
    }

    public void showGoogleLoginDialog(GoogleLoginDialog mCallback){
        mBaseActivity.loginWithGoogle(mCallback);
    }

    public static void setColorOnBtn(int colorRes, View btn){
        LayerDrawable shape = (LayerDrawable)btn.getBackground();
        GradientDrawable gradientDrawableTop = (GradientDrawable) shape
                .findDrawableByLayerId(R.id.homeBtnTopId);
        gradientDrawableTop.setColor(colorRes);

        btn.setBackground(shape);
    }

    public void setHintForInputNumber(String code, EditText editText) {
        Phonenumber.PhoneNumber phoneNumber = PhoneNumberUtil.getInstance().getExampleNumber(code);
        if (phoneNumber!=null) {
            String number = PhoneNumberUtil.getInstance().format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.E164);
            editText.setHint(getString(com.uiu.kids.R.string.phone_number_template, number));
        } else {
            editText.setHint(com.uiu.kids.R.string.mobile_number);
        }
    }
    protected File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        return image;
    }


    protected void updateSearchView(SearchView searchView){
        EditText searchEditText = searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(getResources().getColor(com.uiu.kids.R.color.text_color_grey));
        searchEditText.setHintTextColor(getResources().getColor(com.uiu.kids.R.color.text_color_grey));
        searchEditText.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 28F, getResources().getDisplayMetrics()));
        Typeface typeface = ResourcesCompat.getFont(getContext(), com.uiu.kids.R.font.helvetica_neue_lt);
        searchEditText.setTypeface(typeface);
    }

    protected void showCenterToast(String text) {
        Toast toast  = Toast.makeText(getActivity(),text,Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER_VERTICAL,0,0);
        toast.show();
    }

}
