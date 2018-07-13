package com.wiser.kids.ui.home.dialer.callhistory;

import android.content.Context;
import android.provider.CallLog;

import com.wiser.kids.source.Repository;
import com.wiser.kids.ui.home.dialer.DialerContract;
import com.wiser.kids.util.CallLogManager;
import com.wiser.kids.util.CallRecord;
import com.wiser.kids.util.PreferenceUtil;

import java.util.ArrayList;

public class CallHistoryPresenter implements CallHistoryContract.Presenter {

    private CallHistoryContract.View view;
    private PreferenceUtil preferenceUtil;
    private Repository repository;
    private ArrayList<CallRecord> callList=new ArrayList<>();

    public CallHistoryPresenter(CallHistoryContract.View view, PreferenceUtil util, Repository repository) {
        this.view = view;
        this.preferenceUtil = util;
        this.repository = repository;
        this.view.setPresenter(this);
    }


    @Override
    public void start() {

    }


    @Override
    public void listLoad(Context context) {
    callList= CallLogManager.getInstance(context).getCallRecords();
    CallHistoryAdapter adapter=new CallHistoryAdapter(context,callList);
    view.listLoaded(adapter);
    }
}
