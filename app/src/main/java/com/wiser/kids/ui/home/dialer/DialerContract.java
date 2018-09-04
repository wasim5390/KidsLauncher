package com.wiser.kids.ui.home.dialer;

import com.wiser.kids.BasePresenter;
import com.wiser.kids.BaseView;

public class DialerContract {
    interface View extends BaseView<DialerContract.Presenter> {
        void showMessage();
    }

    interface Presenter extends BasePresenter {
        void callContact();
    }
}
