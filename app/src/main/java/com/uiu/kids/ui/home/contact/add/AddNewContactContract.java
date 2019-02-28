package com.uiu.kids.ui.home.contact.add;

import com.uiu.kids.BasePresenter;
import com.uiu.kids.BaseView;
import com.uiu.kids.ui.home.contact.ContactEntity;

import java.io.File;

public class AddNewContactContract{

    interface View extends BaseView<Presenter> {
        void showMessage(String message);
        void onContactCreated(ContactEntity contact);
        void onMobileEmpty();
        void onFirstNameEmpty();
    }

    interface Presenter extends BasePresenter {

        void createContact(String name,String mobileNumber);
        void saveContactImage(File file);
    }}