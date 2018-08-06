package com.wiser.kids.ui.reminder;

import com.wiser.kids.model.SlideItem;
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
        ReminderEntity entity=new ReminderEntity(null,null,null);
        entity.setFlagEmpty(true);
        mReminderList.add(entity);
        view.onLoadedReminderList(mReminderList);
        onLoadReminderList();
    }

    private void onLoadReminderList() {

    }


}
