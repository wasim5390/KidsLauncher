package com.wiser.kids.ui.message.MessageAudioRecord;

import android.Manifest;
import android.annotation.SuppressLint;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.wiser.kids.model.SlideItem;
import com.wiser.kids.source.Repository;
import com.wiser.kids.util.PreferenceUtil;

import java.io.File;
import java.io.IOException;

public class MessageAudioRecordPresenter implements MessageAudioRecordContract.Presenter {

    public MessageAudioRecordContract.View view;
    public PreferenceUtil preferenceUtil;
    public Repository repository;
    public SlideItem slideItem;
    MediaRecorder recorder;

    public MessageAudioRecordPresenter(MessageAudioRecordContract.View view, SlideItem slideItem, PreferenceUtil preferenceUtil, Repository repository) {
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
        recorder = new MediaRecorder();

        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(getFilename());
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try
        {
            recorder.prepare();
            recorder.start();
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


    }

    @SuppressLint("SdCardPath")
    private String getFilename()
    {
        String filepath = Environment.getExternalStorageDirectory().getPath()+"/KidsLauncher";
        File file = new File(filepath);

        if(!file.exists()){
            file.mkdirs();


        }

        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        view.showMessage(file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".mp3");

        Log.e("file",file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".mp3");
        return (file.getAbsolutePath() + "/Audio.mp3");
    }
}
