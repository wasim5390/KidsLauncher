package com.wiser.kids.ui.home.apps;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wiser.kids.BaseFragment;
import com.wiser.kids.R;

import java.util.ArrayList;
import java.util.List;


public class AppsFragment extends BaseFragment implements AppsContract.View,AppsListAdapter.onAppItemClick{
    private AppsContract.Presenter presenter;
    private RecyclerView appsListView;
    public AppsListAdapter adapter;

    public static AppsFragment newInstance()
    {
        Bundle args=new Bundle();
        AppsFragment instance=new AppsFragment();
        instance.setArguments(args);
        return instance;
    }


    @Override
    public int getID() {
        return R.layout.fragment_apps;
    }

    @Override
    public void initUI(View view) {

        init(view);
        presenter.loadInstalledAppsList(getInstalledApps());
    }

    private void init(View view) {

        appsListView=(RecyclerView) view.findViewById(R.id.appsListView);
    }

    @Override
    public void setPresenter(AppsContract.Presenter presenter) {
        this.presenter=presenter;
    }

    @Override
    public void showNoInternet() {

    }

    private List<AppsEntity> getInstalledApps() {
        List<AppsEntity> res = new ArrayList<AppsEntity>();
        List<PackageInfo> packs = getContext().getPackageManager().getInstalledPackages(0);
        for (int i = 0; i < packs.size(); i++) {
            PackageInfo p = packs.get(i);
            if (isSystemPackage(p) == false) {
                String appName = p.applicationInfo.loadLabel(getActivity().getPackageManager()).toString();
                Drawable icon = p.applicationInfo.loadIcon(getActivity().getPackageManager());
                String pkgName= p.applicationInfo.packageName.toString();
                if (!pkgName.equals("com.wiser.kids"))
                {
                    res.add(new AppsEntity(appName, icon, pkgName));
                }

            }
        }
        return res;
    }

    private boolean isSystemPackage(PackageInfo pkgInfo) {
        return ((pkgInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) ? true : false;
    }

    @Override
    public void installedAppListLoaded(List<AppsEntity> appslist) {

        appsListView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        adapter = new AppsListAdapter(appslist,getContext(),this);
        appsListView.setAdapter(adapter);

    }

    @Override
    public void onAppSelected(AppsEntity appsEntity) {

        Log.e("pkage name ",appsEntity.getPkgName());
        if(appsEntity.getPkgName().equals("com.google.android.instantapps.supervisor")){
            final Uri marketUri = Uri.parse("https://play.google.com/store/apps?id=" + appsEntity.getPkgName());
            startActivity(new Intent(Intent.ACTION_VIEW, marketUri));
            }
            else {
            Intent LaunchIntent = getActivity().getPackageManager().getLaunchIntentForPackage(appsEntity.getPkgName());
            if(LaunchIntent!=null) {
                startActivity(LaunchIntent);
            }
            }
    }

}
