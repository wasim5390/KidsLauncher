package com.uiu.kids.ui.home.apps;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.AutoCompleteTextView;

import com.uiu.kids.BaseFragment;
import com.uiu.kids.Constant;
import com.uiu.kids.R;
import com.uiu.kids.util.Util;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import butterknife.BindView;


public class AppsFragment extends BaseFragment implements AppsContract.View,AppsListAdapter.onAppItemClick,
        SearchView.OnQueryTextListener
{
    private AppsContract.Presenter presenter;
    @BindView(R.id.appsListView)
    public RecyclerView appsListView;
    public AppsListAdapter adapter;
    public List<AppsEntity> appslist;
    @BindView(R.id.searchView)
    SearchView searchView;
    @BindView(R.id.progressBar)
    ContentLoadingProgressBar progressBar;

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
        appslist = new ArrayList<>();
        appsListView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        adapter = new AppsListAdapter(appslist,getContext(),this);
        appsListView.setAdapter(adapter);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(this);

        try {
            AutoCompleteTextView searchTextView = searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
            searchTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.grey_dark));
        }catch (Exception e){}
        progressBar.show();
        presenter.loadApps(getInstalledApplications());

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
                    res.add(new AppsEntity(appName, pkgName));
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
                    res.add(new AppsEntity(packageInfo.loadLabel(packageManager).toString(),packageName));
            } catch (PackageManager.NameNotFoundException e) {
                //Do Nothing
            }
        }

        return res;
    }


    @Override
    public void onAppListLoaded(List<AppsEntity> appslist) {
        progressBar.hide();
        Collections.sort(appslist, (obj1, obj2) -> {
            // ## Ascending order
            return obj1.getName().compareToIgnoreCase(obj2.getName()); // To compare string values

        });
        adapter.setAppList(appslist);

    }

    @Override
    public void onAppSelected(AppsEntity appsEntity) {
        new Handler().post(() -> {
            Drawable drawable = appsEntity.getIcon(getContext());
            Bitmap bm = Util.drawableToBitmap(drawable);
            String icon = Util.bitmapToBase64(bm);
            AppsEntity entity=appsEntity;
            entity.setAppIcon(icon);
            Intent i = getActivity().getIntent();
            entity.setFlagEmptylist(false);
            i.putExtra(Constant.KEY_SELECTED_APP,(Serializable) entity);
            getActivity().setResult(Activity.RESULT_OK, i);
            getActivity().finish();
        });


    }
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        presenter.searchApps(newText);
        return true;
    }
}