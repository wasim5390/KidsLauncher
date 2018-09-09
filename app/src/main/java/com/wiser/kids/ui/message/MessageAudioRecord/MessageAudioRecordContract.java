package com.wiser.kids.ui.message.MessageAudioRecord;

import android.media.MediaPlayer;

import com.wiser.kids.BasePresenter;
import com.wiser.kids.BaseView;

public class MessageAudioRecordContract {

    interface View extends BaseView<Presenter>
    {
        void onRecordingStarted(boolean isStarted);
        void onMediaPlayStarted();
        void onMediaPaused();
        void onMediaFinished();
        void showMessage(String msg);
        void onMediaFileShare(String filePath);
    }
    interface  Presenter extends BasePresenter
    {
        void startRecording();
        void stopRecording();
        void playRecording(MediaPlayer player);
        void pauseMedia(MediaPlayer player);
        void resumeMedia(MediaPlayer player);
        boolean isMediaAvailable();
        void shareMedia();
        }
}
