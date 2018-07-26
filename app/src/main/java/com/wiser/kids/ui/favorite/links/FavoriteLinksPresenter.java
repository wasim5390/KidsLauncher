package com.wiser.kids.ui.favorite.links;

import com.wiser.kids.model.SlideItem;
import com.wiser.kids.source.Repository;
import com.wiser.kids.ui.favorite.fav_apps.FavoriteAppContract;
import com.wiser.kids.ui.home.apps.AppsEntity;
import com.wiser.kids.util.PreferenceUtil;

import java.util.ArrayList;
import java.util.List;

public class FavoriteLinksPresenter implements FavoriteLinksContract.Presenter {

    public FavoriteLinksContract.View view;
    public SlideItem slideItem;
    public PreferenceUtil preferenceUtil;
    public Repository repository;
    public List<LinksEntity> mFavLinkList=new ArrayList<LinksEntity>();


    public FavoriteLinksPresenter(FavoriteLinksContract.View view, SlideItem slideItem, PreferenceUtil preferenceUtil, Repository repository) {
        this.repository = repository;
        this.slideItem = slideItem;
        this.preferenceUtil=preferenceUtil;
        this.mFavLinkList = new ArrayList<>();
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public void start() {
        LinksEntity linksEntity = new LinksEntity(null,null);
        linksEntity.setFlagEmptylist(true);
        mFavLinkList.add(linksEntity);
        view.onFavoriteLinksLoaded(mFavLinkList);
    }


}
