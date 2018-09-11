package com.uiu.kids.ui.notification;

import com.uiu.kids.BasePresenter;
import com.uiu.kids.BaseView;

import java.util.List;

public class NotificationContract {

    interface View extends BaseView<Presenter>
    {
        void loadNotificationList(List<NotificationEntity> list);
    }
    interface Presenter extends BasePresenter
    {

    }

}
