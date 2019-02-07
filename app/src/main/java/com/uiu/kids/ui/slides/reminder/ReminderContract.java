package com.uiu.kids.ui.slides.reminder;

import android.content.Context;

import com.uiu.kids.BasePresenter;
import com.uiu.kids.BaseView;

import java.util.List;

public class ReminderContract {

    interface View extends BaseView<Presenter> {


        void setAlarm(List<ReminderEntity> list, Context context);

        void onLoadedReminderList(List<ReminderEntity> list);

        void showMessage(String msg);

        void setPendingIntent(List<ReminderEntity> list);

        void checkTime(List<ReminderEntity> list);

        void reLoadReminderList();

        void slideSerial(int serial, int count);
    }

    interface Presenter extends BasePresenter {
        void onLoadReminderList();

        void onReminderListchecked(List<ReminderEntity> list);

        void reLoadedReminderList();
    }
}
