package com.uiu.kids.ui.slides.safe_places;

import android.util.Log;


import com.uiu.kids.Constant;
import com.uiu.kids.model.Location;
import com.uiu.kids.model.Slide;
import com.uiu.kids.model.User;

import com.uiu.kids.model.response.GetDirectionsResponse;
import com.uiu.kids.source.DataSource;
import com.uiu.kids.source.Repository;
import com.uiu.kids.util.PreferenceUtil;
import com.uiu.kids.util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



public class SafePlacesPresenter implements SafePlacesContract.Presenter {

    private Repository mRepository;


    private SafePlacesContract.View mView;
    private List<Location> mFavList;
    private List<Location> mDataList;
    private Slide slideEntity;
    private boolean isItemAdded=false;
    private PreferenceUtil preferenceUtil;
    private static final String TAG = "SafePlacesPresenter";

    public SafePlacesPresenter(SafePlacesContract.View view, Slide slide, PreferenceUtil preferenceUtil, Repository repository) {
        this.mRepository = repository;
        this.preferenceUtil = preferenceUtil;
        this.slideEntity = slide;
        this.mView = view;
        this.mView.setPresenter(this);
        mFavList = new ArrayList<>();
        mDataList = new ArrayList<>();
    }



    @Override
    public void loadDirections(String slideId) {
        if(!Util.isInternetAvailable()) {
            mView.showNoInternet();
            return;
        }
        mRepository.getDirectionsSlide(slideId,new DataSource.GetDataCallback<GetDirectionsResponse>() {

            @Override
            public void onDataReceived(GetDirectionsResponse data) {
                mView.hideProgress();
                if(data.isSuccess()) {
                    preferenceUtil.saveSafePlaces(slideEntity.getId(),data.getDirectionsList());
                    mDataList.clear();
                    mDataList.addAll(data.getDirectionsList());
                    mFavList.clear();
                    mFavList.addAll(mDataList);
                    mView.onDirectionsLoaded(mFavList);
                }else{
                    mView.showMessage(data.getResponseMsg());
                }


            }

            @Override
            public void onFailed(int code, String message) {
                mView.hideProgress();
                Log.i(TAG,"DirectionsItem-onDataReceived1"+ message);
                Log.d(TAG, "onFailed: ");
            }
        });
    }



    @Override
    public void start() {


        List<Location> localList= preferenceUtil.getSafePlacesList(slideEntity.getId());
        if(localList!=null)
            loadFromLocal(localList);
        else
        loadDirections(slideEntity.getId());
        mView.slideSerial(slideEntity.getSerial(),slideEntity.getCount());
    }

    private void loadFromLocal(List<Location> localList) {
        mFavList.clear();
        mFavList.addAll(localList);
        mDataList.clear();
        mDataList.addAll(localList);
        mView.onDirectionsLoaded(mFavList);
    }

    public boolean isLastSlide(){

        return slideEntity.getCount()==1;
    }

    public boolean isLastSlideOfType(){
        Integer actualSerial = slideEntity.getSerial()+1;
        return actualSerial>=slideEntity.getCount();
    }

}
