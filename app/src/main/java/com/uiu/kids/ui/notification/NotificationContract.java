package com.uiu.kids.ui.notification;

import com.uiu.kids.BasePresenter;
import com.uiu.kids.BaseView;
import com.uiu.kids.model.Data;
import com.uiu.kids.model.NotificationsListResponse;

import java.util.List;

public class NotificationContract {

    interface View extends BaseView<Presenter>
    {
        void onNotificationListLoaded(Data notificationsData);
        void showMessage(String message);
    }

    interface Presenter extends BasePresenter
    {
        void fetchNotifications();
    }

}
