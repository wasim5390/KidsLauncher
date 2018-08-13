package com.wiser.kids.ui.home.apps;

import android.content.pm.PackageInfo;

import com.wiser.kids.source.Repository;
import com.wiser.kids.util.PreferenceUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AppsPresenter implements AppsContract.Presenter {

    private AppsContract.View view;
    private PreferenceUtil preferenceUtil;
    private Repository repository;
    private List<AppsEntity> applist =  new ArrayList<>();

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
        applist.clear();
        applist.addAll(list);
        view.onAppListLoaded(applist);
    }

    @Override
    public void searchApps(String app) {
        if (!app.trim().isEmpty()) {
            List<AppsEntity> searchResults = new ArrayList<>();
            for (int i = 0; i < applist.size(); i++) {
                if (applist.get(i).getName().toLowerCase().contains(app.toLowerCase())) {
                    searchResults.add(applist.get(i));
                }
            }
            view.onAppListLoaded(searchResults);
        } else {
            view.onAppListLoaded(applist);
        }
    }

}
