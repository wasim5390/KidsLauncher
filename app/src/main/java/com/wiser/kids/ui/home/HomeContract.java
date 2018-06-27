package com.wiser.kids.ui.home;

import com.wiser.kids.BasePresenter;
import com.wiser.kids.BaseView;

public class HomeContract {

    interface View extends BaseView<Presenter>{
        void showMessage();
        void onLoginSuccessful();
        void loggedIn();

    }

    interface Presenter extends BasePresenter{

        void login();
        void signUp();
        void getAccount();
    }

}
