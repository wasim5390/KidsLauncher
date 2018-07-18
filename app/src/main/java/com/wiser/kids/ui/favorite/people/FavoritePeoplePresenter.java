package com.wiser.kids.ui.favorite.people;

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
    public FavoritePeoplePresenter(FavoritePeopleContract.View view, PreferenceUtil preferenceUtil, Repository repository) {
        this.mRepository = repository;
        this.preferenceUtil = preferenceUtil;
        this.mView = view;
        this.mView.setPresenter(this);
    }

    @Override
    public void loadFavoritePeoples() {
        mFavList= preferenceUtil.getFavPeopleList();
        mView.onFavoritePeopleLoaded(mFavList);
    }

    @Override
    public void saveFavoritePeople(ContactEntity entity) {
        ContactEntity addNewEntity = mFavList.get(mFavList.size()-1);
        mFavList.remove(addNewEntity);
        mFavList.add(entity);
        preferenceUtil.saveFavPeople(mFavList);
        mFavList.add(addNewEntity);
        mView.onFavoritePeopleLoaded(mFavList);
    }

    @Override
    public void start() {
        mFavList = preferenceUtil.getFavPeopleList();
        ContactEntity contactEntity = new ContactEntity();
        contactEntity.setAndroidId(null);
        mFavList.add(contactEntity);
        mView.onFavoritePeopleLoaded(mFavList);
    }
}
