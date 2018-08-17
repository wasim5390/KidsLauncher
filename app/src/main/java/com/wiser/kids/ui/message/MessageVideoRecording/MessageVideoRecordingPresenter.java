package com.wiser.kids.ui.message.MessageVideoRecording;

import android.content.Intent;
import android.support.v7.view.menu.MenuWrapperFactory;

import com.wiser.kids.source.Repository;
import com.wiser.kids.util.PreferenceUtil;

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


}
