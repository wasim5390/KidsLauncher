package com.uiu.kids.ui.message.MessageAudioRecord;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.uiu.kids.BaseFragment;
import com.uiu.kids.R;
import com.uiu.kids.ui.Chronometer;
import com.uiu.kids.ui.message.MessageFragment;
import com.uiu.kids.ui.message.MessagePresenter;
import com.uiu.kids.util.Util;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MessageAudioRecordFragment extends BaseFragment implements MessageAudioRecordContract.View,SeekBar.OnSeekBarChangeListener {

    @BindView(R.id.recording)
    public ImageView recording;

    private MessageAudioRecordContract.Presenter presenter;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.timer)
    Chronometer timer;
    @BindView(R.id.seekBar)
    SeekBar seekBar;
    @BindView(R.id.btnPlay)
    ImageButton btnPlay;
    @BindView(R.id.totalDurationLabel)
    TextView totalDurationLabel;
    @BindView(R.id.currentDurationLabel)
    TextView currentDurationLabel;
    @BindView(R.id.playerView)
    View bottomSheet;
    private boolean isRecording=false;
    private boolean isPlaying=false;
    private boolean isPaused=false;
    private final MediaPlayer mp = new MediaPlayer();
    private final Handler mHandler = new Handler();
    private MessageFragment messageFragment;
    private MessagePresenter messagePresenter;

    private final Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            if(mp.getDuration()<=0)
                return;

            long totalDuration = mp.getDuration();
            long currentDuration = mp.getCurrentPosition();

            // Displaying Total Duration time
            totalDurationLabel.setText(String.valueOf(Util.milliSecondsToTimer(totalDuration)));
            // Displaying time completed playing
            currentDurationLabel.setText(String.valueOf(Util.milliSecondsToTimer(currentDuration)));

            // Updating progress bar
            int progress = Util.getProgressPercentage(currentDuration, totalDuration);
            //Log.d("Progress", ""+progress);
            seekBar.setProgress(progress);

            // Running this thread after 100 milliseconds
            mHandler.postDelayed(this, 100);
        }
    };


    public static MessageAudioRecordFragment newInstance() {
        Bundle args = new Bundle();
        MessageAudioRecordFragment instance = new MessageAudioRecordFragment();
        instance.setArguments(args);
        return instance;

    }

    @Override
    public int getID() {
        return R.layout.fragment_message_audio_record;
    }

    @Override
    public void initUI(View view) {

        ButterKnife.bind(getView());
        presenter.start();
        timer.setBase(SystemClock.elapsedRealtime());
        seekBar.setOnSeekBarChangeListener(this);
        // set Progress bar values
        seekBar.setProgress(0);
        seekBar.setMax(100);

    }

    @Override
    public void setPresenter(MessageAudioRecordContract.Presenter presenter) {

        this.presenter = presenter;
    }

    @Override
    public void showNoInternet() {

    }

    @OnClick(R.id.recording)
    public void onRecording() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    10);
        } else {
   if(!isRecording) {
                timer.setBase(SystemClock.elapsedRealtime());
                presenter.startRecording();
            }
            else
                presenter.stopRecording();

        }
    }

    @OnClick(R.id.btnPlay)
    public void onPlayerClick(){
        if(!presenter.isMediaAvailable()) {
            showMessage("Please Record Audio to share!");
            return;
        }
        if(isPlaying) {
            presenter.pauseMedia(mp);
        }
        else {
            if(isPaused)
                presenter.resumeMedia(mp);
            else {
                presenter.playRecording(mp);
            }
            isPlaying=true;
        }
    }

    @OnClick(R.id.btnShare)
    public void onShareMediaClick(){
        presenter.shareMedia();
    }

    @Override
    public void onRecordingStarted(boolean isStarted) {
        isRecording = isStarted;
        isPlaying=isPaused=false;

        if(isRecording) {
            timer.start();
            mHandler.removeCallbacks(mUpdateTimeTask);
        }
        else {
            timer.stop();
            updateProgressBar();
        }
        progressBar.setVisibility(isStarted?View.VISIBLE:View.GONE);
        timer.setVisibility(isRecording?View.VISIBLE:View.GONE);
        recording.setImageResource(!isStarted?R.mipmap.ic_recorder:R.mipmap.ic_stop);
        bottomSheet.setVisibility(!isRecording?View.VISIBLE:View.GONE);
        btnPlay.setEnabled(presenter.isMediaAvailable());
        seekBar.setEnabled(presenter.isMediaAvailable());

    }

    @Override
    public void onMediaPlayStarted() {
        isPlaying=true;
        isPaused = false;
        btnPlay.setImageResource(R.mipmap.ic_pause);
        updateProgressBar();
    }


    @Override
    public void onMediaPaused() {
        isPlaying=false;
        isPaused = true;
        btnPlay.setImageResource(R.mipmap.ic_play);
    }

    @Override
    public void onMediaFinished() {
        mHandler.removeCallbacks(mUpdateTimeTask);
        isPlaying=false;
        currentDurationLabel.setText(String.valueOf(Util.milliSecondsToTimer(0)));
        seekBar.setProgress(0);
        btnPlay.setImageResource(R.mipmap.ic_play);
        mp.reset();
    }



    private void goToMessageFragment(String filePath) {
//        messageFragment = messageFragment != null ? messageFragment : MessageFragment.newInstance(MEDIA_AUDIO,filePath);
//        messagePresenter = messagePresenter != null ? messagePresenter : new MessagePresenter(messageFragment, PreferenceUtil.getInstance(getActivity()), Injection.provideRepository(getActivity()));
//        FragmentManager fragmentManager = getChildFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.msgframeLayout, messageFragment,null);
//        fragmentTransaction.commit();
    }

    @Override
    public void showMessage(String msg) {
        Toast.makeText(getContext(),msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMediaFileShare(String filePath) {

        messagePresenter=null;
        messagePresenter=null;
        goToMessageFragment(filePath);

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
// remove message Handler from updating progress bar
        mHandler.removeCallbacks(mUpdateTimeTask);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mHandler.removeCallbacks(mUpdateTimeTask);
        int totalDuration = mp.getDuration();
        int currentPosition = Util.progressToTimer(seekBar.getProgress(), totalDuration);

        // forward or backward to certain seconds
        mp.seekTo(currentPosition);

        // update timer progress again
        updateProgressBar();
    }

    private void updateProgressBar() {
        mHandler.postDelayed(mUpdateTimeTask, 100);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(mUpdateTimeTask);
        mp.release();
    }
}
