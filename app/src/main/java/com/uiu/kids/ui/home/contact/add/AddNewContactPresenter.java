package com.uiu.kids.ui.home.contact.add;

import android.net.Uri;
import android.text.TextUtils;

import com.uiu.kids.Constant;
import com.uiu.kids.source.Repository;
import com.uiu.kids.ui.home.contact.ContactEntity;
import com.uiu.kids.util.PreferenceUtil;
import com.uiu.kids.util.Util;

import java.io.File;

public class AddNewContactPresenter implements AddNewContactContract.Presenter, Constant {

    private AddNewContactContract.View view;
    private Repository repository;
    private ContactEntity contact;
    private PreferenceUtil preferenceUtil;
    private File imageFile;


    public AddNewContactPresenter(AddNewContactContract.View view,PreferenceUtil preferenceUtil, Repository repository) {
        this.view = view;
        this.repository = repository;
        this.preferenceUtil = preferenceUtil;
        this.contact = new ContactEntity();
        this.view.setPresenter(this);
    }



    @Override
    public void start() {

    }

    @Override
    public void createContact(String firstName, String lastName, String mobileNumber) {
        if(TextUtils.isEmpty(firstName)) {
            view.showMessage("Please enter First name");
            view.onFirstNameEmpty();
            return;
        }
        if(TextUtils.isEmpty(lastName)) {
            view.showMessage("Please enter Last name");
            view.onLastNameEmpty();
            return;
        }
        if(TextUtils.isEmpty(mobileNumber)) {
            view.showMessage("Please provide valid mobile number");
            view.onMobileEmpty();
            return;
        }
        if(imageFile==null || !imageFile.exists()) {
            view.showMessage("Please select Contact Photo!");
            return;
        }
        contact.setFirstName(firstName);
        contact.setLastName(lastName);
        contact.setName(contact.getName());
        contact.setMobileNumber(mobileNumber);
        contact.setPhotoUri(Uri.fromFile(imageFile).toString());
        view.onContactCreated(contact);

    }

    @Override
    public void saveContactImage(File file) {
        this.imageFile = file;
    }
}
