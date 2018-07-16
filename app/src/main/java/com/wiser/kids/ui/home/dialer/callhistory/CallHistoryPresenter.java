package com.wiser.kids.ui.home.dialer.callhistory;

import android.content.Context;

import com.wiser.kids.source.Repository;
import com.wiser.kids.util.CallLogManager;
import com.wiser.kids.util.CallRecord;
import com.wiser.kids.util.PreferenceUtil;

import java.util.ArrayList;
import java.util.List;

public class CallHistoryPresenter implements CallHistoryContract.Presenter {

    private CallHistoryContract.View view;
    private PreferenceUtil preferenceUtil;
    private Repository repository;
    private List<CallRecord> callRecordList=new ArrayList<>();

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
    callRecordList= CallLogManager.getInstance(context).getCallRecords();
    CallHistoryAdapter adapter=new CallHistoryAdapter(context,callRecordList);
    view.listLoaded(adapter);
    }
}
