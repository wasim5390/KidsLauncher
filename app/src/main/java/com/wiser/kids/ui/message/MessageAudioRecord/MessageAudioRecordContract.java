package com.wiser.kids.ui.message.MessageAudioRecord;

import com.wiser.kids.BasePresenter;
import com.wiser.kids.BaseView;

public class MessageAudioRecordContract {

    interface View extends BaseView<Presenter>
    {
        void showMessage(String msg);
    }
    interface  Presenter extends BasePresenter
    {
        void startRecording();
        void stopRecording();
    }
}
