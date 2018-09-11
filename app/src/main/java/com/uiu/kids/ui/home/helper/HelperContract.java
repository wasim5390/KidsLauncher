package com.uiu.kids.ui.home.helper;

import com.uiu.kids.BasePresenter;
import com.uiu.kids.BaseView;

import java.util.List;

public class HelperContract {

    interface view extends BaseView<Presenter>
    {

        void loadHelperList(List<HelperEntity> list);
        void onHelpersSaved();
        void onPrimarySelection(boolean isPrimay);
        void showMessage(String msg);
    }

    interface Presenter extends BasePresenter
    {
        void updateHelpers(List<HelperEntity> selectedHelpers);
        void savePrimaryHelper(String helperId);
    }
}
