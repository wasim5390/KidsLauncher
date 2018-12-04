package com.uiu.kids.ui.call;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
            if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                String phoneNumber = extras
                        .getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
                boolean sosNumber=false;
                Log.w("MY_DEBUG_TAG", phoneNumber);


                TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                try {
                    Class c = Class.forName(tm.getClass().getName());
                    Method m = c.getDeclaredMethod("getITelephony");
                    m.setAccessible(true);
                    ITelephony telephonyService = (ITelephony) m.invoke(tm);
                    List<ContactEntity> sosList = PreferenceUtil.getInstance(context).getAllSosList();
                    sosList=sosList==null?new ArrayList<>():sosList;
                    Log.e("INCOMING", phoneNumber);
                    for(ContactEntity sos:sosList){
                        if(sos.getAllNumbers().contains(phoneNumber)){
                            sosNumber=true;
                            break;
                        }

                    }
                    if ((phoneNumber != null) && !sosNumber && KidsLauncherApp.isSleepModeActive()) {
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
}
