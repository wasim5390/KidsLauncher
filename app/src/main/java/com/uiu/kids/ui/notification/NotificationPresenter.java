package com.uiu.kids.ui.notification;

import com.uiu.kids.model.NotificationsItem;
import com.uiu.kids.model.NotificationsListResponse;
import com.uiu.kids.model.User;
import com.uiu.kids.source.DataSource;
import com.uiu.kids.source.Repository;
import com.uiu.kids.util.PreferenceUtil;

import java.util.ArrayList;
import java.util.List;

public class NotificationPresenter implements NotificationContract.Presenter {

    public NotificationContract.View view;
    public Repository repository;
    public PreferenceUtil preferenceUtil;
    public List<NotificationsItem> notificationList;
    private User account;
    private int totalPageCount=1;
    private int currentPage=0;

    public NotificationPresenter(NotificationContract.View view, PreferenceUtil preferenceUtil, Repository repository) {
        this.view = view;
        this.repository = repository;
        this.preferenceUtil = preferenceUtil;
        this.account = preferenceUtil.getAccount();
        view.setPresenter(this);
    }

    @Override
    public void start() {
        notificationList= new ArrayList<>();

    }

    @Override
    public void fetchNotifications() {
        if(currentPage>=totalPageCount)
            return;
        repository.getNotificationsList(account.getId(), String.valueOf(currentPage), new DataSource.GetDataCallback<NotificationsListResponse>() {
            @Override
            public void onDataReceived(NotificationsListResponse data) {
                currentPage++;
                totalPageCount = data.getData().getPageCount();
                view.onNotificationListLoaded(data.getData());
            }

            @Override
            public void onFailed(int code, String message) {
                view.showMessage(message);
            }
        });
    }
}
