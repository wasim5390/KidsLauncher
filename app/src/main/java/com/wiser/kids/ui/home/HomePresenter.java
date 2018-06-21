package com.wiser.kids.ui.home;

import com.wiser.kids.Constant;
import com.wiser.kids.source.Repository;
import com.wiser.kids.util.PreferenceUtil;

public class HomePresenter implements HomeContract.Presenter,Constant {

    private HomeContract.View view;
    private PreferenceUtil preferenceUtil;
    private Repository repository;

    public HomePresenter(HomeContract.View view, PreferenceUtil preferenceUtil, Repository repository) {
        this.view = view;
        this.preferenceUtil = preferenceUtil;
        this.repository = repository;
        this.view.setPresenter(this);
    }

    @Override
    public void login() {

    }

    @Override
    public void signUp() {

    }

    @Override
    public void getAccount() {

    }

    @Override
    public void start() {

    }
}
