package com.wiser.kids.ui.home.contact.info;

import com.wiser.kids.BasePresenter;
import com.wiser.kids.BaseView;
import com.wiser.kids.ui.home.contact.ContactEntity;

public class ContactInfoContract {
    interface View extends BaseView<Presenter> {
        void showMessage();
        void onContactLoaded(ContactEntity contactEntity);
        void onContactTypeMobile();
        void onContactTypeHome();
        void onContactTypeEmail();
    }

    interface Presenter extends BasePresenter {
        void loadContact();
        void getContactType(ContactEntity contactEntity);
    }
}
