package com.wiser.kids.ui.favorite.fav_apps;

import com.wiser.kids.source.Repository;
import com.wiser.kids.ui.home.apps.AppsEntity;
import com.wiser.kids.ui.home.contact.ContactEntity;
import com.wiser.kids.util.PreferenceUtil;

import java.util.List;

public class FavoriteAppsPresenter implements FavoriteAppContract.Presenter{

    private FavoriteAppContract.View view;
    private Repository repository;
    private PreferenceUtil preferenceUtil;
    private List<AppsEntity> mFavList;

    public FavoriteAppsPresenter(FavoriteAppContract.View view, PreferenceUtil preferenceUtil, Repository repository) {
        this.repository = repository;
        this.preferenceUtil = preferenceUtil;
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public void start() {
        mFavList = preferenceUtil.getFavAppsList();
        AppsEntity appsEntity = new AppsEntity(null,null);
        appsEntity.setFlagEmptylist(true);
        mFavList.add(appsEntity);
        view.onFavoriteAppsLoaded(mFavList);
    }


    @Override
    public void saveFavoriteApps(AppsEntity entity) {
        AppsEntity addNewEntity = mFavList.get(mFavList.size()-1);
        mFavList.remove(addNewEntity);
        mFavList.add(entity);
        preferenceUtil.saveFavApps(mFavList);
        mFavList.add(addNewEntity);
        view.onFavoriteAppsLoaded(mFavList);

    }
}
