package com.wiser.kids.ui.home.contact.edit;

import android.text.TextUtils;

import com.wiser.kids.Constant;
import com.wiser.kids.source.Repository;
import com.wiser.kids.ui.home.contact.ContactEntity;

public class EditContactInfoPresenter implements EditContactInfoContract.Presenter,Constant{

    private EditContactInfoContract.View view;
    private Repository repository;
    private ContactEntity contact;
    private boolean calledFromHome=true;

    public EditContactInfoPresenter(EditContactInfoContract.View view, ContactEntity contact, Repository repository) {
        this.view = view;
        this.contact = contact;
        this.repository = repository;
        this.view.setPresenter(this);
    }


    @Override
    public void start() {

    }

    @Override
    public void checkContactDetails() {
            view.onNameEntry(!TextUtils.isEmpty(contact.getName()) || contact.getName()!=null);
            view.onPicEntry(!TextUtils.isEmpty(contact.getPhotoUri()) || contact.getPhotoUri()!=null);
            view.onMobileNumberEntry(!TextUtils.isEmpty(contact.getmPhoneNumber()) || contact.getmPhoneNumber() !=null);
            view.onHomeNumberEntry(!TextUtils.isEmpty(contact.getmHomeNumber()) || contact.getmHomeNumber()!=null);
            view.onEmailEntry(!TextUtils.isEmpty(contact.getEmail()) || contact.getEmail()!=null);
    }


}
