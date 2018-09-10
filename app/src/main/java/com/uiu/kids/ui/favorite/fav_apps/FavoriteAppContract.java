package com.uiu.kids.ui.favorite.fav_apps;

import com.uiu.kids.BasePresenter;
import com.uiu.kids.BaseView;
import com.uiu.kids.ui.home.apps.AppsEntity;

import java.util.List;

public class FavoriteAppContract {

    interface View extends BaseView<Presenter>
    {
        void showMessage(String message);
        void onFavoriteAppsLoaded(List<AppsEntity> list);

    }
    interface Presenter extends BasePresenter
    {
        void loadFavApps();
        void saveFavoriteApp(AppsEntity entity);
        void updateEntity(AppsEntity entity);
    }
}
