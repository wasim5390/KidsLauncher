package com.wiser.kids;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;


import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.wiser.kids.util.Util;

import butterknife.ButterKnife;

/**
 * Created by sidhu on 4/12/2018.
 */

public abstract class BaseFragment  extends Fragment implements Constant{
    public BaseActivity mBaseActivity;
    public View view;

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
        initUI(view);
        return view;
    }

    public abstract int getID();

    public abstract void initUI(View view);

    public View getView(){
        return this.view;
    }

    public void showProgress() {
        mBaseActivity.showProgress();
    }

    public void hideProgress() {
        mBaseActivity.hideProgress();
    }

    public void setHintForInputNumber(String code, EditText editText) {
        Phonenumber.PhoneNumber phoneNumber = PhoneNumberUtil.getInstance().getExampleNumber(code);
        if (phoneNumber!=null) {
            String number = PhoneNumberUtil.getInstance().format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.E164);
            editText.setHint(getString(R.string.phone_number_template, number));
        } else {
            editText.setHint(R.string.mobile_number);
        }
    }

}
