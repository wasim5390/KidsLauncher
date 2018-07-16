package com.wiser.kids.ui.home.messaging;

import android.Manifest;

import com.wiser.kids.source.Repository;
import com.wiser.kids.ui.home.dialer.callhistory.CallHistoryContract;
import com.wiser.kids.util.PermissionUtil;
import com.wiser.kids.util.PreferenceUtil;

public class MessagePresenter implements MessageContract.Presenter {

    private MessageContract.View view;
    private PreferenceUtil preferenceUtil;
    private Repository repository;

    public MessagePresenter(MessageContract.View view, PreferenceUtil util, Repository repository) {
        this.view = view;
        this.preferenceUtil = util;
        this.repository = repository;
        this.view.setPresenter(this);
    }

    @Override
    public void start() {

    }


}
