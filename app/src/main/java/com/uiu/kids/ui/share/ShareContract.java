package com.uiu.kids.ui.share;

import com.uiu.kids.BasePresenter;
import com.uiu.kids.BaseView;
import com.uiu.kids.ui.home.contact.ContactEntity;

import java.util.List;

public class ShareContract {

    interface View extends BaseView<Presenter>
    {
       void loadPeople(List<ContactEntity> list);
       void showMessage(String msg);
    }
    interface Presenter extends BasePresenter
    {
        void loadRegisterPeople();
    }
}
