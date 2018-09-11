package com.uiu.kids.ui.favorite.regd_peoples;

import com.uiu.kids.model.response.GetFavContactResponse;
import com.uiu.kids.source.DataSource;
import com.uiu.kids.source.Repository;
import com.uiu.kids.ui.home.contact.ContactEntity;
import com.uiu.kids.util.PreferenceUtil;

import java.util.ArrayList;
import java.util.List;

public class RegdContactPresenter implements RegdContactContract.Presenter {

    private RegdContactContract.View view;
    private Repository repository;
    private List<ContactEntity> contactList;
    private PreferenceUtil preferenceUtil;

    public RegdContactPresenter(RegdContactContract.View view, PreferenceUtil preferenceUtil, Repository repository) {
        this.view = view;
        this.repository = repository;
        this.preferenceUtil = preferenceUtil;
        this.view.setPresenter(this);
    }

    @Override
    public void loadContacts() {
        repository.getRegdContacts(preferenceUtil.getAccount().getId(), new DataSource.GetDataCallback<GetFavContactResponse>() {
            @Override
            public void onDataReceived(GetFavContactResponse data) {
                if(data.isSuccess())
                    contactList.addAll(data.getRegdContactEntityList());
                view.onContactsLoaded(contactList);
            }

            @Override
            public void onFailed(int code, String message) {
                view.showMessage(message);
            }
        });


    }

    @Override
    public void searchContacts(String contact) {
        if (!contact.trim().isEmpty()) {
            List<ContactEntity> searchResults = new ArrayList<>();
            for (int i = 0; i < contactList.size(); i++) {
                if (contactList.get(i).getName().toLowerCase().contains(contact.toLowerCase())) {
                    searchResults.add(contactList.get(i));
                }
            }
            if(!searchResults.isEmpty())
            view.onContactsLoaded(searchResults);
            else
                view.onEmptySearchResult();
        } else {
            if(!contactList.isEmpty())
            view.onContactsLoaded(contactList);
        }
    }

    @Override
    public void start() {
        contactList = new ArrayList<>();
        loadContacts();
    }
}
