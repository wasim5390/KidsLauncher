package com.uiu.kids.ui.message.MessageVideoRecording;

import android.os.Environment;
import android.util.Log;

import com.uiu.kids.source.Repository;
import com.uiu.kids.util.PreferenceUtil;
import com.uiu.kids.util.Util;

import java.io.File;
import java.io.IOException;

public class MessageVideoRecordingPresenter implements MessageVideoRecordingContract.Presenter {

   public MessageVideoRecordingContract.View view;
   public PreferenceUtil preferenceUtil;
   public Repository repository;
   public String path;

    public MessageVideoRecordingPresenter(MessageVideoRecordingContract.View view, PreferenceUtil preferenceUtil, Repository repository) {
        this.view = view;
        this.preferenceUtil = preferenceUtil;
        this.repository = repository;
        view.setPresenter(this);
    }

    @Override
    public void start() {


    }


    @Override
    public void videoInFile(String srcfilepath) {
        File srcfile=new File(srcfilepath);
       String filepath = Environment.getExternalStorageDirectory().getPath()+"/Kids Launcher/Video";
        File desFile = new File(filepath);
        if(!desFile.exists()){
            desFile.mkdirs();
        }
        try {
            desFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        File file= Util.copyFileOrDirectory(srcfile.getAbsolutePath(),desFile.getAbsolutePath());
        Log.e("FilePath ",file.getAbsolutePath());
        path=file.getAbsolutePath();
        shareMedia(srcfile);

        }

    @Override
    public void shareMedia(File file) {

        if(view.deleteFile(file))
        {
            view.onMediaFileShare(path);
        }


    }


}
