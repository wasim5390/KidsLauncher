package com.wiser.kids.ui.home.dialer;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import com.wiser.kids.BaseFragment;
import com.wiser.kids.R;
import com.wiser.kids.util.PermissionUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;


public class DialerFragment extends BaseFragment implements DialerContract.View{

    DialerContract.Presenter presenter;
    @BindView(R.id.dialer_phone_number)
    TextView mDigitsTv;

    protected String mPhoneNumber = "";
    private ToneGenerator mTone;
    private int tone;
    private static final int[] numpadIds = {R.id.numpad0,R.id.numpad1,R.id.numpad2,R.id.numpad3,R.id.numpad4
            ,R.id.numpad5,R.id.numpad6,R.id.numpad7,R.id.numpad8,R.id.numpad9
    };

    public static DialerFragment newInstance() {
        Bundle args = new Bundle();
        DialerFragment dialerFragment = new DialerFragment();
        dialerFragment.setArguments(args);
        return dialerFragment;
    }

    @Override
    public int getID() {
        return R.layout.fragment_dialer;
    }

    @Override
    public void initUI(View view) {
        ButterKnife.bind(this,view);
        mTone = new ToneGenerator(AudioManager.STREAM_RING, 100);
        setAlphabetsOnNumPad();


    }
    private void setDigitText (int id) {
        TextView tv = view.findViewById(id);
        String str = tv.getText().toString();
        Spannable span = new SpannableString(str);
        int size = getResources().getDimensionPixelSize(R.dimen.numpad_size);
        int color = ContextCompat.getColor(getActivity(),R.color.numpad_letter_color);
        span.setSpan(new AbsoluteSizeSpan(size), 1, str.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        span.setSpan(new ForegroundColorSpan(color), 1, str.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);

        tv.setText(span);
    }

    private void setAlphabetsOnNumPad(){
        for(int index=0;index<numpadIds.length;index++)
            setDigitText(numpadIds[index]);
    }

    @Override
    public void showMessage() {

    }

    @Override
    public void setPresenter(DialerContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showNoInternet() {

    }

    @OnClick({R.id.numpad0,R.id.numpad1,R.id.numpad2,R.id.numpad3,R.id.numpad4
            ,R.id.numpad5,R.id.numpad6,R.id.numpad7,R.id.numpad8,R.id.numpad9,R.id.numpad_pound,
            R.id.numpad_star, R.id.dialer_delete_btn})
    public void onNumClick(View view) {
        tone = ToneGenerator.TONE_DTMF_9;
        switch (view.getId()) {
            case R.id.numpad0:
                mPhoneNumber += "0";
                tone = ToneGenerator.TONE_DTMF_0;
                break;
            case R.id.numpad1:
                mPhoneNumber += "1";
                tone = ToneGenerator.TONE_DTMF_1;
                break;
            case R.id.numpad2:
                mPhoneNumber += "2";
                tone = ToneGenerator.TONE_DTMF_2;
                break;

            case R.id.numpad3:
                mPhoneNumber += "3";
                tone = ToneGenerator.TONE_DTMF_3;
                break;

            case R.id.numpad4:
                mPhoneNumber += "4";
                tone = ToneGenerator.TONE_DTMF_4;
                break;
            case R.id.numpad5:
                mPhoneNumber += "5";
                tone = ToneGenerator.TONE_DTMF_5;
                break;

            case R.id.numpad6:
                mPhoneNumber += "6";
                tone = ToneGenerator.TONE_DTMF_6;
                break;
            case R.id.numpad7:
                mPhoneNumber += "7";
                tone = ToneGenerator.TONE_DTMF_7;
                break;
            case R.id.numpad8:
                mPhoneNumber += "8";
                tone = ToneGenerator.TONE_DTMF_8;
                break;
            case R.id.numpad9:
                mPhoneNumber += "9";
                tone = ToneGenerator.TONE_DTMF_9;
                break;
            case R.id.numpad_star:
                mPhoneNumber += "*";
                tone = ToneGenerator.TONE_DTMF_P;
                break;

            case R.id.numpad_pound:
                mPhoneNumber += "#";
                tone = ToneGenerator.TONE_DTMF_S;
                break;
            case R.id.dialer_delete_btn:
                if (!TextUtils.isEmpty(mPhoneNumber)) {
                    mPhoneNumber = mPhoneNumber.substring(0,mPhoneNumber.length()-1);
                }
                break;
        }

        if (mTone != null) {
            mTone.startTone(tone, 150);
        }
        mDigitsTv.setText(mPhoneNumber);


    }

    public void onCallClicked(String numberToCall) {
        if (TextUtils.isEmpty(numberToCall)) {
            //TODO: Ran show tip
            return;
        }

        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + numberToCall));
        if(PermissionUtil.isPermissionGranted(getActivity(), Manifest.permission.CALL_PHONE))
            try {
                startActivity(callIntent);
            }catch (ActivityNotFoundException e){
            e.printStackTrace();
            }
    }

    @OnClick(R.id.dialer_call_btn)
    public void onCallClick() {
        onCallClicked(mPhoneNumber);

    }

    @OnLongClick(R.id.dialer_delete_btn)
    public boolean onClearClick(){
        mPhoneNumber = "";
        mDigitsTv.setText(mPhoneNumber);
        return true;
    }
}
