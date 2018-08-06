package com.wiser.kids.ui.reminder;

import android.app.AlarmManager;
import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.speech.SpeechRecognizer;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.wiser.kids.BaseFragment;
import com.wiser.kids.R;
import com.wiser.kids.model.LinksEntity;
import com.wiser.kids.ui.favorite.links.FavoriteLinksAdapter;
import com.wiser.kids.util.Util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
    private Button btnCancel, btnSave;
    private Switch swRepeat;
    public Dialog dialog;
    private String saveMode="Creat";
    private EditText etTitle, etNotes;
    private TextView tvTitle, tvDate, tvTime;
    Calendar myCalendar = Calendar.getInstance();
    private AlarmManager alarmManager;
    private String reminderId;
    private int ID;
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

        init(view);
        setAdapter();
        presenter.start();

    }

    private void init(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.rvReminder);
    }

    private void setAdapter() {
        adapter = new ReminderAdapterList(getContext(), new ArrayList<>(), this);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

    }


    @Override
    public void onLoadedReminderList(List<ReminderEntity> list) {
        adapter.setSlideItems(list);
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

            if (slideItem.getName() == null) {
                showAlarmDialog(null, null, 0, false);

            } else {


            }
        }, 1);

    }


    private void showAlarmDialog(String title, String notes, long milli, boolean repeat) {

        Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth);
        dialog.setContentView(R.layout.dialog_creat_alarm);
        rlCalendar = (RelativeLayout) dialog.findViewById(R.id.rlDate);
        rlTime = (RelativeLayout) dialog.findViewById(R.id.rlTime);
        tvTitle = (TextView) dialog.findViewById(R.id.textView);
        tvTitle.setText(saveMode + " Your Reminder");
        tvDate = (TextView) dialog.findViewById(R.id.tvDate);
        ivMic = (ImageView) dialog.findViewById(R.id.ivMic);
        tvTime = (TextView) dialog.findViewById(R.id.tvTime);
        etTitle = (EditText) dialog.findViewById(R.id.etTitle);
        etNotes = (EditText) dialog.findViewById(R.id.etNotes);
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
        btnSave = (Button) dialog.findViewById(R.id.btnSave);
        swRepeat = (Switch) dialog.findViewById(R.id.swRepeat);
       // flSpeech = (FrameLayout) dialog.findViewById(R.id.flSpeech);
//        rpvSpeech = (RecognitionProgressView) dialog.findViewById(R.id.rpvSpeech);
        ((LinearLayout) dialog.findViewById(R.id.llTop)).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
               Util.hidekeyPad(getActivity(),(LinearLayout) dialog.findViewById(R.id.llTop));
                return false;
            }
        });
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(getActivity());
        setSpeechListener();

        long mDate = System.currentTimeMillis();
        SimpleDateFormat sdfd = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat sdft = new SimpleDateFormat("hh:mm aa");

        if (milli == 0) {
            dateString = sdfd.format(mDate);
            timeString = sdft.format(mDate);
            myCalendar.setTimeInMillis(mDate);
        } else {
            dateString = sdfd.format(milli);
            timeString = sdft.format(milli);
            myCalendar.setTimeInMillis(milli);
        }

        tvDate.setText(dateString);
        String modifiedTime = timeString.replace("PM", "p.m.")
                .replace("AM", "a.m.");// Time formatter issuing on some devices
        tvTime.setText(modifiedTime);
        etTitle.setText(title);
        etNotes.setText(notes);
        swRepeat.setChecked(repeat);
        setLisenter();
        dialog.show();
    }

    private void setLisenter() {

    }

    private void setSpeechListener() {

    }

}
