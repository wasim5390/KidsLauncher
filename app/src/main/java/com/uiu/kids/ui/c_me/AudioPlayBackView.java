package com.uiu.kids.ui.c_me;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.uiu.kids.R;
import com.uiu.kids.util.Util;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;

import static android.content.Context.AUDIO_SERVICE;

public class AudioPlayBackView extends ConstraintLayout  implements SeekBar.OnSeekBarChangeListener{

    @BindView(R.id.media_seekbar)
    SeekBar seekBar;

    @BindView(R.id.run_time)
    TextView elapsedTime;

    @BindView(R.id.total_time)
    TextView endTime;

    @BindView(R.id.playIV)
    CheckBox togglePlay;

    MediaPlayer mediaPlayer = new MediaPlayer();
    final AudioManager audioManager = (AudioManager) getContext().getSystemService(AUDIO_SERVICE);
    final int originalVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
    boolean inLoop = false;

    private Handler mHandler = new Handler();

    public AudioPlayBackView(Context context) {
        super(context);
    }

    public AudioPlayBackView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AudioPlayBackView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this,this);

        seekBar.setOnSeekBarChangeListener(this);
        mediaPlayer.setOnCompletionListener(mp -> {
            togglePlay.setChecked(false);
            elapsedTime.setText(Util.milliSecondsToTimer(0));
            seekBar.setProgress(0);

        });

        mediaPlayer.setOnPreparedListener(mp -> {
            endTime.setText(Util.milliSecondsToTimer(mp.getDuration()));
            elapsedTime.setText(Util.milliSecondsToTimer(mp.getCurrentPosition()));
        });

    }

    public void stopMediaPlayer(){
        if(mediaPlayer!=null)
        {
            mHandler.removeCallbacks(updateTimeTask);
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, originalVolume, 0);
            updateTimeTask = null;
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer=null;
        }
    }



    public void setDataSource(Uri uri,boolean isLoop){
        try {
            this.inLoop = isLoop;
            mediaPlayer.reset();
            mediaPlayer.setDataSource(getContext(), uri);
            mediaPlayer.prepare();

            togglePlay.setChecked(true);
            mediaPlayer.setLooping(inLoop);



            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC),
                    0);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    @OnCheckedChanged(R.id.playIV)
    public void onPlayToggle(CompoundButton button, boolean checked){
        if(checked)
            playAudio();
        else
            pauseAudio();

    }

    private void playAudio() {

        mediaPlayer.start();
        // Updating progress bar
        updateProgressBar();
    }

    private void pauseAudio(){
        mediaPlayer.pause();
        mHandler.removeCallbacks(updateTimeTask);

    }


    private void updateProgressBar() {
        mHandler.postDelayed(updateTimeTask, 100);
    }

    private Runnable updateTimeTask = new Runnable() {
        public void run() {
            seekBar.setProgress(mediaPlayer.getCurrentPosition());
            seekBar.setMax(mediaPlayer.getDuration());
            elapsedTime.setText(Util.milliSecondsToTimer(mediaPlayer.getCurrentPosition()));
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
        mediaPlayer.seekTo(seekbar.getProgress());
        updateProgressBar();
    }


}
