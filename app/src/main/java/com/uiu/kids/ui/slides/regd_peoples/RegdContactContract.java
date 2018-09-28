package com.uiu.kids.ui.slides.regd_peoples;

import com.uiu.kids.BasePresenter;
import com.uiu.kids.BaseView;
import com.uiu.kids.ui.home.contact.ContactEntity;

import java.util.List;

public class RegdContactContract {
    interface View extends BaseView<Presenter> {
        void showMessage(String message);
        void onContactsLoaded(List<ContactEntity> list);
        void onEmptySearchResult();
    }

    interface Presenter extends BasePresenter {
        void loadContacts();
        void searchContacts(String contact);
    }
}
