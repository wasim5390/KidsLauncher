package com.uiu.kids.ui.c_me;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.uiu.kids.BaseActivity;
import com.uiu.kids.R;
import com.uiu.kids.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class VideoPlaybackActivity extends BaseActivity implements SeekBar.OnSeekBarChangeListener{


    @BindView(R.id.seekBar2)
    SeekBar seekBar;

    @BindView(R.id.elapsedTime)
    TextView elapsedTime;

    @BindView(R.id.endTime)
    TextView endTime;

    @BindView(R.id.play_toggle)
    CheckBox togglePlay;

    @BindView(R.id.videoView)
    VideoView videoView;

    private Handler mHandler = new Handler();

    @Override
    public int getID() {
        return R.layout.notification_view_player;
    }

    @Override
    public void created(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        if(!getIntent().hasExtra("uri"))
            finish();

        Uri uri = Uri.parse(getIntent().getStringExtra("uri"));
        videoView.setVideoURI(uri);
        videoView.setOnPreparedListener(mp -> {
            endTime.setText(Util.milliSecondsToTimer(mp.getDuration()));
            elapsedTime.setText(Util.milliSecondsToTimer(mp.getCurrentPosition()));
        });

        seekBar.setOnSeekBarChangeListener(this);
        videoView.setOnCompletionListener(mp -> {
            togglePlay.setChecked(false);
            elapsedTime.setText(Util.milliSecondsToTimer(0));
            seekBar.setProgress(0);
        });
        togglePlay.setChecked(true);
    }


    @OnCheckedChanged(R.id.play_toggle)
    public void onPlayToggle(CompoundButton button, boolean checked){
        if(checked)
            playVideo();
        else
            pauseVideo();

    }

    private void playVideo() {
        videoView.start();
        // Updating progress bar
        updateProgressBar();
    }

    private void pauseVideo(){
        videoView.pause();
        mHandler.removeCallbacks(updateTimeTask);

    }

    private void updateProgressBar() {
        mHandler.postDelayed(updateTimeTask, 100);
    }

    private Runnable updateTimeTask = new Runnable() {
        public void run() {
            seekBar.setProgress(videoView.getCurrentPosition());
            seekBar.setMax(videoView.getDuration());
            elapsedTime.setText(Util.milliSecondsToTimer(videoView.getCurrentPosition()));
            mHandler.postDelayed(this, 100);
        }
    };

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekbar) {
        mHandler.removeCallbacks(updateTimeTask);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekbar) {
        mHandler.removeCallbacks(updateTimeTask);
        videoView.seekTo(seekbar.getProgress());
        updateProgressBar();
    }

    @OnClick(R.id.back)
    public void onBack(){
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(updateTimeTask);
        videoView.stopPlayback();
    }
}
