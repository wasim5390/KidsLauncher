package com.uiu.kids.ui.call;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.telephony.PhoneNumberUtils;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.internal.telephony.ITelephony;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.uiu.kids.KidsLauncherApp;
import com.uiu.kids.ui.home.contact.ContactEntity;
import com.uiu.kids.util.PreferenceUtil;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class CallReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            String state = extras.getString(TelephonyManager.EXTRA_STATE);
            if(state==null){
                String number = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
                if(number!=null && !isAcceptable(context,number))
                setResultData(null);
                return;
            }

            if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)
                    || state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
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
        List<ContactEntity> mergedList = new ArrayList<>();
        List<ContactEntity> sosList = new ArrayList<>();
        sosList.addAll(PreferenceUtil.getInstance(context).getAllSosList());
        List<ContactEntity> favList = PreferenceUtil.getInstance(context).getAllFavPeoples(PreferenceUtil.getInstance(context).getAccount().getId());
        favList=favList==null?new ArrayList<>():favList;
        mergedList.addAll(favList);
        mergedList.addAll(sosList);
       // favList.addAll(sosList);

        Log.e("INCOMING", phoneNumber);
        for(ContactEntity item:mergedList){

            for(String mobileNumber:item.getAllNumbers()){
                Log.e("INCOMING_AVAILEBLE", mobileNumber);
               boolean matched= PhoneNumberUtils.compare(context,mobileNumber,phoneNumber);
               if(matched)
                   return true;
            }
           /* if(item.getAllNumbers().contains(phoneNumber)){
                return true;
            }*/

        }

        return false;
    }
}
