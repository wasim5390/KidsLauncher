package com.wiser.kids.ui.home.contact;

import com.wiser.kids.source.Repository;
import com.wiser.kids.util.PreferenceUtil;

import java.util.ArrayList;
import java.util.List;

public class ContactPresenter implements ContactContract.Presenter {

    private ContactContract.View view;
    private ContactLoader contactLoader;
    private Repository repository;
    private List<ContactEntity> contactList;

    public ContactPresenter(ContactContract.View view,ContactLoader contactLoader, Repository repository) {
        this.view = view;
        this.contactLoader = contactLoader;
        this.repository = repository;
        this.view.setPresenter(this);
    }

    @Override
    public void loadContacts() {
        contactList = contactLoader.getDeviceContactsList();
        view.onContactsLoaded(contactList);
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

    }
}
