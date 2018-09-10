package com.uiu.kids.ui.home.dialer.callhistory;

import com.uiu.kids.source.Repository;
import com.uiu.kids.util.PreferenceUtil;

public class CallHistoryPresenter implements CallHistoryContract.Presenter {

    private CallHistoryContract.View view;
    private PreferenceUtil preferenceUtil;
    private Repository repository;

    public CallHistoryPresenter(CallHistoryContract.View view, PreferenceUtil util, Repository repository) {
        this.view = view;
        this.preferenceUtil = util;
        this.repository = repository;
        this.view.setPresenter(this);
    }


    @Override
    public void start() {

    }

    @Override
    public void callLogLoading(CallHistoryAdapter adapter) {
        view.callLogLoaded(adapter);
        }
}
