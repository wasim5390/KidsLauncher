package com.uiu.kids.ui.slides.reminder;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.uiu.kids.BaseFragment;
import com.uiu.kids.Constant;
import com.uiu.kids.R;
import com.uiu.kids.event.ReminderRecieveEvent;
import com.uiu.kids.event.notification.NotificationReceiveEvent;
import com.uiu.kids.util.Util;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ReminderFragment extends BaseFragment implements ReminderContract.View, ReminderAdapterList.Callback {


    public ReminderContract.Presenter presenter;
    public ReminderAdapterList adapter;
    @BindView(R.id.rvReminder)
    public RecyclerView recyclerView;
    @BindView(R.id.reminderText)
    public TextView tvReminder;

    private String TAG = "ReminderSlideFragment";




    public static ReminderFragment newInstance() {
        Bundle args = new Bundle();
        ReminderFragment instance = new ReminderFragment();
        instance.setArguments(args);
        return instance;

    }

    @Override
    public int getID() {
        return R.layout.fragment_reminder;
    }

    @Override
    public void initUI(View view) {

        EventBus.getDefault().register(this);
        setAdapter();
        if(presenter!=null)
            presenter.start();

    }
    @Override
    public void setUserVisibleHint(boolean isFragmentVisible_) {
        super.setUserVisibleHint(true);
        if (this.isVisible()) {
// we check that the fragment is becoming visible
            if (isFragmentVisible_ ) {
                // progressBar.show();
                presenter.start();
            }
        }
    }


    private void setAdapter() {
        adapter = new ReminderAdapterList(getContext(), new ArrayList<>(), this);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

    }


    @Override
    public void onLoadedReminderList(List<ReminderEntity> list) {
        if (!list.isEmpty()) {
            tvReminder.setVisibility(View.GONE);
        } else {
            tvReminder.setVisibility(View.VISIBLE);
        }
        setPendingIntent(list);
        if(list.size()<3)
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        else
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        adapter.setSlideItems(list);
    }

    @Override
    public void showMessage(String msg) {

        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }
    @Override
    public void slideSerial(int serial, int count) {
        serial++;
        String pageNum = serial+"/"+count;
        ((TextView)getView().findViewById(R.id.tvreminderTitle)).setText(getString(R.string.reminder)+" ("+pageNum+")");
    }
    @Override
    public void setPendingIntent(List<ReminderEntity> list) {

        setAlarm(list, getContext());
    }

    @Override
    public void checkTime(List<ReminderEntity> list) {

        Calendar calendar = Calendar.getInstance();
        Date currentTime = Calendar.getInstance().getTime();
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setIsActiveReminder(true);
            if (!list.get(i).getIs_repeated()) {
                if (currentTime.after(list.get(i).getdate())) {
                    list.get(i).setIsActiveReminder(false);
                }
            }

            calendar.setTimeInMillis(Long.valueOf(list.get(i).getTime()));
            Log.e("timeDate", calendar.getTime().toString());
        }

        presenter.onReminderListchecked(list);

    }


    @Override
    public void setPresenter(ReminderContract.Presenter presenter) {

        this.presenter = presenter;
    }

    @Override
    public void showNoInternet() {

    }

    @Override
    public void onSlideItemClick(ReminderEntity slideItem) {

        EventBus.getDefault().postSticky(new ReminderRecieveEvent(0, Constant.SLIDE_INDEX_REMINDERS,slideItem.getTitle(),slideItem.getReminderNoteLink()));

    }


    @Override
    public void setAlarm(List<ReminderEntity> entities, Context context) {
        AlarmManager[] alarmManager = new AlarmManager[entities.size()];
        List<PendingIntent> pendingIntentList = new ArrayList<>();
        for (int i = 0; i < entities.size(); i++) {
            if (entities.get(i).isActiveReminder()) {
                Intent intent = new Intent("alarm_action");
                Bundle bundle = new Bundle();
                bundle.putInt("index", i);
                bundle.putString("title", entities.get(i).getTitle());
                bundle.putString("note", entities.get(i).getReminderNoteLink());
                intent.putExtras(bundle);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context
                        , i, intent, 0);
                alarmManager[i] = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

                if (entities.get(i).getIs_repeated()) {

                    alarmManager[i].setInexactRepeating(AlarmManager.RTC_WAKEUP, entities.get(i).getdate().getTime(), 24 * 60 * 60 * 1000, pendingIntent);

                } else {
                    alarmManager[i].set(AlarmManager.RTC_WAKEUP, entities.get(i).getdate().getTime(),
                            pendingIntent);
                }

                Log.e("time" + i, String.valueOf(entities.get(i).getdate().getTime()));
                pendingIntentList.add(pendingIntent);
            }
        }
    }



    @Override
    public void reLoadReminderList() {
        presenter.reLoadedReminderList();
    }


    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Unregister");
        EventBus.getDefault().unregister(this);
    }



    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(NotificationReceiveEvent receiveEvent) {
        if (receiveEvent.getNotificationForSlideType() == Constant.SLIDE_INDEX_REMINDERS
                && receiveEvent.isSlideUpdate()
        ) {
            //  JSONObject jsonObject = receiveEvent.getNotificationResponse();
            //  ReminderEntity entity = new Gson().fromJson(jsonObject.toString(), ReminderEntity.class);
            //  presenter.reLoadedReminderList();
            presenter.start();

        }
    }



}
