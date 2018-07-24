package com.wiser.kids.ui.favorite.fav_apps;

import com.wiser.kids.model.request.FavAppsRequest;
import com.wiser.kids.model.response.GetFavAppsResponse;
import com.wiser.kids.source.DataSource;
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
    public void loadFavApps() {

    }

    @Override
    public void saveFavoriteApp(AppsEntity entity) {
        AppsEntity addNewEntity = mFavList.get(mFavList.size()-1);
        entity.setSlideId(entity.getSlideId());
        FavAppsRequest request = new FavAppsRequest();
        request.setApp(entity);

        repository.addFavAppToSlide(request, new DataSource.GetDataCallback<GetFavAppsResponse>() {
            @Override
            public void onDataReceived(GetFavAppsResponse data) {
                mFavList.remove(addNewEntity);
                mFavList.add(data.getAppsEntity());
                preferenceUtil.saveFavApps(mFavList);
                mFavList.add(addNewEntity);
                view.onFavoriteAppsLoaded(mFavList);
            }

            @Override
            public void onFailed(int code, String message) {

            }
        });

    }
}
