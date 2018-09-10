package com.uiu.kids.ui.message;

import com.uiu.kids.BasePresenter;
import com.uiu.kids.BaseView;
import com.uiu.kids.ui.home.contact.ContactEntity;

import java.io.File;
import java.util.List;

public class MessageContract  {

    interface View extends BaseView<Presenter>
    {
       void loadPeople(List<ContactEntity> list);
       void showMessage(String msg);
       void onFileShared();
    }
    interface Presenter extends BasePresenter
    {
        void loadRegisterPeople();
        void shareFileToContact(List<ContactEntity> list, File file, int type);
    }
}
