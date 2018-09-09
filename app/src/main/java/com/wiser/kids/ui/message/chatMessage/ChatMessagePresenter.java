package com.wiser.kids.ui.message.chatMessage;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

import com.wiser.kids.model.response.BaseResponse;
import com.wiser.kids.model.response.GetAllChatResponse;
import com.wiser.kids.source.DataSource;
import com.wiser.kids.source.Repository;
import com.wiser.kids.ui.home.contact.ContactEntity;
import com.wiser.kids.util.PreferenceUtil;
import com.wiser.kids.util.Util;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static com.wiser.kids.Constant.MEDIA_AUDIO;
import static com.wiser.kids.Constant.MEDIA_VIDEO;

public class ChatMessagePresenter implements ChatMessageContract.Presenter {

    public ChatMessageContract.View view;
    public PreferenceUtil preferenceUtil;
    public Repository repository;
    public String vpath,aPath;
    public String contactid;
    public MediaRecorder recorder;
    public List<ChatMessageEntity> msgList;

    public ChatMessagePresenter(ChatMessageContract.View view, PreferenceUtil preferenceUtil, Repository repository) {
        this.view = view;
        this.preferenceUtil = preferenceUtil;
        this.repository = repository;
        view.setPresenter(this);
    }

    @Override
    public void getAllMessage(String contactId) {

        this.contactid = contactId;
        repository.getChatMessageList(getUserId(), contactId, new DataSource.GetDataCallback<GetAllChatResponse>() {
            @Override
            public void onDataReceived(GetAllChatResponse data) {
                if (data != null) {
                    if (data.isSuccess()) {
                        msgList.clear();
                        msgList.addAll(data.getMsgList());
                        view.loadMessageList(msgList);
                    }
                }
            }

            @Override
            public void onFailed(int code, String message) {

            }
        });

    }

    @Override
    public String getUserId() {
        return preferenceUtil.getAccount().getId();
    }

    @Override
    public void start() {
        msgList = new ArrayList<>();
    }

    @Override
    public void videoInFile(String srcfilepath) {
        File srcfile = new File(srcfilepath);
        String filepath = Environment.getExternalStorageDirectory().getPath() + "/Kids Launcher/Video";
        File desFile = new File(filepath);
        if (!desFile.exists()) {
            desFile.mkdirs();
        }
        try {
            desFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        File file = Util.copyFileOrDirectory(srcfile.getAbsolutePath(), desFile.getAbsolutePath());
        Log.e("FilePath ", file.getAbsolutePath());
        vpath = file.getAbsolutePath();
        shareVideoMedia(srcfile);

    }

    @Override
    public void shareVideoMedia(File file) {
        if (view.deleteFile(file)) {
            view.onMediaFileShare(vpath, MEDIA_VIDEO);
        }
    }
    /////////////////////////Audio portion///////

    @Override
    public void startRecording() {
        if(aPath!=null)
            new File(aPath).delete();
        aPath = getFilename();
        recorder = new MediaRecorder();

        recorder.setAudioSource(1);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(aPath);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        recorder.setMaxDuration(10*6000);

        try
        {
            recorder.prepare();
            recorder.start();
            //view.onRecordingStarted(true);
//            recorder.setOnInfoListener((mr, what, extra) -> {
//                if (what == MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED) {
//                  //  stopRecording();
//                }
//            });
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

        //view.onRecordingStarted(false);
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
        return aPath;
    }

    @Override
    public boolean isMediaAvailable(){
        if(aPath==null || new File(getFilePath()).length()<=0)
            return false;
        return true;

    }
    @Override
    public void deleteAudioFile()
    {
        File file=new File(getFilePath());
        aPath=null;
        view.deleteFile(file);
    }

    @Override
    public void shareAudioMedia() {
        if(isMediaAvailable())
            view.onMediaFileShare(getFilePath(),MEDIA_AUDIO);
        else
            view.showMessage("Please Record Audio to share!");
    }


//////////////////////////////////Api for File sharing/////////////////////


    @Override
    public void shareFileToContact(File file, int type) {
        RequestBody fBody = null;
        Log.e("contactId",contactid);
        if (type == 2) {
            fBody = RequestBody.create(MediaType.parse("video/*"), file);
        } else if (type == 3) {
            fBody = RequestBody.create(MediaType.parse("audio/*"), file);

        }
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), fBody);

        RequestBody mediaType = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(type));
        RequestBody user_id = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(preferenceUtil.getAccount().getId()));

        HashMap<String, RequestBody> params = new HashMap<>();
        params.put("type", mediaType);
        params.put("user_id", user_id);
        view.showProgress();
        repository.shareMediaFile(params, body, contactid, new DataSource.GetDataCallback<GetAllChatResponse>() {
            @Override
            public void onDataReceived(GetAllChatResponse data) {
                view.hideProgress();
                if (data.isSuccess()) {
                    view.showMessage(data.getResponseMsg());
                    file.delete();
                    msgList.add(data.getEntity());
                    view.loadMessageList(msgList);

                }
                else
                {
                    view.showMessage(data.getResponseMsg());
                }
            }

            @Override
            public void onFailed(int code, String message) {
                view.hideProgress();
                view.showMessage(message);
            }
        });
    }

}
