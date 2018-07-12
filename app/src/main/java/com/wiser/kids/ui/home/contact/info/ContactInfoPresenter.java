package com.wiser.kids.ui.home.contact.info;

import com.wiser.kids.source.Repository;
import com.wiser.kids.ui.home.contact.ContactEntity;

public class ContactInfoPresenter implements ContactInfoContract.Presenter {

    private ContactInfoContract.View view;
    private Repository repository;
    private ContactEntity contact;

    public ContactInfoPresenter(ContactInfoContract.View view,ContactEntity contact, Repository repository) {
        this.view = view;
        this.contact = contact;
        this.repository = repository;
        this.view.setPresenter(this);
    }

    @Override
    public void searchContacts(String contact) {

    }

    @Override
    public void start() {

    }
}
