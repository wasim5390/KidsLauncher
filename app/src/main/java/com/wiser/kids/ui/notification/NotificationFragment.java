package com.wiser.kids.ui.notification;

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
import com.wiser.kids.ui.message.MessageAdapterList;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class NotificationFragment extends BaseFragment implements NotificationContract.View,NotificationAdapterList.Callback {

    @BindView(R.id.rvNotification)
    public RecyclerView recyclerView;

    public NotificationContract.Presenter presenter;
    public NotificationAdapterList adapter;

    public static NotificationFragment newInstance()
    {
        Bundle args=new Bundle();
        NotificationFragment instance=new NotificationFragment();
        instance.setArguments(args);
        return instance;
    }

    @Override
    public int getID() {
        return R.layout.fragment_notification;
    }

    @Override
    public void initUI(View view) {
        ButterKnife.bind(getActivity());
        setAdapter();
        presenter.start();
    }

    private void setAdapter() {
        adapter = new NotificationAdapterList(getContext(), this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void setPresenter(NotificationContract.Presenter presenter) {
        this.presenter=presenter;
    }

    @Override
    public void showNoInternet() {

    }
    @Override
    public void loadNotificationList(List<NotificationEntity> list) {
        adapter.setSlideItems(list);
    }

    @Override
    public void onSlideItemClick(NotificationEntity slideItem) {

    }
}
