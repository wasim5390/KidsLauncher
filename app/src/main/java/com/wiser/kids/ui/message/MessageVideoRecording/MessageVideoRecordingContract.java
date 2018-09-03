package com.wiser.kids.ui.message.MessageVideoRecording;

import android.net.Uri;

import com.wiser.kids.BasePresenter;
import com.wiser.kids.BaseView;

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
