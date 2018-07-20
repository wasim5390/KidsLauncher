package com.wiser.kids.ui.home.apps;

import com.wiser.kids.source.Repository;
import com.wiser.kids.util.PreferenceUtil;

import java.util.List;

public class AppsPresenter implements AppsContract.Presenter {

    private AppsContract.View view;
    private PreferenceUtil preferenceUtil;
    private Repository repository;
    private List<AppsEntity> applist;

    @Override
    public void start() {

    }
    public AppsPresenter(AppsContract.View view, PreferenceUtil util, Repository repository) {
        this.view = view;
        this.preferenceUtil = util;
        this.repository = repository;
        this.view.setPresenter(this);
    }


    @Override
    public void loadApps(List<AppsEntity> list) {
        view.onAppListLoaded(list);
    }

}
