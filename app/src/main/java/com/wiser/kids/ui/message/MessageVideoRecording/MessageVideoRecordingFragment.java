package com.wiser.kids.ui.message.MessageVideoRecording;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wiser.kids.BaseFragment;
import com.wiser.kids.R;

public class MessageVideoRecordingFragment extends BaseFragment {





    public static MessageVideoRecordingFragment newInstance()
    {
        Bundle args=new Bundle();
        MessageVideoRecordingFragment instance=new MessageVideoRecordingFragment();
        instance.setArguments(args);
        return instance;
    }

    @Override
    public int getID() {
        return R.layout.fragment_message_video_recording;
    }

    @Override
    public void initUI(View view) {

    }
}
