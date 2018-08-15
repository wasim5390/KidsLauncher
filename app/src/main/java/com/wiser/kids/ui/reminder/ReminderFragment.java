package com.wiser.kids.ui.reminder;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.speech.SpeechRecognizer;

import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import android.view.View;

import android.widget.Button;

import android.widget.FrameLayout;
import android.widget.ImageView;

import android.widget.RelativeLayout;

import android.widget.TextView;
import android.widget.Toast;


import com.wiser.kids.BaseFragment;
import com.wiser.kids.Constant;
import com.wiser.kids.R;
import com.wiser.kids.event.NotificationReceiveEvent;
import com.wiser.kids.event.ReminderRecieveEvent;
import com.wiser.kids.model.LinksEntity;
import com.wiser.kids.ui.favorite.links.FavoriteLinksAdapter;
import com.wiser.kids.ui.home.contact.ContactEntity;
import com.wiser.kids.util.Util;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.internal.Utils;


public class ReminderFragment extends BaseFragment implements ReminderContract.View, ReminderAdapterList.Callback {


    public ReminderContract.Presenter presenter;
    public ReminderAdapterList adapter;
    public RecyclerView recyclerView;

    private static final String PARAM_TITLE = "title";
    private static final String PARAM_DATE = "date";
    private static final String PARAM_TIME = "time";
    private static final String PARAM_REPEAT = "repeat";
    private static final String PARAM_NOTE = "note";
    private String TAG = "ReminderSlideFragment";
    String dateString, timeString;
    private RelativeLayout rlCalendar, rlTime;
    private ImageView ivMic;
    private Button btnCancel;
    public Dialog dialog;
    private String saveMode = "Creat";
    private TextView etTitle, etNotes;
    private TextView tvTitle, tvDate, tvTime;
    Calendar myCalendar = Calendar.getInstance();
    private String reminderId;
    private int ID;
    public MediaPlayer mp;
    private static final int REQUEST_RECORD_AUDIO_PERMISSION_CODE = 1;
    private SpeechRecognizer speechRecognizer;
    private FrameLayout flSpeech;


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
        mp = new MediaPlayer();
        init(view);
        setAdapter();
        presenter.start();

    }

    private void init(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.rvReminder);
    }

    private void setAdapter() {
        adapter = new ReminderAdapterList(getContext(), new ArrayList<>(), this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

    }


    @Override
    public void onLoadedReminderList(List<ReminderEntity> list) {
        setPendingIntent(list);
        adapter.setSlideItems(list);
    }

    @Override
    public void showMessage(String msg) {

        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setPendingIntent(List<ReminderEntity> list) {

        setAlarm(list, getContext());
    }

    @Override
    public void checkTime(List<ReminderEntity> list) {

        for (int i = 0; i < list.size(); i++) {
//            int j = 50 + i;
//            list.get(i).setTime("16:" + j + ":00");

            String dateTime = list.get(i).getDate() + " " + list.get(i).getTime();
            Log.e("timeDate", dateTime);
            list.get(i).setdate(Util.convertStringDate(dateTime));
            list.get(i).setIsActiveReminder(true);
            Log.e("mili", String.valueOf(list.get(i).getdate().getTime()));
        }
        Date currenttime = Calendar.getInstance().getTime();
        for (int i = 0; i < list.size(); i++) {

            if (!list.get(i).getIs_repeated()) {
                if (currenttime.after(list.get(i).getdate())) {
                    list.get(i).setIsActiveReminder(false);
                }
            }

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

        new Handler().postDelayed(() -> {


            showAlarmDialog(slideItem);


        }, 1);
    }

    @Override
    public void showAlarmDialog(ReminderEntity entity) {

        Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth);
        dialog.setContentView(R.layout.dialog_creat_alarm);
        rlCalendar = (RelativeLayout) dialog.findViewById(R.id.rlDate);
        rlTime = (RelativeLayout) dialog.findViewById(R.id.rlTime);
        tvTitle = (TextView) dialog.findViewById(R.id.textView);
        tvDate = (TextView) dialog.findViewById(R.id.tvDate);
        ivMic = (ImageView) dialog.findViewById(R.id.ivMic);
        tvTime = (TextView) dialog.findViewById(R.id.tvTime);
        etTitle = (TextView) dialog.findViewById(R.id.etTitle);
        etNotes = (TextView) dialog.findViewById(R.id.etNotes);
        btnCancel = (Button) dialog.findViewById(R.id.btnCancel);




        tvDate.setText(entity.getDate());
        tvTime.setText(entity.getTime());
        etTitle.setText(entity.getTitle());
        etNotes.setText(entity.getNote());

        dialog.show();

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
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
                bundle.putString("note", entities.get(i).getNote());
                intent.putExtras(bundle);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context
                        , i, intent, 0);
                alarmManager[i] = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

                if(entities.get(i).getIs_repeated())
                {
                    alarmManager[i].setRepeating(AlarmManager.RTC_WAKEUP, entities.get(i).getdate().getTime(),24*60*60*1000, pendingIntent);

                   
                }
                else {
                    alarmManager[i].set(AlarmManager.RTC_WAKEUP, entities.get(i).getdate().getTime(),
                            pendingIntent);
                }
                Log.e("time" + i, String.valueOf(entities.get(i).getdate().getTime()));
                pendingIntentList.add(pendingIntent);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ReminderRecieveEvent receiveEvent) {
        if (receiveEvent.getType() == Constant.SLIDE_INDEX_REMINDERS) {
            int index = receiveEvent.getIndex();
            String title = receiveEvent.getTitle();
            String note = receiveEvent.getNote();
            Log.e("index", String.valueOf(index));
            setalarmAlert(title,note);
        }

    }

    @Override
    public void setalarmAlert(String title, String note) {

        Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        mp = new MediaPlayer();
        try {
            mp.setDataSource(getContext(), alert);
            final AudioManager audioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);

            if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
                mp.setAudioStreamType(AudioManager.STREAM_ALARM);
                mp.prepare();
                mp.setLooping(true);
                mp.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
///////////////set alart/////////

        Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth);
        dialog.setContentView(R.layout.alarm_alert_dialog);
        TextView tvTitle,tvNote;
        Button cancel;
        tvNote=(TextView) dialog.findViewById(R.id.alertNote);
        tvTitle=(TextView) dialog.findViewById(R.id.alartTitle);
        cancel=(Button) dialog.findViewById(R.id.alartCancel);

        tvTitle.setText(title.toString());
        tvNote.setText(note.toString());

        dialog.show();

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.stop();
                reLoadReminderList();
                dialog.dismiss();
            }
        });


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


}
