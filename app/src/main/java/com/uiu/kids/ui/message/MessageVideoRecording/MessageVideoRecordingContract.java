package com.uiu.kids.ui.message.MessageVideoRecording;

import com.uiu.kids.BasePresenter;
import com.uiu.kids.BaseView;

import java.io.File;

public class MessageVideoRecordingContract {

    interface View extends BaseView<Presenter>
    {
        void loadCamera();
        void onMediaFileShare(String filePath);
        boolean deleteFile(File file);
    }
    interface Presenter extends BasePresenter
    {
        void videoInFile(String filepath);
        void shareMedia(File file);
    }

}
