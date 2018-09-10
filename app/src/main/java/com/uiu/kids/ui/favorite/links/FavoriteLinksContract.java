package com.uiu.kids.ui.favorite.links;

import com.uiu.kids.BasePresenter;
import com.uiu.kids.BaseView;
import com.uiu.kids.model.LinksEntity;

import java.util.List;

public class FavoriteLinksContract {

    interface View extends BaseView<Presenter>
    {
        void onFavoriteLinksLoaded(List<LinksEntity> linksEntities);
        void showProgressbar();
        void hideProgressbar();
        void showMassage(String msg);
    }
    interface Presenter extends BasePresenter
    {
        void getFavLinkData(String link);
        void loadFavLinks();
        void updateFavLink(LinksEntity favLink);
    }


}
