package com.wiser.kids.ui.home.dialer.callhistory;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wiser.kids.BaseFragment;
import com.wiser.kids.R;
import com.wiser.kids.ui.home.contact.ContactEntity;
import com.wiser.kids.util.CallLogManager;
import com.wiser.kids.util.CallRecord;

import java.util.ArrayList;
import java.util.List;

public class CallHistoryFragment extends BaseFragment implements CallHistoryContract.View,CallHistoryAdapter.onHistoryItemClick {

    private  CallHistoryContract.Presenter presenter;
    private RecyclerView callHistorylist;
    private List<CallRecord> callRecordList=new ArrayList<>();



    public static CallHistoryFragment newInstance()
    {
        Bundle arg=new Bundle();
        CallHistoryFragment instance= new CallHistoryFragment();
        instance.setArguments(arg);
        return instance;
    }

    @Override
    public int getID() {
        return R.layout.fragment_call_history;
    }

    @Override
    public void initUI(View view) {

        init(view);
       loadCallLog();
    }

    private void loadCallLog() {
        callRecordList= CallLogManager.getInstance(getContext()).getCallRecords();
        CallHistoryAdapter adapter=new CallHistoryAdapter(getContext(),callRecordList);
        presenter.callLogLoading(adapter);
    }


    private void init(View view) {
        callHistorylist=(RecyclerView)view.findViewById(R.id.callhistoryListView);

    }

    @Override
    public void setPresenter(CallHistoryContract.Presenter presenter) {

        this.presenter=presenter;

    }

    @Override
    public void showNoInternet() {

    }

    @Override
    public void callLogLoaded(CallHistoryAdapter adapter) {
        callHistorylist.setLayoutManager(new LinearLayoutManager(getContext()));
        callHistorylist.setAdapter(adapter);
    }

    @Override
    public void onContactSelected(CallRecord callRecord) {

        ContactEntity contactEntity=new ContactEntity();
        contactEntity.setName(callRecord.displayName);
        contactEntity.setmPhoneNumber(callRecord.phoneNumber);
        contactEntity.setmHomeNumber(callRecord.);
        contactEntity

    }
}
