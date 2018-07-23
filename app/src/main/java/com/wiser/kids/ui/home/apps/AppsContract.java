package com.wiser.kids.ui.home.apps;

import android.content.pm.PackageInfo;

import com.wiser.kids.BasePresenter;
import com.wiser.kids.BaseView;

import java.util.List;

public class AppsContract {

    interface View extends BaseView<Presenter>
    {

        void onAppListLoaded(List<AppsEntity> appslist);

    }

    interface Presenter extends BasePresenter
    {

        void loadApps(List<AppsEntity> list);
    }
}
