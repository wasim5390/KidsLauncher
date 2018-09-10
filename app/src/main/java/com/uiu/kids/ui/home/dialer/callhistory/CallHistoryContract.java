package com.uiu.kids.ui.home.dialer.callhistory;

import com.uiu.kids.BasePresenter;
import com.uiu.kids.BaseView;
import com.uiu.kids.util.CallRecord;

public class CallHistoryContract {

    interface View extends BaseView<Presenter>
    {
        void callLogLoaded(CallHistoryAdapter adapter);
        void onContactSelected(CallRecord callRecord);

    }

    interface Presenter extends BasePresenter
    {
      void callLogLoading(CallHistoryAdapter adapter);
    }

}
