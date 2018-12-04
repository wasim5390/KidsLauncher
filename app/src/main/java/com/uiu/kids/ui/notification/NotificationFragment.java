package com.uiu.kids.ui.notification;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.uiu.kids.BaseFragment;
import com.uiu.kids.R;
import com.uiu.kids.model.Data;
import com.uiu.kids.model.NotificationsItem;

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
    public void onSlideItemClick(NotificationsItem slideItem) {

    }

    @Override
    public void onNotificationListLoaded(Data notificationsData) {
        adapter.setSlideItems(notificationsData.getNotifications());
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(mBaseActivity, message, Toast.LENGTH_SHORT).show();
    }
}
