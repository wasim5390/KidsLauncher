package com.wiser.kids.ui.home.dialer;

import com.wiser.kids.source.Repository;
import com.wiser.kids.util.PreferenceUtil;

public class DialerPresenter implements DialerContract.Presenter {

    private DialerContract.View view;
    private PreferenceUtil preferenceUtil;
    private Repository repository;

    public DialerPresenter(DialerContract.View view, PreferenceUtil util, Repository repository) {
        this.view = view;
        this.preferenceUtil = util;
        this.repository = repository;
        this.view.setPresenter(this);
    }

    @Override
    public void callContact() {

    }

    @Override
    public void start() {

    }
}
