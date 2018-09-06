package com.wiser.kids.ui.message.chatMessage;

import com.wiser.kids.BasePresenter;
import com.wiser.kids.BaseView;

import java.io.File;
import java.util.List;

public class ChatMessageContract {

    interface View extends BaseView<Presenter>
    {
        void loadMessageList(List<ChatMessageEntity> list);
        void onMediaFileShare(String filePath,int path);
        boolean deleteFile(File file);
        void showMessage(String msg);
    }
    interface Presenter extends BasePresenter
    {
        void getAllMessage(String contactid);
        String getUserId();
        void videoInFile(String filepath);
        void shareVideoMedia(File file);
        void shareFileToContact(File file,int type);
        void startRecording();
        void stopRecording();
        boolean isMediaAvailable();
        void shareAudioMedia();
        void deleteAudioFile();
    }
}
