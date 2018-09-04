package com.wiser.kids.ui.message.chatMessage;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static com.wiser.kids.Constant.MEDIA_VIDEO;

public class ChatMessagePresenter implements ChatMessageContract.Presenter {

    public ChatMessageContract.View view;
    public PreferenceUtil preferenceUtil;
    public Repository repository;
    public String path;
    public String contactid;
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
        path = file.getAbsolutePath();
        shareMedia(srcfile);

    }

    @Override
    public void shareMedia(File file) {
        if (view.deleteFile(file)) {
            view.onMediaFileShare(path, MEDIA_VIDEO);
        }
    }


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
