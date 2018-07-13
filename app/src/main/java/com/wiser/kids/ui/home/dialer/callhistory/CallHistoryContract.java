package com.wiser.kids.ui.home.dialer.callhistory;

import android.content.Context;

import com.wiser.kids.BasePresenter;
import com.wiser.kids.BaseView;

public class CallHistoryContract {

    interface View extends BaseView<CallHistoryContract.Presenter>
    {
        void listLoaded(CallHistoryAdapter adapter);

    }

    interface Presenter extends BasePresenter
    {
      void listLoad(Context context);
    }

}
