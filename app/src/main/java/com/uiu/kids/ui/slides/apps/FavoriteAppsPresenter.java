package com.uiu.kids.ui.slides.apps;

import com.uiu.kids.BaseActivity;
import com.uiu.kids.Constant;
import com.uiu.kids.model.Slide;
import com.uiu.kids.model.request.CreateSlideRequest;
import com.uiu.kids.model.request.FavAppsRequest;
import com.uiu.kids.model.response.CreateSlideResponse;
import com.uiu.kids.model.response.GetFavAppsResponse;
import com.uiu.kids.source.DataSource;
import com.uiu.kids.source.Repository;
import com.uiu.kids.ui.home.apps.AppsEntity;
import com.uiu.kids.util.PreferenceUtil;
import com.uiu.kids.util.Util;

import java.util.ArrayList;
import java.util.List;

import static com.uiu.kids.Constant.FAV_APP_SLIDE_NAME;

public class FavoriteAppsPresenter implements FavoriteAppContract.Presenter{

    private FavoriteAppContract.View view;
    private Repository repository;
    private PreferenceUtil preferenceUtil;
    private Slide slideItem;
    private List<AppsEntity> mFavList;
    private boolean isAddedItem=false;
    private boolean isLoading=false;

    public FavoriteAppsPresenter(FavoriteAppContract.View view, Slide slideItem, PreferenceUtil preferenceUtil, Repository repository) {
        this.repository = repository;
        this.slideItem = slideItem;
        this.preferenceUtil=preferenceUtil;
        this.mFavList = new ArrayList<>();
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public void start() {

        AppsEntity appsEntity = new AppsEntity(null,null);
        appsEntity.setFlagEmptylist(true);
        mFavList.add(appsEntity);
        view.onFavoriteAppsLoaded(mFavList);
        view.slideSerial(slideItem.getSerial(),slideItem.getCount());
        List<AppsEntity> localList= preferenceUtil.getFavAppsList(slideItem.getId());
        loadAppsFromLocal(localList);
       // loadFavApps();
    }


    @Override
    public void loadFavApps() {
        if(!Util.isInternetAvailable()) {
            view.showNoInternet();
            return;
        }
            if(!isLoading) {
            isLoading = true;
        }else{
            return;
        }
        repository.getFavApps(slideItem.getId(), new DataSource.GetDataCallback<GetFavAppsResponse>() {
            @Override
            public void onDataReceived(GetFavAppsResponse data) {
                isLoading=false;
                if(data.isSuccess()){
                    preferenceUtil.saveFavApps(slideItem.getId(),data.getFavAppsList());
                    AppsEntity addNewEntity = mFavList.get(mFavList.size()-1);
                    mFavList.clear();
                    mFavList.addAll(data.getFavAppsList());

                    if(mFavList.size()<=3 || isLastSlide() && mFavList.size()>=4)
                        mFavList.add(addNewEntity);
                    view.onFavoriteAppsLoaded(mFavList);

                }else{
                    view.showMessage(data.getResponseMsg());
                }
            }

            @Override
            public void onFailed(int code, String message) {
                isLoading=false;
                view.showMessage(message);
            }
        });
    }

    private void loadAppsFromLocal(List<AppsEntity> localList){

        AppsEntity addNewEntity = mFavList.get(mFavList.size()-1);
        mFavList.clear();
        mFavList.addAll(localList);

        if(mFavList.size()<=3 || (isLastSlide() && mFavList.size()>=4))
            mFavList.add(addNewEntity);
        view.onFavoriteAppsLoaded(mFavList);
    }

    @Override
    public void saveFavoriteApp(AppsEntity entity) {
        entity.setOnNewSlide(false);
        if(BaseActivity.primaryParentId==null)
            return;

        if(isLastSlide() && mFavList.size()>4){
            addNewSlide(entity);
            return;
        }else{
            entity.setSlideId(slideItem.getId());
            saveAppOnSlide(entity,null);
        }

    }

    public void saveAppOnSlide(AppsEntity entity,Slide newSlide){
        if(!Util.isInternetAvailable()) {
            view.showNoInternet();
            return;
        }
        entity.setUserId(preferenceUtil.getAccount().getId());
        FavAppsRequest request = new FavAppsRequest();
        request.setApp(entity);
        view.showProgress();
        repository.addFavAppToSlide(request, new DataSource.GetDataCallback<GetFavAppsResponse>() {
            @Override
            public void onDataReceived(GetFavAppsResponse data) {
                view.hideProgress();
                if(data.isSuccess()) {
                    AppsEntity addNewEntity = mFavList.get(mFavList.size() - 1);
                    mFavList.remove(addNewEntity);
                    mFavList.add(data.getAppsEntity());

                    if (mFavList.size() <= 3 || (isLastSlide() && mFavList.size()>=4))
                        if(newSlide==null) {
                            preferenceUtil.saveFavApps(slideItem.getId(),mFavList);
                            mFavList.add(addNewEntity);
                        }
                        else {
                            List<AppsEntity> list = new ArrayList<>();
                            list.add(data.getAppsEntity());
                            preferenceUtil.saveFavApps(newSlide.getId(),list);
                            view.itemAddedOnNewSlide(newSlide);
                        }
                    view.onFavoriteAppsLoaded(mFavList);
                }else
                    view.showMessage(data.getResponseMsg());
            }

            @Override
            public void onFailed(int code, String message) {
                view.hideProgress();
                view.showMessage(message);
            }
        });
    }

    public void addNewSlide(AppsEntity appsEntity){
        if(!Util.isInternetAvailable()) {
            view.showNoInternet();
            return;
        }
        Slide newSlide = new Slide();
        newSlide.setUser_id(preferenceUtil.getAccount().getId());
        newSlide.setType(Constant.SLIDE_INDEX_FAV_APP);
        newSlide.setName(FAV_APP_SLIDE_NAME);
        CreateSlideRequest slideRequest = new CreateSlideRequest();
        slideRequest.setSlide(newSlide);
        repository.createSlide(slideRequest, new DataSource.GetDataCallback<CreateSlideResponse>() {
            @Override
            public void onDataReceived(CreateSlideResponse data) {
                if(data.isSuccess()){
                    appsEntity.setSlideId(data.getSlideItem().getId());
                    saveAppOnSlide(appsEntity,data.getSlideItem());
                   // view.onNewSlideCreated(data.getSlideItem());
                }else
                    view.showMessage(data.getResponseMsg());
            }

            @Override
            public void onFailed(int code, String message) {

            }
        });
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

    private boolean isLastSlide(){
        Integer actualSerial = slideItem.getSerial()+1;
        return actualSerial>=slideItem.getCount();
    }

}
