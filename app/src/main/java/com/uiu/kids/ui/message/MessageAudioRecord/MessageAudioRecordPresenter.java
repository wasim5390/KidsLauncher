package com.uiu.kids.ui.message.MessageAudioRecord;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

import com.uiu.kids.model.Slide;
import com.uiu.kids.source.Repository;
import com.uiu.kids.util.PreferenceUtil;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MessageAudioRecordPresenter implements MessageAudioRecordContract.Presenter {

    public MessageAudioRecordContract.View view;
    public PreferenceUtil preferenceUtil;
    public Repository repository;
    public Slide slideItem;
    MediaRecorder recorder;
    public String filePath;

    public MessageAudioRecordPresenter(MessageAudioRecordContract.View view, Slide slideItem, PreferenceUtil preferenceUtil, Repository repository) {
        this.view = view;
        this.preferenceUtil = preferenceUtil;
        this.repository = repository;
        this.slideItem=slideItem;
        view.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void startRecording() {
        if(filePath!=null)
            new File(filePath).delete();
        filePath = getFilename();
        recorder = new MediaRecorder();

        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(filePath);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        recorder.setMaxDuration(10*6000);

        try
        {
            recorder.prepare();
            recorder.start();
            view.onRecordingStarted(true);
            recorder.setOnInfoListener((mr, what, extra) -> {
                if (what == MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED) {
                    stopRecording();
                }
            });
        }
        catch (IllegalStateException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void stopRecording() {

        if(null != recorder)
        {
            recorder.stop();
            recorder.reset();
            recorder.release();
            recorder = null;

        }

        view.onRecordingStarted(false);
    }

    @Override
    public void playRecording(MediaPlayer mp) {
        try {
            mp.setDataSource(getFilePath());
            mp.setOnCompletionListener(mp1 -> {
                view.onMediaFinished();
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            mp.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mp.start();
        view.onMediaPlayStarted();

    }

    @Override
    public void pauseMedia(MediaPlayer player) {
        player.pause();
        view.onMediaPaused();
    }

    @Override
    public void resumeMedia(MediaPlayer player) {
        player.start();
        view.onMediaPlayStarted();
    }

    @SuppressLint("SdCardPath")
    private String getFilename()
    {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String suffix = "AUDIO_" + timeStamp;
        String filepath = Environment.getExternalStorageDirectory().getPath()+"/Kids Launcher/Audio";
        File file = new File(filepath);

        if(!file.exists()){
            file.mkdirs();
        }

        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String path = file.getAbsolutePath() + "/"+suffix+".mp3";

        Log.e("file",path);
        return (path);
    }



    public String getFilePath(){
        return filePath;
    }

    @Override
    public boolean isMediaAvailable(){
        if(filePath==null || new File(getFilePath()).length()<=0)
            return false;
        return true;

    }

    @Override
    public void shareMedia() {
        if(isMediaAvailable())
            view.onMediaFileShare(getFilePath());
        else
            view.showMessage("Please Record Audio to share!");
}
}
