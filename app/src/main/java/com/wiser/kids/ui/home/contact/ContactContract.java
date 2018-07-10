package com.wiser.kids.ui.home.contact;

import com.wiser.kids.BasePresenter;
import com.wiser.kids.BaseView;
import com.wiser.kids.ui.home.HomeContract;

import java.util.List;

public class ContactContract {
    interface View extends BaseView<ContactContract.Presenter> {
        void showMessage();
        void onContactsLoaded(List<String> list);
    }

    interface Presenter extends BasePresenter {
        void loadContacts();
    }
}
