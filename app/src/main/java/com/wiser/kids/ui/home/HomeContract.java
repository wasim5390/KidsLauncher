package com.wiser.kids.ui.home;

import com.wiser.kids.BasePresenter;
import com.wiser.kids.BaseView;

import java.util.List;

public class HomeContract {

    interface View extends BaseView<Presenter>{
        void showMessage();
        void slideItemsLoaded(List<String> list);
    }

    interface Presenter extends BasePresenter{
        void getSlideItems();
    }

}
