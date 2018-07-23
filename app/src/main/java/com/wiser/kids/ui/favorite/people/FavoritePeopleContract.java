package com.wiser.kids.ui.favorite.people;

import com.wiser.kids.BasePresenter;
import com.wiser.kids.BaseView;
import com.wiser.kids.ui.home.contact.ContactEntity;

import java.util.List;

public class FavoritePeopleContract {

    interface View extends BaseView<Presenter>{
        void showMessage(String message);
        void onFavoritePeopleLoaded(List<ContactEntity> list);
        void onFavoritePeopleSaved();
    }

    interface Presenter extends BasePresenter{
        void loadFavoritePeoples();
        void saveFavoritePeople(ContactEntity entity,String id);
    }
}
