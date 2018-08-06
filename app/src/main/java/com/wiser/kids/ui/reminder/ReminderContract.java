package com.wiser.kids.ui.reminder;

import com.wiser.kids.BasePresenter;
import com.wiser.kids.BaseView;

import java.util.List;

public class ReminderContract {

    interface View extends BaseView<Presenter>
    {
       void onLoadedReminderList(List<ReminderEntity> list);
    }
    interface Presenter extends BasePresenter
    {

    }
}
