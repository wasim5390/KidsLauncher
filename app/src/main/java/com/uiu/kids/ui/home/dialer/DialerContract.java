package com.uiu.kids.ui.home.dialer;

import com.uiu.kids.BasePresenter;
import com.uiu.kids.BaseView;

public class DialerContract {
    interface View extends BaseView<Presenter> {
        void showMessage();
    }

    interface Presenter extends BasePresenter {
        void callContact();
    }
}
