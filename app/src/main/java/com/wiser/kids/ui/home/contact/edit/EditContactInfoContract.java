package com.wiser.kids.ui.home.contact.edit;

import com.wiser.kids.BasePresenter;
import com.wiser.kids.BaseView;

public class EditContactInfoContract {
    interface View extends BaseView<Presenter> {
        void showMessage();
        void onNameEntry(boolean value);
        void onPicEntry(boolean value);
        void onMobileNumberEntry(boolean value);
        void onHomeNumberEntry(boolean value);
        void onEmailEntry(boolean value);
    }

    interface Presenter extends BasePresenter {
        void checkContactDetails();

    }
}
