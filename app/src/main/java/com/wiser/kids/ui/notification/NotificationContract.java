package com.wiser.kids.ui.notification;

import com.wiser.kids.BasePresenter;
import com.wiser.kids.BaseView;

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
