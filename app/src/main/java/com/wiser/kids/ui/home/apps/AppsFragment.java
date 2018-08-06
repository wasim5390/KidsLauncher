package com.wiser.kids.ui.home.apps;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.wiser.kids.BaseFragment;
import com.wiser.kids.Constant;
import com.wiser.kids.R;
import com.wiser.kids.util.Util;

import java.io.File;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;


public class AppsFragment extends BaseFragment implements AppsContract.View,AppsListAdapter.onAppItemClick{
    private AppsContract.Presenter presenter;
    private RecyclerView appsListView;
    public AppsListAdapter adapter;
    public List<AppsEntity> appslist;

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
        showProgress();
        presenter.loadApps(getInstalledApplications());
    }

    private void init(View view) {
        appslist = new ArrayList<>();
        appsListView=(RecyclerView) view.findViewById(R.id.appsListView);
        appsListView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        adapter = new AppsListAdapter(appslist,getContext(),this);
        appsListView.setAdapter(adapter);
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
        final PackageManager pm = getActivity().getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN,null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> apps = pm.queryIntentActivities(intent, 0);

        for (int i = 0; i < apps.size(); i++) {
            ResolveInfo p = apps.get(i);
            if (Util.isSystemPackage(p)) {
                PackageManager packageManager = getActivity().getPackageManager();
                String appName = p.loadLabel(packageManager).toString();
                String pkgName= p.activityInfo.packageName;
                if (!pkgName.equals("com.wiser.kids"))
                {
                    res.add(new AppsEntity(appName, pkgName,null));
                }

            }
        }
        return res;
    }

    public List<AppsEntity> getInstalledApplications(){
        List<AppsEntity> res = new ArrayList<AppsEntity>();
        final PackageManager packageManager = getActivity().getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> resInfos = packageManager.queryIntentActivities(intent, 0);
        //using hashset so that there will be no duplicate packages,
        HashSet<String> packageNames = new HashSet<String>(0);

        //getting package names and adding them to the hashset
        for(ResolveInfo resolveInfo : resInfos) {
            packageNames.add(resolveInfo.activityInfo.packageName);
        }

        //now we have unique packages in the hashset, so get their application infos
        //and add them to the arraylist
        for(String packageName : packageNames) {
            try {
                ApplicationInfo packageInfo = packageManager.getApplicationInfo(packageName, 0);
                if (!packageName.equals("com.wiser.kids"))
                res.add(new AppsEntity(packageInfo.loadLabel(packageManager).toString(),packageName,null));
            } catch (PackageManager.NameNotFoundException e) {
                //Do Nothing
            }
        }

        return res;
    }


    @Override
    public void onAppListLoaded(List<AppsEntity> appslist) {
        hideProgress();
       adapter.setAppList(appslist);

    }

    @Override
    public void onAppSelected(AppsEntity appsEntity) {

        Bitmap bitmapImg=Util.drawablToBitmap(appsEntity.getIcon(getContext()));
        File imgFile=Util.bitmapToFile(bitmapImg,"applicatopnIcon",getContext());
        AppsEntity entity=appsEntity;
        entity.setAppIcon(imgFile);
        Intent i = getActivity().getIntent();
        entity.setFlagEmptylist(false);

        i.putExtra(Constant.KEY_SELECTED_APP,(Serializable) entity);

        getActivity().setResult(Activity.RESULT_OK, i);
        getActivity().finish();

    }

}