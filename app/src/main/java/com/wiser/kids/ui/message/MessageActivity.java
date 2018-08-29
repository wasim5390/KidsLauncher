package com.wiser.kids.ui.message;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.wiser.kids.BaseActivity;
import com.wiser.kids.Injection;
import com.wiser.kids.R;
import com.wiser.kids.model.SlideItem;
import com.wiser.kids.ui.home.apps.AppsFragment;
import com.wiser.kids.ui.home.apps.AppsPresenter;
import com.wiser.kids.ui.message.MessageAudioRecord.MessageAudioRecordFragment;
import com.wiser.kids.ui.message.MessageAudioRecord.MessageAudioRecordPresenter;
import com.wiser.kids.ui.message.MessageVideoRecording.MessageVideoRecordingFragment;
import com.wiser.kids.ui.message.MessageVideoRecording.MessageVideoRecordingPresenter;
import com.wiser.kids.util.PreferenceUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MessageActivity extends BaseActivity {

    @BindView(R.id.message_video)
    ImageView btnVideo;

    @BindView(R.id.message_audio)
    ImageView btnAudio;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.header_btn_right)
    Button btnRight;

    public MessageAudioRecordFragment messageAudioRecordFragment;
    public MessageAudioRecordPresenter messageAudioRecordPresenter;

    public MessageVideoRecordingFragment messageVideoRecordingFragment;
    public MessageVideoRecordingPresenter messageVideoRecordingPresenter;

    public MessageFragment messageFragment;
    public MessagePresenter messagePresenter;


    @Override
    public int getID() {
        return R.layout.activity_message2;
    }

    @Override
    public void created(Bundle savedInstanceState) {

        ButterKnife.bind(this);
        setToolBar(toolbar, "", true);
        btnRight.setVisibility(View.GONE);

    }


    @OnClick(R.id.message_video)
    public void videoClick()
    {
        loadVideoFragment();

    }

    @OnClick(R.id.message_audio)
    public void audioClick()
    {

        btnAudio.setVisibility(View.GONE);
        btnVideo.setVisibility(View.GONE);
        toolbar.setVisibility(View.GONE);
        loadAudioFragment();

    }

    @OnClick(R.id.header_btn_left)
    public void onBackClick() {
        onBackPressed();
    }


    public void loadAudioFragment()
    {
        messageAudioRecordFragment = messageAudioRecordFragment != null ? messageAudioRecordFragment : MessageAudioRecordFragment.newInstance();
        messageAudioRecordPresenter = messageAudioRecordPresenter != null ? messageAudioRecordPresenter : new MessageAudioRecordPresenter(messageAudioRecordFragment,new SlideItem(),PreferenceUtil.getInstance(this), Injection.provideRepository(this));
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.msgframeLayout, messageAudioRecordFragment);
        fragmentTransaction.commit();
    }

    public void loadVideoFragment()
    {
        messageVideoRecordingFragment = messageVideoRecordingFragment != null ? messageVideoRecordingFragment : MessageVideoRecordingFragment.newInstance();
        messageVideoRecordingPresenter = messageVideoRecordingPresenter != null ? messageVideoRecordingPresenter : new MessageVideoRecordingPresenter(messageVideoRecordingFragment,PreferenceUtil.getInstance(this), Injection.provideRepository(this));
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.msgframeLayout, messageVideoRecordingFragment);
        fragmentTransaction.commit();

    }

}
