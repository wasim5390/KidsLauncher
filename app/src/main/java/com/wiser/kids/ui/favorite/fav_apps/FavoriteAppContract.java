package com.wiser.kids.ui.favorite.fav_apps;

import com.wiser.kids.BasePresenter;
import com.wiser.kids.BaseView;
import com.wiser.kids.ui.home.apps.AppsEntity;
import com.wiser.kids.ui.home.contact.ContactEntity;

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
    }
}
