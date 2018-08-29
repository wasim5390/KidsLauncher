package com.wiser.kids.ui.message.MessageVideoRecording;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.view.menu.MenuWrapperFactory;
import android.util.Log;

import com.wiser.kids.source.Repository;
import com.wiser.kids.util.PreferenceUtil;
import com.wiser.kids.util.Util;

import java.io.File;
import java.io.IOException;

public class MessageVideoRecordingPresenter implements MessageVideoRecordingContract.Presenter {

   public MessageVideoRecordingContract.View view;
   public PreferenceUtil preferenceUtil;
   public Repository repository;


    public MessageVideoRecordingPresenter(MessageVideoRecordingContract.View view, PreferenceUtil preferenceUtil, Repository repository) {
        this.view = view;
        this.preferenceUtil = preferenceUtil;
        this.repository = repository;
    }

    @Override
    public void start() {


    }


    @Override
    public void videoInFile(File srcfile) {
        String filepath = Environment.getExternalStorageDirectory().getPath()+"/KidsLauncher/Video";
        File desFile = new File(filepath);
        if(!desFile.exists()){
            desFile.mkdirs();
        }
        try {
            desFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        File file=Util.copyFileOrDirectory(srcfile.getAbsolutePath(),desFile.getAbsolutePath());

        Log.e("File ", String.valueOf(file));
    }
}
