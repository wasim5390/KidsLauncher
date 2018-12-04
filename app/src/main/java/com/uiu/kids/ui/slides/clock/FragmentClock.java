package com.uiu.kids.ui.slides.clock;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;
import com.uiu.kids.BaseFragment;
import com.uiu.kids.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;

public class FragmentClock  extends BaseFragment {

    @BindView(R.id.tvHour)
    TextView tvHour;
    @BindView(R.id.tvMinutes)
    TextView tvMinutes;
    @BindView(R.id.tvAMPM)
    TextView tvAMPM;
    @BindView(R.id.tvSec)
    TextView tvSec;
    @BindView(R.id.tvdate)
    TextView tvDate;

    public static FragmentClock newInstance()
    {
        Bundle args=new Bundle();
        FragmentClock instance=new FragmentClock();
        instance.setArguments(args);
        return instance;
    }
    @Override
    public int getID() {
        return R.layout.fragment_clock;
    }

    @Override
    public void initUI(View view) {
        tvDate.setSelected(true);
        show();
    }

    private void show() {

        CountDownTimer newtimer = new CountDownTimer(1000000000, 1000) {

            public void onTick(long millisUntilFinished) {
                Calendar c = Calendar.getInstance();
                DateFormat dfHour = new SimpleDateFormat("hh");
                DateFormat dfMinutes = new SimpleDateFormat("mm");
                DateFormat dfAMPM = new SimpleDateFormat("a");
                tvHour.setText(dfHour.format(c.getTime()));
                tvMinutes.setText(dfMinutes.format(c.getTime()));
                tvAMPM.setText(dfAMPM.format(c.getTime()));
                String sec;

                sec= ""+c.get(Calendar.SECOND);
                if(c.get(Calendar.SECOND)<10)
                    sec = "0" + c.get(Calendar.SECOND);

                tvSec.setText(sec);

                SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
                Date d = new Date();
                String dayOfTheWeek = sdf.format(d);

                tvDate.setText(c.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())
                        + " " + c.get(Calendar.DAY_OF_MONTH) + " " + c.get(Calendar.YEAR)+"    "+dayOfTheWeek);


            }

            public void onFinish() {
                show();
            }
        };
        newtimer.start();
    }
}

