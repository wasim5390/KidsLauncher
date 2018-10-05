package com.uiu.kids.ui.slides.people;

import com.uiu.kids.BasePresenter;
import com.uiu.kids.BaseView;
import com.uiu.kids.model.Slide;
import com.uiu.kids.ui.home.contact.ContactEntity;

import java.util.List;

public class FavoritePeopleContract {

    interface View extends BaseView<Presenter> {
        void showMessage(String message);
        void onFavoritePeopleLoaded(List<ContactEntity> list);
        void onFavoritePeopleSaved();
        void slideSerial(int serial,int count);
        void onNewSlideCreated(Slide slide);
        void itemAddedOnNewSlide(Slide newSlide);
    }

    interface Presenter extends BasePresenter {
        void loadFavoritePeoples();
        void saveFavoritePeople(ContactEntity entity, String userId);
        void updateFavoritePeople(ContactEntity entity);
    }
}
