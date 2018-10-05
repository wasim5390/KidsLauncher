package com.uiu.kids.ui.slides.reminder;

import com.uiu.kids.model.Slide;
import com.uiu.kids.model.response.ReminderResponse;
import com.uiu.kids.source.DataSource;
import com.uiu.kids.source.Repository;
import com.uiu.kids.util.PreferenceUtil;

import java.util.ArrayList;
import java.util.List;

public class ReminderPresenter implements ReminderContract.Presenter {
    public ReminderContract.View view;
    public PreferenceUtil preferenceUtil;
    public Slide slideItem;
    public Repository repository;
    public List<ReminderEntity> mReminderList;
    public List<ReminderEntity> isActiveReminderlist;

    public ReminderPresenter(ReminderContract.View view, Slide slideItem, PreferenceUtil preferenceUtil, Repository repository) {
        this.repository = repository;
        this.slideItem = slideItem;
        this.preferenceUtil = preferenceUtil;
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public void start() {

        mReminderList = new ArrayList<>();
        isActiveReminderlist=new ArrayList<>();
        view.slideSerial(slideItem.getSerial(),slideItem.getCount());
        onLoadReminderList();
    }

    @Override
    public void onLoadReminderList() {

        repository.fetchReminderList(slideItem.getId(), new DataSource.GetDataCallback<ReminderResponse>() {
            @Override
            public void onDataReceived(ReminderResponse data) {

                if (data != null) {
                    if (data.isSuccess()) {
                        mReminderList.clear();
                        mReminderList.addAll(data.getReminders());
                        view.checkTime(mReminderList);
                    }
                }
            }

            @Override
            public void onFailed(int code, String message) {
                view.checkTime(mReminderList);
                view.showMessage(message);

            }
        });
    }

    @Override
    public void onReminderListchecked(List<ReminderEntity> list) {

        for(int i=0;i<list.size();i++)
        {
         if(list.get(i).isActiveReminder())
         {
             isActiveReminderlist.add(list.get(i));
         }
        }

        view.onLoadedReminderList(isActiveReminderlist);

    }

    @Override
    public void reLoadedReminderList() {
        mReminderList.clear();
        isActiveReminderlist.clear();
        onLoadReminderList();
    }
}
