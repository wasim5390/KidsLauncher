package com.uiu.kids.ui.slides.links;

import com.uiu.kids.BasePresenter;
import com.uiu.kids.BaseView;
import com.uiu.kids.model.LinksEntity;
import com.uiu.kids.model.Slide;

import java.util.List;

public class FavoriteLinksContract {

    interface View extends BaseView<Presenter>
    {
        void onFavoriteLinksLoaded(List<LinksEntity> linksEntities);
        void showMassage(String msg);
        void slideSerial(int serial,int count);
        void itemAddedOnNewSlide(Slide newSlide);
        void onFavoriteLinkDataLoaded(String originalLink, LinksEntity entity);

        void onNewSlideCreated(Slide slideItem);
    }
    interface Presenter extends BasePresenter
    {
        void getFavLinkData(String link);

        void loadFavLinks();
        void updateFavLink(LinksEntity favLink);
        void addFavLinkOnSlide(LinksEntity entity);

        boolean canAddOnSlide();
    }


}
