package com.wiser.kids.ui.home.apps;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.graphics.drawable.Drawable;

import com.wiser.kids.source.Repository;
import com.wiser.kids.ui.home.dialer.DialerContract;
import com.wiser.kids.util.PreferenceUtil;

import java.util.ArrayList;
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
    public void loadInstalledAppsList(List<AppsEntity> list) {

        view.installedAppListLoaded(list);

    }

}
