package com.uiu.kids.ui;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.uiu.kids.ui.home.contact.ContactEntity;

import java.util.List;

public class SosManager {
    List<ContactEntity> sosList;
    Context context;
    ICallListener listener;
    int currentIndex=0;
    CallManager manager;

    public SosManager(List<ContactEntity> sosList, Context context,ICallListener listener) {
        this.sosList = sosList;
        this.context = context;
        this.listener = listener;
    }

    public void start(){
        if(sosList.isEmpty())
            return;
         manager = new CallManager(listener);
        TelephonyManager mTM = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        mTM.listen(manager, PhoneStateListener.LISTEN_CALL_STATE);

        startCalling(sosList,currentIndex,listener);
    }


    private void startCalling(List<ContactEntity> contactEntities,int index,ICallListener listener){
        try {

            if(index>=contactEntities.size()) {
                listener.stateAllCalled();
                TelephonyManager mTM = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                mTM.listen(manager, PhoneStateListener.LISTEN_NONE);
                manager=null;
                return;
            }
            String numberToDial = null;
            ContactEntity entity = contactEntities.get(index);
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            if(entity.getMobileNumber()!=null)
                numberToDial=entity.getMobileNumber();
            else if(entity.getmHomeNumber()!=null)
                numberToDial = entity.getmHomeNumber();
            else
                return;

            callIntent.setData(Uri.parse("tel:" + numberToDial));

            if (ActivityCompat.checkSelfPermission(context,
                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            ((Activity)context).startActivityForResult(callIntent,900);

        } catch (ActivityNotFoundException activityException) {
            Log.e("dialing-example", "Call failed", activityException);
        }
    }

    public void callNext(){
        startCalling(sosList,currentIndex++,listener);
    }

    public class CallManager extends PhoneStateListener {
        public boolean wasRinging;
        String LOG_TAG = "PhoneListener";
        ICallListener callListener;
        public CallManager(ICallListener listener) {
            this.callListener = listener;
        }

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            switch(state){
                case TelephonyManager.CALL_STATE_RINGING:
                    Log.i(LOG_TAG, "RINGING");
                    wasRinging = true;
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    Log.i(LOG_TAG, "OFFHOOK");


                    if (!wasRinging) {
                     //   startCalling(sosList,currentIndex++,callListener);
                        // Start your new activity
                    } else {
                        // Cancel your old activity
                    }

                    // this should be the last piece of code before the break
                    wasRinging = true;
                    break;
                case TelephonyManager.CALL_STATE_IDLE:
                    Log.i(LOG_TAG, "IDLE");
                    // this should be the last piece of code before the break
                    if(wasRinging)
                    startCalling(sosList,++currentIndex,callListener);
                    wasRinging = false;
                    break;
            }
        }


    }


    public interface ICallListener{
        void stateAllCalled();
    }


}
