package com.uiu.kids.ui.home.apps;

import com.uiu.kids.BasePresenter;
import com.uiu.kids.BaseView;

import java.util.List;

public class AppsContract {

    interface View extends BaseView<Presenter>
    {
        void onAppListLoaded(List<AppsEntity> appslist);

    }

    interface Presenter extends BasePresenter
    {
        void loadApps(List<AppsEntity> list);
         void searchApps(String app);
    }
}
