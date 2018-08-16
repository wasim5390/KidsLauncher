package com.wiser.kids.ui.favorite.people;

import android.util.Log;

import com.wiser.kids.model.response.GetFavContactResponse;
import com.wiser.kids.source.DataSource;
import com.wiser.kids.source.Repository;

import com.wiser.kids.ui.home.contact.ContactEntity;
import com.wiser.kids.util.PreferenceUtil;

import java.util.ArrayList;
import java.util.List;


public class FavoritePeoplePresenter implements FavoritePeopleContract.Presenter {

    private Repository mRepository;
    private PreferenceUtil preferenceUtil;
    private FavoritePeopleContract.View mView;
    private List<ContactEntity> mFavList;
    private boolean isItemAdded=true;
    private String slideId;
    private static final String TAG = "FavoritePeoplePresenter";

    public FavoritePeoplePresenter(FavoritePeopleContract.View view,String slideId, PreferenceUtil preferenceUtil, Repository repository) {
        this.mRepository = repository;
        this.preferenceUtil = preferenceUtil;
        this.slideId = slideId;
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
    public void saveFavoritePeople(ContactEntity entity,String userId) {

        entity.setUserId(userId);
        for(int i=0;i<mFavList.size();i++)
        {

            if (entity.getmPhoneNumber().equals(mFavList.get(i).getmPhoneNumber()))
            {
              isItemAdded=true;
            }
        }

        if(!isItemAdded) {
            saveFavPeopleOnSlide(entity, slideId);

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

    public void saveFavPeopleOnSlide(ContactEntity contact,String id)
    {
        mView.showProgress();
        mRepository.addToSlide(id,contact,new DataSource.GetDataCallback<ContactEntity>() {
            @Override
            public void onDataReceived(ContactEntity data) {
                mView.hideProgress();
                ContactEntity addNewEntity = mFavList.get(mFavList.size() - 1);
                mFavList.remove(addNewEntity);
                mFavList.add(data);
                mFavList.add(addNewEntity);
                mView.onFavoritePeopleLoaded(mFavList);
               // Log.i(TAG,"ContactEntity-onDataReceived "+ data.getName());
            }

            @Override
            public void onFailed(int code, String message) {
                mView.hideProgress();
                Log.i(TAG,"ContactEntity-onDataReceived1"+ message);
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
        loadFavoritePeoples(slideId);
    }
}
