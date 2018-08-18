package com.wiser.kids.ui.message.MessageVideoRecording;

import com.wiser.kids.BasePresenter;
import com.wiser.kids.BaseView;

import java.io.File;

public class MessageVideoRecordingContract {

    interface View extends BaseView<Presenter>
    {
        void loadCamera();
    }
    interface Presenter extends BasePresenter
    {
        void videoInFile(File file);

    }

}
