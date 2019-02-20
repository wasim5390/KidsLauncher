package com.uiu.kids.ui.home.contact.info;

import com.uiu.kids.BasePresenter;
import com.uiu.kids.BaseView;
import com.uiu.kids.ui.home.contact.ContactEntity;

import java.io.File;

public class ContactInfoContract {
    interface View extends BaseView<Presenter> {
        void showMessage(String message);
        void onContactLoaded(ContactEntity contactEntity);
        void onContactTypeMobile();
        void onContactTypeHome();
        void onContactTypeEmail();

        void onContactUpdated(ContactEntity contact);
    }

    interface Presenter extends BasePresenter {
        void loadContact();
        void getContactType(ContactEntity contactEntity);
        void updateContactPic(File file);
    }
}
