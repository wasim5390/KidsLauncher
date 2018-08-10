package com.wiser.kids.ui.SOS;

import com.wiser.kids.BasePresenter;
import com.wiser.kids.BaseView;
import com.wiser.kids.ui.home.contact.ContactEntity;

import java.util.List;

public class SOSContract {

    interface  View extends BaseView<Presenter>
    {
        void onSOSListLoaded(List<ContactEntity> sosList);
        void showMessage(String msg);
        void startCallInten(String number,int cod);
        void itemLoadForCall(List<ContactEntity> list);
    }

    interface Presenter extends BasePresenter
    {

        void loadSOSList();
        void saveFavoriteSOS(ContactEntity entity,String userId);
        void updateSOSItem(ContactEntity entity);
        void getItemForCall();
    }
}
