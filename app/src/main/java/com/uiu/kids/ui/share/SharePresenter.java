package com.uiu.kids.ui.share;

import com.uiu.kids.model.response.GetFavContactResponse;
import com.uiu.kids.source.DataSource;
import com.uiu.kids.source.Repository;
import com.uiu.kids.ui.home.contact.ContactEntity;
import com.uiu.kids.util.PreferenceUtil;

import java.util.ArrayList;
import java.util.List;

public class SharePresenter implements ShareContract.Presenter {

    public ShareContract.View view;
    public PreferenceUtil preferenceUtil;
    public Repository repository;
    public List<ContactEntity> registerContactList;

    public SharePresenter(ShareContract.View view, PreferenceUtil preferenceUtil, Repository repository) {
        this.view = view;
        this.preferenceUtil = preferenceUtil;
        this.repository = repository;
        view.setPresenter(this);
    }





    @Override
    public void start() {
        registerContactList = new ArrayList<>();
        loadRegisterPeople();
    }

    @Override
    public void loadRegisterPeople() {

        repository.getFavContacts(preferenceUtil.getAccount().getId(), new DataSource.GetDataCallback<GetFavContactResponse>() {
            @Override
            public void onDataReceived(GetFavContactResponse data) {
                if (data != null) {
                    if (data.isSuccess()) {
                        registerContactList.clear();
                        registerContactList.addAll(data.getContactEntityList());
                        view.loadPeople(registerContactList);
                    }
                }


            }

            @Override
            public void onFailed(int code, String message) {

            }
        });

    }

}
