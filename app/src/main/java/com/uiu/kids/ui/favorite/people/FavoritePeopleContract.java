package com.uiu.kids.ui.favorite.people;

import com.uiu.kids.BasePresenter;
import com.uiu.kids.BaseView;
import com.uiu.kids.ui.home.contact.ContactEntity;

import java.util.List;

public class FavoritePeopleContract {

    interface View extends BaseView<Presenter> {
        void showMessage(String message);
        void onFavoritePeopleLoaded(List<ContactEntity> list);
        void onFavoritePeopleSaved();
    }

    interface Presenter extends BasePresenter {
        void loadFavoritePeoples(String slideId);
        void saveFavoritePeople(ContactEntity entity, String userId);
        void updateFavoritePeople(ContactEntity entity);
    }
}
