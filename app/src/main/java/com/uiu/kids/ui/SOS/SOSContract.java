package com.uiu.kids.ui.SOS;

import com.uiu.kids.BasePresenter;
import com.uiu.kids.BaseView;
import com.uiu.kids.ui.home.contact.ContactEntity;

import java.util.List;

public class SOSContract {

    interface  View extends BaseView<Presenter>
    {
        void onSOSListLoaded(List<ContactEntity> sosList);
        void showMessage(String msg);
        void startCallInten(String number, int cod);
        void itemLoadForCall(List<ContactEntity> list);
    }

    interface Presenter extends BasePresenter
    {

        void loadSOSList();
        void saveFavoriteSOS(ContactEntity entity, String userId);
        void updateSOSItem(ContactEntity entity);
        void getItemForCall();
        void generateAccessedList(List<ContactEntity> list);
    }
}
