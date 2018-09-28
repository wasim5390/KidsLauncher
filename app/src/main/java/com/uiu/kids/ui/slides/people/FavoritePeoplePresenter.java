package com.uiu.kids.ui.slides.people;

import android.util.Log;

import com.uiu.kids.BaseActivity;
import com.uiu.kids.model.Slide;
import com.uiu.kids.model.response.GetFavContactResponse;
import com.uiu.kids.source.DataSource;
import com.uiu.kids.source.Repository;
import com.uiu.kids.ui.home.contact.ContactEntity;
import com.uiu.kids.util.PreferenceUtil;

import java.util.ArrayList;
import java.util.List;


public class FavoritePeoplePresenter implements FavoritePeopleContract.Presenter {

    private Repository mRepository;
    private PreferenceUtil preferenceUtil;
    private FavoritePeopleContract.View mView;
    private List<ContactEntity> mFavList;
    private boolean isItemAdded=false;
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
    public void loadFavoritePeoples(String slideId) {
        mRepository.fetchFromSlide(slideId,new DataSource.GetDataCallback<GetFavContactResponse>() {

            @Override
            public void onDataReceived(GetFavContactResponse data) {

                if(data.isSuccess()) {
                    ContactEntity addNewEntity = mFavList.get(mFavList.size() - 1);
                    mFavList.clear();
                    mFavList.addAll(data.getContactEntityList());
                    if(mFavList.size()<=3)
                        mFavList.add(addNewEntity);
                    mView.onFavoritePeopleLoaded(mFavList);
                }else{
                    mView.showMessage(data.getResponseMsg());
                }


            }

            @Override
            public void onFailed(int code, String message) {
                Log.i(TAG,"ContactEntity-onDataReceived1"+ message);
                Log.d(TAG, "onFailed: ");
            }
        });
    }

    @Override
    public void saveFavoritePeople(ContactEntity entity, String userId) {
        if(BaseActivity.primaryParentId==null)
            return;
        entity.setUserId(userId);
        for(int i=0;i<mFavList.size();i++)
        {

            if (entity.getAndroidId().equals(mFavList.get(i).getAndroidId())
                    && entity.getLookupId().equals(mFavList.get(i).getLookupId()))
            {
                isItemAdded=true;
                break;
            }
        }

        if(!isItemAdded) {
            saveFavPeopleOnSlide(entity, slide.getId());

        }
        else
        {
            isItemAdded=false;
            mView.showMessage("You have aleady add this number");

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

    public void saveFavPeopleOnSlide(ContactEntity contact, String id)
    {
        mView.showProgress();
        mRepository.addFavPeopleToSlide(id,contact,new DataSource.GetDataCallback<GetFavContactResponse>() {
            @Override
            public void onDataReceived(GetFavContactResponse data) {
                mView.hideProgress();
                if(data.isSuccess()) {
                    ContactEntity addNewEntity = mFavList.get(mFavList.size() - 1);
                    mFavList.remove(addNewEntity);
                    mFavList.add(data.getFavoriteContact());
                    if (mFavList.size() <= 3)
                        mFavList.add(addNewEntity);
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


    @Override
    public void start() {
        mFavList = new ArrayList<>();
        ContactEntity entity = new ContactEntity();
        mFavList.add(entity);
        mView.onFavoritePeopleLoaded(mFavList);
        loadFavoritePeoples(slide.getId());
        mView.slideSerial(slide.getSerial());
    }
}
