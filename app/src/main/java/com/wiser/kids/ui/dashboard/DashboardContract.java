package com.wiser.kids.ui.dashboard;

import android.support.v4.app.Fragment;

import com.wiser.kids.BasePresenter;
import com.wiser.kids.BaseView;

import java.util.List;

public class DashboardContract {

    interface View extends BaseView<Presenter>{
        void showMessage();
        void onLoginSuccessful();
        void onSlidesCreated(List<Fragment> fragments);

    }

    interface Presenter extends BasePresenter{

        void login();
        void createSlides();
    }

}
