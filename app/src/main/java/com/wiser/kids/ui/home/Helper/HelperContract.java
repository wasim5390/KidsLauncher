package com.wiser.kids.ui.home.Helper;

import com.wiser.kids.BasePresenter;
import com.wiser.kids.BaseView;

import java.util.List;

public class HelperContract {

    interface view extends BaseView<Presenter>
    {

        void loadHelperList(List<HelperEntity> list);
        void showMessage(String msg);
    }

    interface Presenter extends BasePresenter
    {
        void addHelper(HelperEntity helper);
        void removeHelper(HelperEntity Helper);

    }
}
