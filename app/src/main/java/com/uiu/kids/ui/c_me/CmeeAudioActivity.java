package com.uiu.kids.ui.c_me;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.uiu.kids.BaseActivity;
import com.uiu.kids.R;
import com.uiu.kids.ui.Chronometer;
import com.uiu.kids.ui.message.MessageActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;

public class CmeeAudioActivity extends BaseActivity {

    public static final String TAG = CmeeAudioActivity.class.getSimpleName();

    @BindView(R.id.toggleAudio)
    ToggleButton btnAudio;

    @BindView(R.id.chronometer)
    Chronometer timer;

    @Override
    public int getID() {
        return R.layout.activity_cmee_audio;
    }

    @Override
    public void created(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        timer.setBase(SystemClock.elapsedRealtime());
    }

    @OnCheckedChanged(R.id.toggleAudio)
    public void onToggleClick(CompoundButton button, boolean checked){
        if(checked) {
            timer.setBase(SystemClock.elapsedRealtime());
            timer.start();
        }
        else {
            timer.stop();
            startActivity(new Intent(this, MessageActivity.class));
        }
    }
}
