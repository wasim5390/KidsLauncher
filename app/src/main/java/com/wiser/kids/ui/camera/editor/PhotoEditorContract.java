package com.wiser.kids.ui.camera.editor;

import com.wiser.kids.BasePresenter;
import com.wiser.kids.BaseView;
import com.wiser.kids.ui.home.contact.ContactEntity;

import java.io.File;
import java.util.List;

public class PhotoEditorContract {

    interface View extends BaseView<Presenter> {
        void showMessage(String message);
        void onPicShared();
        void onFavPeopleLoaded(List<ContactEntity> contactEntities);
    }

    interface Presenter extends BasePresenter{
        void sharePicToFav(List<String> contacts,int media_type,File file);
        void loadFavPeoples();
    }
}
