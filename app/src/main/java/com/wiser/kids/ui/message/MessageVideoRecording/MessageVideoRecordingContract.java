package com.wiser.kids.ui.message.MessageVideoRecording;

import com.wiser.kids.BasePresenter;
import com.wiser.kids.BaseView;

public class MessageVideoRecordingContract {

    interface View extends BaseView<Presenter>
    {
        void loadCamera();
    }
    interface Presenter extends BasePresenter
    {


    }

}