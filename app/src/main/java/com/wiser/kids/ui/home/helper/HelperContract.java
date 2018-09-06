package com.wiser.kids.ui.home.helper;

import com.wiser.kids.BasePresenter;
import com.wiser.kids.BaseView;

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
