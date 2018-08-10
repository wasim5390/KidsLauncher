package com.wiser.kids.ui.reminder;

import android.content.Context;

import com.wiser.kids.BasePresenter;
import com.wiser.kids.BaseView;

import java.util.List;

public class ReminderContract {

    interface View extends BaseView<Presenter> {
        void showAlarmDialog(ReminderEntity entity);

        void setAlarm(List<ReminderEntity> list, Context context);

        void onLoadedReminderList(List<ReminderEntity> list);

        void showMessage(String msg);

        void setPendingIntent(List<ReminderEntity> list);

        void checkTime(List<ReminderEntity> list);
    }

    interface Presenter extends BasePresenter {
        void onLoadReminderList();
        void onReminderListchecked(List<ReminderEntity> list);
    }
}
