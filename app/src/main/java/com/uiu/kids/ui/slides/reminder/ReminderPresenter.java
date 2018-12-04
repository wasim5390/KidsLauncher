package com.uiu.kids.ui.slides.reminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;

import com.uiu.kids.model.Slide;
import com.uiu.kids.model.response.ReminderResponse;
import com.uiu.kids.source.DataSource;
import com.uiu.kids.source.Repository;
import com.uiu.kids.util.PreferenceUtil;
import com.uiu.kids.util.Util;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.ALARM_SERVICE;

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
        mReminderList = new ArrayList<>();
        isActiveReminderlist=new ArrayList<>();
        this.view.setPresenter(this);
    }

    @Override
    public void start() {
        view.slideSerial(slideItem.getSerial(),slideItem.getCount());
        List<ReminderEntity> localList= preferenceUtil.getReminderList(slideItem.getId());
        if(localList!=null)
            loadFromLocal(localList);
        else
            onLoadReminderList();
    }

    private void loadFromLocal(List<ReminderEntity> localList) {

        mReminderList.clear();
        mReminderList.addAll(localList);
        view.onLoadedReminderList(mReminderList);
    }

    @Override
    public void onLoadReminderList() {
        if(!Util.isInternetAvailable()) {
            view.showNoInternet();
            return;
        }
        repository.fetchReminderList(slideItem.getId(), new DataSource.GetDataCallback<ReminderResponse>() {
            @Override
            public void onDataReceived(ReminderResponse data) {

                if (data != null) {
                    if (data.isSuccess()) {
                        preferenceUtil.saveReminders(slideItem.getId(),data.getReminders());
                        mReminderList.clear();
                        mReminderList.addAll(data.getReminders());
                        view.onLoadedReminderList(mReminderList);
                        // view.checkTime(mReminderList);
                    }
                    else{
                        preferenceUtil.saveReminders(slideItem.getId(),mReminderList);
                        view.onLoadedReminderList(mReminderList);
                    }
                }
            }

            @Override
            public void onFailed(int code, String message) {
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
