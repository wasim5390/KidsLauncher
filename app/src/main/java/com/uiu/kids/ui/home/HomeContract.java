package com.uiu.kids.ui.home;

import com.uiu.kids.BasePresenter;
import com.uiu.kids.BaseView;

import java.io.File;
import java.util.List;

public class HomeContract {

    interface View extends BaseView<Presenter> {
        void showMessage(String message);
        void slideItemsLoaded(List<String> list);
    }

    interface Presenter extends BasePresenter {
        void getSlideItems();
        void updateProfilePic(File imageFile);
    }

}
