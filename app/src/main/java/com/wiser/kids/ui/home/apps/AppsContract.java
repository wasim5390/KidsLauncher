package com.wiser.kids.ui.home.apps;

import android.content.pm.PackageInfo;

import com.wiser.kids.BasePresenter;
import com.wiser.kids.BaseView;

import java.util.List;

public class AppsContract {

    interface View extends BaseView<Presenter>
    {

        void installedAppListLoaded(List<AppsEntity> appslist, List<PackageInfo> packageInfoList);

    }

    interface Presenter extends BasePresenter
    {

        void loadInstalledAppsList(List<AppsEntity> list, List<PackageInfo> packageInfoList);
    }
}
