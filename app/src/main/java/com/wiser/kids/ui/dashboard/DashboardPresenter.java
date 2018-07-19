package com.wiser.kids.ui.dashboard;

import android.support.v4.app.Fragment;

import com.wiser.kids.Constant;
import com.wiser.kids.KidsLauncherApp;
import com.wiser.kids.source.Repository;
import com.wiser.kids.ui.favorite.fav_apps.FavoriteAppFragment;
import com.wiser.kids.ui.favorite.fav_apps.FavoriteAppsPresenter;
import com.wiser.kids.ui.favorite.people.FavoritePeopleContract;
import com.wiser.kids.ui.home.apps.AppsFragment;
import com.wiser.kids.ui.home.apps.AppsPresenter;
import com.wiser.kids.ui.favorite.people.FavoritePeopleFragment;
import com.wiser.kids.ui.favorite.people.FavoritePeoplePresenter;
import com.wiser.kids.ui.home.HomeFragment;
import com.wiser.kids.ui.home.HomePresenter;
import com.wiser.kids.util.PreferenceUtil;

import java.util.ArrayList;
import java.util.List;

public class DashboardPresenter implements DashboardContract.Presenter,Constant {

    private DashboardContract.View view;
    private PreferenceUtil preferenceUtil;
    private Repository repository;

    public DashboardPresenter(DashboardContract.View view, PreferenceUtil preferenceUtil, Repository repository) {
        this.view = view;
        this.preferenceUtil = preferenceUtil;
        this.repository = repository;
        this.view.setPresenter(this);
    }

    @Override
    public void login() {

    }


    @Override
    public void createSlides() {
        List<Fragment> mSlides = new ArrayList<>();

        HomeFragment homeFragment = HomeFragment.newInstance();
        new HomePresenter(homeFragment,repository);

        mSlides.add(homeFragment);

        FavoritePeopleFragment favoritePeopleFragment = FavoritePeopleFragment.newInstance();
        new FavoritePeoplePresenter(favoritePeopleFragment,PreferenceUtil.getInstance(KidsLauncherApp.getInstance()),repository);
        mSlides.add(favoritePeopleFragment);

        FavoriteAppFragment appsFragment = FavoriteAppFragment.newInstance();
        new FavoriteAppsPresenter(appsFragment,PreferenceUtil.getInstance(KidsLauncherApp.getInstance()),repository);
        mSlides.add(appsFragment);

      view.onSlidesCreated(mSlides);
    }

    @Override
    public void start() {

    }
}
