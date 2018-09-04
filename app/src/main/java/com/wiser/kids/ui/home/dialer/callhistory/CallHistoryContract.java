package com.wiser.kids.ui.home.dialer.callhistory;

import com.wiser.kids.BasePresenter;
import com.wiser.kids.BaseView;
import com.wiser.kids.util.CallRecord;

public class CallHistoryContract {

    interface View extends BaseView<CallHistoryContract.Presenter>
    {
        void callLogLoaded(CallHistoryAdapter adapter);
        void onContactSelected(CallRecord callRecord);

    }

    interface Presenter extends BasePresenter
    {
      void callLogLoading(CallHistoryAdapter adapter);
    }

}
