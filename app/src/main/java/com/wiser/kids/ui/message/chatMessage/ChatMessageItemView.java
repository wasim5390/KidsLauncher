package com.wiser.kids.ui.message.chatMessage;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.wiser.kids.Constant;
import com.wiser.kids.R;
import com.wiser.kids.ui.home.contact.ContactEntity;
import com.wiser.kids.ui.message.MessageAdapterList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jzvd.JZVideoPlayerStandard;
import de.hdodenhof.circleimageview.CircleImageView;
import nl.changer.audiowife.AudioWife;

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
    @BindView(R.id.audio_time_tv)
    TextView audio_time_tv;
    @BindView(R.id.media_seekbar)
    SeekBar mMediaSeekBar;


    Animation animScale;
    public Context mContext;

    private MessageAdapterList.Callback callback;
    private ChatMessageEntity slideItem;

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
        animScale = AnimationUtils.loadAnimation(getContext(), R.anim.anim_scale);

    }

    public void setSlideItem(Context mContext,ChatMessageEntity item, MessageAdapterList.Callback callback) {
        this.callback = callback;
        this.slideItem = item;
        this.mContext=mContext;



    }


    public void checkMessageType(int modetype)
    {
        switch (modetype)
        {
            case MEDIA_AUDIO:

                video_ll.setVisibility(View.GONE);

                fileLayout.setVisibility(View.VISIBLE);
                Audio_ll.setVisibility(View.VISIBLE);
                final AudioWife audioWife=AudioWife.getInstance();

                audioWife.init(mContext, Uri.parse(slideItem.getUrl()))
                        .setPlayView(playIV)
                        .setPauseView(pauseIV)
                        .setSeekBar(mMediaSeekBar)
                        .setRuntimeView(mRunTime)
                        .setTotalTimeView(audio_time_tv);


                audioWife.addOnPlayClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        playIV.setVisibility(View.GONE);
                        pauseIV.setVisibility(View.VISIBLE);
                    }
                });

                audioWife.addOnPauseClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        playIV.setVisibility(View.VISIBLE);
                        pauseIV.setVisibility(View.GONE);
                    }
                });

                audioWife.addOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        playIV.setVisibility(View.VISIBLE);
                        pauseIV.setVisibility(View.GONE);
                        audioWife.release();
                    }
                });


                break;
            case MEDIA_VIDEO:
                Audio_ll.setVisibility(View.GONE);

                fileLayout.setVisibility(View.VISIBLE);
                video_ll.setVisibility(View.VISIBLE);
                playVideo(video_view,slideItem.getUrl());
                break;
        }
    }

    private void playVideo(JZVideoPlayerStandard video_view, String url) {
        video_view.setUp(url, JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL, "");
    }


}