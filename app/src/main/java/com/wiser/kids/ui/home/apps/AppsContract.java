package com.wiser.kids.ui.home.apps;

import com.wiser.kids.BasePresenter;
import com.wiser.kids.BaseView;

import java.util.List;

public class AppsContract {

    interface View extends BaseView<Presenter>
    {

        void installedAppListLoaded(List<AppsEntity> appslist);

    }

    interface Presenter extends BasePresenter
    {

        void loadInstalledAppsList(List<AppsEntity> list);
    }
}
