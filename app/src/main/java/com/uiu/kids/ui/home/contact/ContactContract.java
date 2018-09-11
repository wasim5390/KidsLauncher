package com.uiu.kids.ui.home.contact;

import com.uiu.kids.BasePresenter;
import com.uiu.kids.BaseView;

import java.util.List;

public class ContactContract {
    interface View extends BaseView<Presenter> {
        void showMessage();
        void onContactsLoaded(List<ContactEntity> list);
        void onEmptySearchResult();
    }

    interface Presenter extends BasePresenter {
        void loadContacts();
        void searchContacts(String contact);
    }
}
