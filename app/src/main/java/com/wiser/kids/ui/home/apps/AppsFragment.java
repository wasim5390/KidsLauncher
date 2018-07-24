package com.wiser.kids.ui.home.apps;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
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
        presenter.loadApps(getInstalledApps());
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
            if (Util.isSystemPackage(p) == false) {
                String appName = p.applicationInfo.loadLabel(getActivity().getPackageManager()).toString();
                String pkgName= p.applicationInfo.packageName.toString();

//                Bitmap bitmapImg=Util.drawablToBitmap(p.applicationInfo.loadIcon(getActivity().getPackageManager()));
//                File imgFile=Util.bitmapToFile(bitmapImg,"applicatopnIcon",getContext());

                if (!pkgName.equals("com.wiser.kids"))
                {

                    res.add(new AppsEntity(appName, pkgName,null));
                }

            }
        }
        return res;
    }


    @Override
    public void onAppListLoaded(List<AppsEntity> appslist) {

        appsListView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        adapter = new AppsListAdapter(appslist,getContext(),this);
        appsListView.setAdapter(adapter);

    }

    @Override
    public void onAppSelected(AppsEntity appsEntity) {


        AppsEntity entity=appsEntity;
        Intent i = getActivity().getIntent();
        entity.setFlagEmptylist(false);

        i.putExtra(Constant.KEY_SELECTED_APP,(Serializable) entity);

        getActivity().setResult(Activity.RESULT_OK, i);
        getActivity().finish();

    }

}