package com.wiser.kids.ui.notification;

import com.wiser.kids.source.Repository;
import com.wiser.kids.util.PreferenceUtil;

import java.util.ArrayList;
import java.util.List;

public class NotificationPresenter implements NotificationContract.Presenter {

    public NotificationContract.View view;
    public Repository repository;
    public PreferenceUtil preferenceUtil;
    public List<NotificationEntity> notificationList;

    public NotificationPresenter(NotificationContract.View view, PreferenceUtil preferenceUtil, Repository repository) {
        this.view = view;
        this.repository = repository;
        this.preferenceUtil = preferenceUtil;
        view.setPresenter(this);
    }

    @Override
    public void start() {
        notificationList= new ArrayList<>();
        NotificationEntity notificationEntity=new NotificationEntity();
        notificationEntity.setTitle("Title");
        notificationEntity.setSenderName("Alex Ansari");
        notificationEntity.setMessage("My name is abid and i have done my degree from " +
                "XYZ university but now i'm doing a job as a android developer");
        notificationList.add(notificationEntity);

        NotificationEntity notificationEntity2=new NotificationEntity();
        notificationEntity2.setTitle("Title");
        notificationEntity2.setSenderName("Alex Ansari");
        notificationEntity2.setMessage("My name is abid and i have done my degree from " +
                "XYZ university but now i'm doing a job as a android developer");
        notificationList.add(notificationEntity2);

        view.loadNotificationList(notificationList);

    }
}
