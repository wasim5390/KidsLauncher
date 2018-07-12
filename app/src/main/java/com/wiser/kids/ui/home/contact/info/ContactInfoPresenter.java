package com.wiser.kids.ui.home.contact.info;

import android.text.TextUtils;

import com.wiser.kids.Constant;
import com.wiser.kids.KidsLauncherApp;
import com.wiser.kids.source.Repository;
import com.wiser.kids.ui.home.contact.ContactEntity;
import com.wiser.kids.ui.home.contact.ContactLoader;
import com.wiser.kids.util.Util;

public class ContactInfoPresenter implements ContactInfoContract.Presenter,Constant{

    private ContactInfoContract.View view;
    private Repository repository;
    private ContactEntity contact;
    private boolean calledFromHome=true;

    public ContactInfoPresenter(ContactInfoContract.View view,ContactEntity contact, Repository repository) {
        this.view = view;
        this.contact = contact;
        this.repository = repository;
        this.view.setPresenter(this);
    }

    @Override
    public void loadContact() {
        if (!TextUtils.isEmpty(contact.getLookupId()) && !TextUtils.isEmpty(contact.getAndroidId())) {
            if (calledFromHome || !Util.isInternetAvailable(KidsLauncherApp.getInstance())) {
                ContactEntity mContact = ContactLoader.buildContactFromDb(contact.getAndroidId(), contact.getLookupId(), KidsLauncherApp.getInstance());
                view.onContactLoaded(mContact);
            }
        }
    }

    @Override
    public void getContactType(ContactEntity contactEntity) {
        this.contact = contactEntity;
        if (!TextUtils.isEmpty(contactEntity.getmPhoneNumber())) {
            view.onContactTypeMobile();
            return;
        }
        //no mobile number, check home number
        if (!TextUtils.isEmpty(contactEntity.getmHomeNumber())) {
            //user has home number
            view.onContactTypeHome();
            return;
        }
        // no home number, check email
        if (!TextUtils.isEmpty(contactEntity.getEmail())) {
            //has email
            view.onContactTypeEmail();
            return;
        }
    }

    @Override
    public void start() {

    }
}
