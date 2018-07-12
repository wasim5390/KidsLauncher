package com.wiser.kids.ui.home.contact.info;

import com.wiser.kids.BasePresenter;
import com.wiser.kids.BaseView;

public class ContactInfoContract {
    interface View extends BaseView<Presenter> {
        void showMessage();
    }

    interface Presenter extends BasePresenter {
        void searchContacts(String contact);
    }
}
