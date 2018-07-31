package com.wiser.kids.ui.favorite.links;

import com.wiser.kids.BasePresenter;
import com.wiser.kids.BaseView;
import com.wiser.kids.model.LinksEntity;

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
    }


}
