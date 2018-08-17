package com.wiser.kids.ui.favorite.fav_apps;

import android.widget.Toast;

import com.wiser.kids.model.SlideItem;
import com.wiser.kids.model.request.FavAppsRequest;
import com.wiser.kids.model.response.GetFavAppsResponse;
import com.wiser.kids.source.DataSource;
import com.wiser.kids.source.Repository;
import com.wiser.kids.ui.home.apps.AppsEntity;
import com.wiser.kids.ui.home.contact.ContactEntity;
import com.wiser.kids.util.PreferenceUtil;

import java.util.ArrayList;
import java.util.List;

public class FavoriteAppsPresenter implements FavoriteAppContract.Presenter{

    private FavoriteAppContract.View view;
    private Repository repository;
    private  PreferenceUtil preferenceUtil;
    private SlideItem slideItem;
    private List<AppsEntity> mFavList;
    private boolean isAddedItem=false;

    public FavoriteAppsPresenter(FavoriteAppContract.View view, SlideItem slideItem,PreferenceUtil preferenceUtil, Repository repository) {
        this.repository = repository;
        this.slideItem = slideItem;
        this.preferenceUtil=preferenceUtil;
        this.mFavList = new ArrayList<>();
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public void start() {

        AppsEntity appsEntity = new AppsEntity(null,null,null);
        appsEntity.setFlagEmptylist(true);
        mFavList.add(appsEntity);
        view.onFavoriteAppsLoaded(mFavList);
        loadFavApps();
    }


    @Override
    public void loadFavApps() {
    repository.getFavApps(slideItem.getId(), new DataSource.GetDataCallback<GetFavAppsResponse>() {
        @Override
        public void onDataReceived(GetFavAppsResponse data) {
            if(data.isSuccess()){

                AppsEntity addNewEntity = mFavList.get(mFavList.size()-1);
                mFavList.clear();
                mFavList.addAll(data.getFavAppsList());
                mFavList.add(addNewEntity);
                view.onFavoriteAppsLoaded(mFavList);
            }else{
                view.showMessage(data.getResponseMsg());
            }
        }

        @Override
        public void onFailed(int code, String message) {
            view.showMessage(message);
        }
    });
    }

    @Override
    public void saveFavoriteApp(AppsEntity entity) {

        for (int i =0;i<mFavList.size();i++) {

            if(entity.getName().equals(mFavList.get(i).getName()))
            {
                isAddedItem=true;
            }

        }

        if(!isAddedItem) {


            entity.setSlideId(slideItem.getId());
            entity.setUserId(preferenceUtil.getAccount().getId());
            FavAppsRequest request = new FavAppsRequest();
            request.setApp(entity);
            view.showProgress();
            repository.addFavAppToSlide(request, new DataSource.GetDataCallback<GetFavAppsResponse>() {
                @Override
                public void onDataReceived(GetFavAppsResponse data) {
                    view.hideProgress();
                    AppsEntity addNewEntity = mFavList.get(mFavList.size()-1);
                    mFavList.remove(addNewEntity);
                    mFavList.add(data.getAppsEntity());

                    mFavList.add(addNewEntity);
                    view.onFavoriteAppsLoaded(mFavList);
                }

                @Override
                public void onFailed(int code, String message) {
                    view.hideProgress();
                    view.showMessage(message);
                }
            });
        }
        else
        {
            isAddedItem=false;
            view.showMessage("You have aleady add this app");
           // view.onFavoriteAppsLoaded(mFavList);
        }
    }

    @Override
    public void updateEntity(AppsEntity entity) {
        try {
            for (AppsEntity appsEntity : mFavList) {
                if (appsEntity != null && appsEntity.getId() != null && appsEntity.getId().equals(entity.getId())) {
                    appsEntity.setRequestStatus(entity.getRequestStatus());
                    break;
                }
            }
            view.onFavoriteAppsLoaded(mFavList);
        } catch (Exception e) {
            loadFavApps();
        }

    }

}
