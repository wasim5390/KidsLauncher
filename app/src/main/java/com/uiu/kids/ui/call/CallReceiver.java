package com.uiu.kids.ui.call;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.internal.telephony.ITelephony;
import com.uiu.kids.KidsLauncherApp;
import com.uiu.kids.ui.home.contact.ContactEntity;
import com.uiu.kids.util.PreferenceUtil;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class CallReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            String state = extras.getString(TelephonyManager.EXTRA_STATE);
            Log.w("MY_DEBUG_TAG", state);
           /* if(state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)){
                String number = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
                if(isAcceptable(context,number));
                setResultData(null);
                return;
            }*/

            if (state.equals(TelephonyManager.EXTRA_STATE_RINGING) || state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
                String phoneNumber = extras
                        .getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
               // Log.w("MY_DEBUG_TAG", phoneNumber);

                TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                try {
                    Class c = Class.forName(tm.getClass().getName());
                    Method m = c.getDeclaredMethod("getITelephony");
                    m.setAccessible(true);
                    ITelephony telephonyService = (ITelephony) m.invoke(tm);

                   // if ((phoneNumber != null) && !sosNumber && KidsLauncherApp.isSleepModeActive()) {
                    if ((phoneNumber != null) && !isAcceptable(context,phoneNumber) ){
                        telephonyService.silenceRinger();
                        telephonyService.endCall();
                        Log.e("HANG UP", phoneNumber);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private boolean isAcceptable(Context context, String phoneNumber){
        List<ContactEntity> sosList = PreferenceUtil.getInstance(context).getAllSosList();
        List<ContactEntity> favList = PreferenceUtil.getInstance(context).getAllFavPeoples(PreferenceUtil.getInstance(context).getAccount().getId());
        sosList=sosList==null?new ArrayList<>():sosList;
        favList=favList==null?new ArrayList<>():favList;
        sosList.addAll(favList);
        Log.e("INCOMING", phoneNumber);
        for(ContactEntity sos:sosList){
            if(sos.getAllNumbers().contains(phoneNumber)){
                return true;
            }

        }

        return false;
    }
}
