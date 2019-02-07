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
    private List<AppsEntity> mDataList;
    private boolean isAddedItem=false;
    private boolean isLoading=false;

    public FavoriteAppsPresenter(FavoriteAppContract.View view, Slide slideItem, PreferenceUtil preferenceUtil, Repository repository) {
        this.repository = repository;
        this.slideItem = slideItem;
        this.preferenceUtil=preferenceUtil;
        this.mFavList = new ArrayList<>();
        this.mDataList = new ArrayList<>();
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public void start() {

        AppsEntity appsEntity = new AppsEntity();
        appsEntity.setFlagEmptylist(true);
        mFavList.add(appsEntity);
        mFavList.add(appsEntity);
        mFavList.add(appsEntity);
        mFavList.add(appsEntity);
        view.onFavoriteAppsLoaded(mFavList);
        view.slideSerial(slideItem.getSerial(),slideItem.getCount());
        List<AppsEntity> localList= preferenceUtil.getFavAppsList(slideItem.getId());
        if(localList!=null)
            loadAppsFromLocal(localList);
        else
            loadFavApps();
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
                    mDataList.clear();
                    mFavList.clear();
                    mDataList.addAll(data.getFavAppsList());
                    mFavList.addAll(mDataList);

                    for(int i=0;i<4-mDataList.size();i++)
                        mFavList.add(new AppsEntity());
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

        mFavList.clear();
        mFavList.addAll(localList);
        mDataList.clear();
        mDataList.addAll(localList);
        for(int i=0;i<4-localList.size();i++)
            mFavList.add(new AppsEntity());
        view.onFavoriteAppsLoaded(mFavList);
    }

    @Override
    public void saveFavoriteApp(AppsEntity entity) {
        if(preferenceUtil.getAccount().getPrimaryHelper()==null ||
                !preferenceUtil.getAccount().getPrimaryHelper().isPrimaryConnected())
            return;
        entity.setUserId(preferenceUtil.getAccount().getId());
        entity.setSlideId(slideItem.getId());

        if(isLastSlide() && mDataList.size()>=4){
            addNewSlide(entity);
            return;
        }else{

            saveAppOnSlide(entity,null);
        }

    }

    public void saveAppOnSlide(AppsEntity entity,Slide newSlide){
        if(!Util.isInternetAvailable()) {
            view.showNoInternet();
            return;
        }
        FavAppsRequest request = new FavAppsRequest();
        request.setApp(entity);
        view.showProgress();
        repository.addFavAppToSlide(request, new DataSource.GetDataCallback<GetFavAppsResponse>() {
            @Override
            public void onDataReceived(GetFavAppsResponse data) {
                view.hideProgress();
                if(data.isSuccess()) {
                    preferenceUtil.saveFavApp(entity.getSlideId(),data.getAppsEntity());
                    mDataList.clear();
                    mFavList.clear();
                    mDataList.addAll(preferenceUtil.getFavAppsList(entity.getSlideId()));
                    mFavList.addAll(mDataList);
                    for(int i=0;i<4-mDataList.size();i++)
                        mFavList.add(new AppsEntity());

                    if(newSlide!=null)
                        view.itemAddedOnNewSlide(newSlide);

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
                    view.onNewSlideCreated(data.getSlideItem());
                    appsEntity.setSlideId(data.getSlideItem().getId());
                    saveAppOnSlide(appsEntity,data.getSlideItem());
                    //
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

    @Override
    public boolean canAddOnSlide(){
        if(!isLastSlide() && mDataList.size()>=4)
        {
            view.showMessage(Constant.NO_SPACE_ON_SLIDE);
            return false;
        }
        return true;
    }

    private boolean isLastSlide(){
        Integer actualSerial = slideItem.getSerial()+1;
        return actualSerial>=slideItem.getCount();
    }

}
