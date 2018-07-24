package com.wiser.kids.ui.favorite.people;

import android.util.Log;

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
    private ArrayList<ContactEntity> mFavList;

    private static final String TAG = "FavoritePeoplePresenter";

    public FavoritePeoplePresenter(FavoritePeopleContract.View view, PreferenceUtil preferenceUtil, Repository repository) {
        this.mRepository = repository;
        this.preferenceUtil = preferenceUtil;
        this.mView = view;
        this.mView.setPresenter(this);
    }



    @Override
    public void loadFavoritePeoples() {
        //mFavList= preferenceUtil.getFavPeopleList();
        mView.onFavoritePeopleLoaded(mFavList);
    }

    @Override
    public void saveFavoritePeople(ContactEntity entity,String userId,String slideID) {

        saveFavPeopleOnSlide(slideID,userId,entity);
        //mView.onFavoritePeopleLoaded(mFavList);

    }

    public void saveFavPeopleOnSlide(String slideID,String userID,ContactEntity entity)
    {
        mRepository.addToSlide(slideID,userID,entity,new DataSource.GetDataCallback<ContactEntity>() {
            @Override
            public void onDataReceived(ContactEntity data) {
                Log.i(TAG,"ContactEntity-onDataReceived "+ data.getName());
                ContactEntity addNewEntity = mFavList.get(mFavList.size()-1);
                mFavList.remove(addNewEntity);
                mFavList.add(data);
                //preferenceUtil.saveFavPeople(mFavList);
                mFavList.add(addNewEntity);
                mView.onFavoritePeopleLoaded(mFavList);

            }

            @Override
            public void onFailed(int code, String message) {
                Log.i(TAG,"ContactEntity-onDataReceived1"+ message);
                Log.d(TAG, "onFailed: ");
            }
        });
    }


    public void fetchFavPeopleFromSlide(String slideID)
    {
        mRepository.fetchFromSlide(slideID,new DataSource.GetDataCallback<ContactsResponse>() {

            @Override
            public void onDataReceived(ContactsResponse data) {
                //mFavList.clear();
                Log.i(TAG,"ContactEntity-onDataReceived ");
                //mFavList = preferenceUtil.getFavPeopleList();
                if(data==null)
                {
                    ContactEntity contactEntity = new ContactEntity();
                    contactEntity.setAndroidId(null);
                    mFavList.add(contactEntity);
                    mView.onFavoritePeopleLoaded(mFavList);
                }else
                {
                    mFavList= data.getContacts();
                    ContactEntity contactEntity = new ContactEntity();
                    contactEntity.setAndroidId(null);
                    mFavList.add(contactEntity);
                    mView.onFavoritePeopleLoaded(mFavList);
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
    public void start() {

       /* call Api for fetching contacts*/
        //fetchFavPeopleFromSlide("5ac610cea45f12274c2fca5a");



    }
}
