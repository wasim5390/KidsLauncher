package com.uiu.kids.ui.home.contact.info;

import android.text.TextUtils;

import com.uiu.kids.Constant;
import com.uiu.kids.KidsLauncherApp;
import com.uiu.kids.source.Repository;
import com.uiu.kids.ui.home.contact.ContactEntity;
import com.uiu.kids.ui.home.contact.ContactLoader;
import com.uiu.kids.util.Util;

public class ContactInfoPresenter implements ContactInfoContract.Presenter,Constant {

    private ContactInfoContract.View view;
    private Repository repository;
    private ContactEntity contact;
    private boolean calledFromHome=true;

    public ContactInfoPresenter(ContactInfoContract.View view, ContactEntity contact, Repository repository) {
        this.view = view;
        this.contact = contact;
        this.repository = repository;
        this.view.setPresenter(this);
    }

    @Override
    public void loadContact() {
        if(contact!=null)
        view.onContactLoaded(contact);

    /*    if (!TextUtils.isEmpty(contact.getLookupId()) && !TextUtils.isEmpty(contact.getAndroidId())) {
            if (calledFromHome || !Util.isInternetAvailable()) {
                ContactEntity mContact = ContactLoader.buildContactFromDb(contact.getAndroidId(), contact.getLookupId(), KidsLauncherApp.getInstance());
                view.onContactLoaded(mContact);
            }
        }else{

            ContactEntity mContact=null;
            if(!contact.getPhoneNumbersList().isEmpty())
                mContact = ContactLoader.getInstance(KidsLauncherApp.getInstance()).getContactEntityByNumber(contact.getPhoneNumbersList().get(0).toString(), contact.getName());
            view.onContactLoaded(mContact==null?contact:new ContactEntity());
        }*/
    }

    @Override
    public void getContactType(ContactEntity contactEntity) {
        this.contact = contactEntity;
        if (!TextUtils.isEmpty(contactEntity.getMobileNumber())) {
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
