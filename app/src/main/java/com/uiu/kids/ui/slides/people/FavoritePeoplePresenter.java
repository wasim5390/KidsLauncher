package com.uiu.kids.ui.slides.people;

import android.util.Log;

import com.uiu.kids.BaseActivity;
import com.uiu.kids.Constant;
import com.uiu.kids.model.Slide;
import com.uiu.kids.model.request.CreateSlideRequest;
import com.uiu.kids.model.response.CreateSlideResponse;
import com.uiu.kids.model.response.GetFavContactResponse;
import com.uiu.kids.source.DataSource;
import com.uiu.kids.source.Repository;
import com.uiu.kids.ui.home.apps.AppsEntity;
import com.uiu.kids.ui.home.contact.ContactEntity;
import com.uiu.kids.util.PreferenceUtil;
import com.uiu.kids.util.Util;

import java.util.ArrayList;
import java.util.List;

import static com.uiu.kids.Constant.FAV_APP_SLIDE_NAME;
import static com.uiu.kids.Constant.FAV_PEOPLE_SLIDE_NAME;


public class FavoritePeoplePresenter implements FavoritePeopleContract.Presenter {

    private Repository mRepository;
    private PreferenceUtil preferenceUtil;
    private FavoritePeopleContract.View mView;
    private List<ContactEntity> mFavList;
    private List<ContactEntity> mDataList;
    private boolean isLoading=false;
    private Slide slide;
    private static final String TAG = "FavoritePeoplePresenter";

    public FavoritePeoplePresenter(FavoritePeopleContract.View view, Slide slide, PreferenceUtil preferenceUtil, Repository repository) {
        this.mRepository = repository;
        this.preferenceUtil = preferenceUtil;
        this.slide = slide;
        this.mView = view;
        this.mView.setPresenter(this);
    }

    @Override
    public void loadFavoritePeoples() {
        if(!Util.isInternetAvailable()) {
            mView.showNoInternet();
            return;
        }
        if(!isLoading) {
            isLoading = true;
        }else{
            return;
        }
        mRepository.fetchFromSlide(slide.getId(),new DataSource.GetDataCallback<GetFavContactResponse>() {

            @Override
            public void onDataReceived(GetFavContactResponse data) {
                isLoading=false;
                if(data.isSuccess()) {
                    preferenceUtil.saveFavPeople(slide.getId(),data.getContactEntityList());
                    mDataList.clear();
                    mFavList.clear();
                    mDataList.addAll(data.getContactEntityList());
                    mFavList.addAll(mDataList);
                    for(int i=0;i<4-mDataList.size();i++)
                        mFavList.add(new ContactEntity());
                    mView.onFavoritePeopleLoaded(mFavList);
                }else{
                    mView.showMessage(data.getResponseMsg());
                }

            }

            @Override
            public void onFailed(int code, String message) {
                isLoading=false;
                Log.i(TAG,"ContactEntity-onDataReceived1"+ message);
                Log.d(TAG, "onFailed: ");
            }
        });
    }

    @Override
    public void saveFavoritePeople(ContactEntity entity, String userId) {
        if(preferenceUtil.getAccount().getPrimaryHelper()==null ||
                !preferenceUtil.getAccount().getPrimaryHelper().isPrimaryConnected())
            return;
        entity.setUserId(userId);
        entity.setSlide_id(slide.getId());




        if(isLastSlide() && mDataList.size()>=4){
            addNewSlide(entity);
            return;
        }else{
            saveFavPeopleOnSlide(entity,null);
        }


    }

    @Override
    public void updateFavoritePeople(ContactEntity entity) {
        for(ContactEntity contactEntity: mFavList){
            if(contactEntity!=null && contactEntity.getId()!=null &&contactEntity.getId().equals(entity.getId())){
                contactEntity.setRequestStatus(entity.getRequestStatus());
                break;
            }
        }
        mView.onFavoritePeopleLoaded(mFavList);
    }

    @Override
    public boolean canAddOnSlide(){
        if(!isLastSlide() && mDataList.size()>=4)
        {
            mView.showMessage(Constant.NO_SPACE_ON_SLIDE);
            return false;
        }
        return true;
    }

    public void addNewSlide(ContactEntity contactEntity){
        if(!Util.isInternetAvailable()) {
            mView.showNoInternet();
            return;
        }
        Slide newSlide = new Slide();
        newSlide.setUser_id(preferenceUtil.getAccount().getId());
        newSlide.setType(Constant.SLIDE_INDEX_FAV_PEOPLE);
        newSlide.setName(FAV_PEOPLE_SLIDE_NAME);
        CreateSlideRequest slideRequest = new CreateSlideRequest();
        slideRequest.setSlide(newSlide);
        mRepository.createSlide(slideRequest, new DataSource.GetDataCallback<CreateSlideResponse>() {
            @Override
            public void onDataReceived(CreateSlideResponse data) {
                if(data.isSuccess()){
                    mView.onNewSlideCreated(data.getSlideItem());
                    contactEntity.setSlide_id(data.getSlideItem().getId());
                    saveFavPeopleOnSlide(contactEntity,data.getSlideItem());
                }else
                    mView.showMessage(data.getResponseMsg());
            }

            @Override
            public void onFailed(int code, String message) {

            }
        });
    }

    public void saveFavPeopleOnSlide(ContactEntity contact,Slide newSlide)
    {
        mView.showProgress();
        mRepository.addFavPeopleToSlide(contact.getSlide_id(),contact,new DataSource.GetDataCallback<GetFavContactResponse>() {
            @Override
            public void onDataReceived(GetFavContactResponse data) {
                mView.hideProgress();
                if(data.isSuccess()) {
                    preferenceUtil.saveFavPeople(contact.getSlide_id(),data.getFavoriteContact());
                    mDataList.clear();
                    mFavList.clear();
                    mDataList.addAll(preferenceUtil.getFavPeopleList(contact.getSlide_id()));
                    mFavList.addAll(mDataList);
                    for(int i=0;i<4-mDataList.size();i++)
                        mFavList.add(new ContactEntity());

                    if(newSlide!=null)
                        mView.itemAddedOnNewSlide(newSlide);
                    mView.onFavoritePeopleLoaded(mFavList);
                }else{
                    mView.showMessage(data.getResponseMsg());
                }
            }

            @Override
            public void onFailed(int code, String message) {
                mView.hideProgress();
                Log.d(TAG, "onFailed: ");
            }
        });
    }

    private void loadContactsFromLocal(List<ContactEntity> localList){

        mFavList.clear();
        mFavList.addAll(localList);
        for(int i=0;i<4-localList.size();i++)
            mFavList.add(new ContactEntity());
        mView.onFavoritePeopleLoaded(mFavList);
    }

    @Override
    public void start() {
        mFavList = new ArrayList<>();
        mDataList = new ArrayList<>();
        ContactEntity entity = new ContactEntity();
        mFavList.add(entity);
        mFavList.add(entity);
        mFavList.add(entity);
        mFavList.add(entity);

        mView.onFavoritePeopleLoaded(mFavList);
        mView.slideSerial(slide.getSerial(),slide.getCount());
        List<ContactEntity> localList= preferenceUtil.getFavPeopleList(slide.getId());
        if(localList!=null)
            loadContactsFromLocal(localList);
        else
            loadFavoritePeoples();
    }

    private boolean isLastSlide(){
        Integer actualSerial = slide.getSerial()+1;
        return actualSerial>=slide.getCount();
    }
}
