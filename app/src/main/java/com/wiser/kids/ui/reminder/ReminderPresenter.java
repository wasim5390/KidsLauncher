package com.wiser.kids.ui.reminder;

import android.util.Log;

import com.wiser.kids.model.SlideItem;
import com.wiser.kids.model.response.ReminderResponse;
import com.wiser.kids.source.DataSource;
import com.wiser.kids.source.Repository;
import com.wiser.kids.util.PreferenceUtil;

import java.util.ArrayList;
import java.util.List;

public class ReminderPresenter implements ReminderContract.Presenter {
    public ReminderContract.View view;
    public PreferenceUtil preferenceUtil;
    public SlideItem slideItem;
    public Repository repository;
    public List<ReminderEntity> mReminderList;

    public ReminderPresenter(ReminderContract.View view, SlideItem slideItem, PreferenceUtil preferenceUtil, Repository repository) {
        this.repository = repository;
        this.slideItem = slideItem;
        this.preferenceUtil = preferenceUtil;
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public void start() {

        mReminderList=new ArrayList<>();
        onLoadReminderList();
    }

    @Override
    public void onLoadReminderList() {

        repository.fetchReminderList(slideItem.getId(), new DataSource.GetDataCallback<ReminderResponse>() {
            @Override
            public void onDataReceived(ReminderResponse data) {

                if (data != null) {
                    mReminderList.addAll(data.getReminders());
                    view.onLoadedReminderList(mReminderList);
                }
            }

            @Override
            public void onFailed(int code, String message) {
                view.onLoadedReminderList(mReminderList);
                view.showMessage(message);

            }
        });
    }
}
