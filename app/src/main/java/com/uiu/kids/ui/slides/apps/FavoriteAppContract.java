package com.uiu.kids.ui.slides.apps;

import com.uiu.kids.BasePresenter;
import com.uiu.kids.BaseView;
import com.uiu.kids.model.Slide;
import com.uiu.kids.ui.home.apps.AppsEntity;

import java.util.List;

public class FavoriteAppContract {

    interface View extends BaseView<Presenter>
    {
        void showMessage(String message);
        void onFavoriteAppsLoaded(List<AppsEntity> list);
        void onNewSlideCreated(Slide slide);
        void slideSerial(int serial, int count);
        void itemAddedOnNewSlide(Slide slide);

    }
    interface Presenter extends BasePresenter
    {
        void loadFavApps();
        void saveFavoriteApp(AppsEntity entity);
        void updateEntity(AppsEntity entity);
    }
}
