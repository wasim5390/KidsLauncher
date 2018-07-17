package com.wiser.kids.ui.home.messaging;

import android.Manifest;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.wiser.kids.BaseFragment;
import com.wiser.kids.R;
import com.wiser.kids.util.PermissionUtil;

public class MessageFragment extends BaseFragment implements MessageContract.View {

    private MessageContract.Presenter presenter;
    private final int REQ_SMS=0x022;

    public static MessageFragment newInstance()
    {
        Bundle arg=new Bundle();
        MessageFragment instance=new MessageFragment();
        instance.setArguments(arg);

        return instance;
    }

    @Override
    public int getID() {
        return R.layout.fragment_message;
    }

    @Override
    public void initUI(View view) {


        allowSMS();

    }

    private void allowSMS() {
        if(PermissionUtil.isPermissionGranted(getContext(), Manifest.permission.SEND_SMS))
            sendSMS();
        else
            PermissionUtil.requestPermission(getActivity(), Manifest.permission.SEND_SMS, new PermissionUtil.PermissionCallback() {
                @Override
                public void onPermissionGranted(String permission) {
                    sendSMS();
                }

                @Override
                public void onPermissionGranted() {

                }

                @Override
                public void onPermissionDenied() {

                }
            });
        }


    @Override
    public void setPresenter(MessageContract.Presenter presenter) {
        this.presenter=presenter;
    }


    @Override
    public void showNoInternet() {

    }

    public void sendSMS() {
        Intent smsIntent = new Intent(Intent.ACTION_VIEW);
        smsIntent.setData(Uri.parse("sms:" +"abid"));
        try {
            startActivityForResult(smsIntent,REQ_SMS);
        } catch (ActivityNotFoundException ex) {
            // @todo replace toast with in-app notification dialog
            Toast.makeText(mBaseActivity, getString(R.string.no_sms_clients), Toast.LENGTH_SHORT).show();
            // Utils.showAlertError(mContext.getString(R.string.no_sms_clients), true);
        }
    }


    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);

        if (requestCode==REQ_SMS)
        {
         //   getActivity().onBackPressed();
        }


    }
}
