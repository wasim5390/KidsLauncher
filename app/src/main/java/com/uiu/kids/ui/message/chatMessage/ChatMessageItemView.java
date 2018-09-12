package com.uiu.kids.ui.message.chatMessage;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;


import com.uiu.kids.Constant;
import com.uiu.kids.R;
import com.uiu.kids.util.Util;
import com.uiu.kids.event.StopRunable;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jzvd.JZVideoPlayerStandard;

public class ChatMessageItemView extends ConstraintLayout implements Constant {

    @BindView(R.id.chatDateTextView)
    public TextView chatDateTextView;

    @BindView(R.id.fileLayout)
    public LinearLayout fileLayout;

    @BindView(R.id.showTimeTv)
    public TextView showTimeTv;


    @BindView(R.id.video_ll)
    LinearLayout video_ll;
    @BindView(R.id.video_view)
    public JZVideoPlayerStandard video_view;


    @BindView(R.id.Audio_ll)
    LinearLayout Audio_ll;
    @BindView(R.id.pauseIV)
    ImageView pauseIV;
    @BindView(R.id.playIV)
    ImageView playIV;
    @BindView(R.id.run_time)
    TextView mRunTime;
    @BindView(R.id.total_time)
    TextView total_time_tv;
    @BindView(R.id.media_seekbar)
    SeekBar mMediaSeekBar;


    Animation animScale;
    MediaPlayer mp;
    public Context mContext;
    public static int isLastPosition = -1;
    public int position;
    public boolean isPlaying = false;
    private ChatMessageAdapterList.Callback callback;
    private ChatMessageEntity slideItem;
    private static Handler mHandler = new Handler();

    public ChatMessageItemView(Context context) {
        super(context);
    }

    public ChatMessageItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ChatMessageItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this, this);
        EventBus.getDefault().register(this);
        animScale = AnimationUtils.loadAnimation(getContext(), R.anim.anim_scale);

    }

    public void setSlideItem(Context mContext, ChatMessageEntity item, ChatMessageAdapterList.Callback callback, MediaPlayer mp, int position) {
        this.callback = callback;
        this.slideItem = item;
        this.mContext = mContext;
        this.mp = mp;
        this.position = position;
        chatDateTextView.setText(slideItem.getTime());
        checkMessageType(slideItem.getMsgMode());
        if (!slideItem.isAudioPlaying()) {
            isMediaReset();
        } else {
            playIV.setVisibility(View.GONE);
            pauseIV.setVisibility(View.VISIBLE);
        }
    }

    public void isMediaReset() {
        playIV.setVisibility(View.VISIBLE);
        pauseIV.setVisibility(View.GONE);
        mMediaSeekBar.setProgress(0);
        mMediaSeekBar.setMax(100);
        mHandler.removeCallbacks(mUpdateTimeTask);

    }


    public void checkMessageType(int modetype) {
        switch (modetype) {
            case MEDIA_AUDIO:

                video_ll.setVisibility(View.GONE);

                fileLayout.setVisibility(View.VISIBLE);
                Audio_ll.setVisibility(View.VISIBLE);

                mMediaSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                        if(seekBar.getProgress()==100)
                        {
                            isMediaReset();
                        }

                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                        mHandler.removeCallbacks(mUpdateTimeTask);
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                        mHandler.removeCallbacks(mUpdateTimeTask);
                        int totalDuration = mp.getDuration();
                        int currentPosition = Util.progressToTimer(seekBar.getProgress(), totalDuration);
                        mp.seekTo(currentPosition);
                        updateProgressBar();
                    }
                });


                break;
            case MEDIA_VIDEO:
                Audio_ll.setVisibility(View.GONE);
                fileLayout.setVisibility(View.VISIBLE);
                video_ll.setVisibility(View.VISIBLE);
                playVideo(video_view, slideItem.getUrl());
                break;
        }
    }

    private void playVideo(JZVideoPlayerStandard video_view, String url) {
        video_view.setUp(url, JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL, "");
    }



    @OnClick(R.id.playIV)
    public void setPlayIV() {
        if (!mp.isPlaying()) {
            mp.reset();
            slideItem.setAudioPlaying(true);
            try {
                mp.setDataSource("http://www.hamariwebsite.com/quran/ar-ur/01-(hamariweb.com).mp3");
                mp.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mp.start();
            playIV.setVisibility(View.GONE);
            pauseIV.setVisibility(View.VISIBLE);
            isPlaying = true;
        } else {
            slideItem.setAudioPlaying(true);
            //mp.stop();
            mp.reset();

//            mp.release();
            try {
                mp.setDataSource("http://www.hamariwebsite.com/quran/ar-ur/109-(hamariweb.com).mp3");
                mp.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mp.start();
            playIV.setVisibility(View.GONE);
            pauseIV.setVisibility(View.VISIBLE);
            isPlaying = true;
            if (isLastPosition != -1) {
                callback.onSlideItemClick(isLastPosition);
            }

        }
        isLastPosition = position;
        updateProgressBar();

    }

    @OnClick(R.id.pauseIV)
    public void setPauseIV() {
        if (mp.isPlaying()) {
            mp.stop();
            mp.reset();
//            mp.release();
            isPlaying = false;
        }
        playIV.setVisibility(View.VISIBLE);
        pauseIV.setVisibility(View.GONE);
        isMediaReset();
        mHandler.removeCallbacks(mUpdateTimeTask);

    }

    private void updateProgressBar() {
        mHandler.postDelayed(mUpdateTimeTask, 100);
    }

    private final Runnable mUpdateTimeTask = new Runnable() {
        public void run() {

            if (mp != null) {
                if (mp.getDuration() <= 0)
                    return;

                long totalDuration = mp.getDuration();
                long currentDuration = mp.getCurrentPosition();

                // Displaying Total Duration time
                total_time_tv.setText(String.valueOf(Util.milliSecondsToTimer(totalDuration)));
                // Displaying time completed playing
                mRunTime.setText(String.valueOf(Util.milliSecondsToTimer(currentDuration)));

                // Updating progress bar
                int progress = Util.getProgressPercentage(currentDuration, totalDuration);
                //Log.d("Progress", ""+progress);
                mMediaSeekBar.setProgress(progress);

                // Running this thread after 100 milliseconds
                mHandler.postDelayed(this, 100);
            }
        }
    };

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(StopRunable Event) {
        if (Event.isIsrunning() == true) {
         stopRunable();
        }
    }

    private void stopRunable() {
        mHandler.removeCallbacks(mUpdateTimeTask);
        isMediaReset();
        EventBus.getDefault().unregister(this);

    }
//"http://www.html5videoplayer.net/videos/toystory.mp4"
}