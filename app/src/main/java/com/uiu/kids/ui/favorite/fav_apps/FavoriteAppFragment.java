package com.uiu.kids.ui.favorite.fav_apps;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.uiu.kids.BaseFragment;
import com.uiu.kids.Constant;
import com.uiu.kids.R;
import com.uiu.kids.event.NotificationReceiveEvent;
import com.uiu.kids.ui.home.apps.AppsActivity;
import com.uiu.kids.ui.home.apps.AppsEntity;
import com.uiu.kids.util.Util;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static android.app.Activity.RESULT_OK;

public class FavoriteAppFragment extends BaseFragment implements FavoriteAppContract.View, FavoriteAppsAdapter.Callback{

    private static final int REQ_APPS = 987;
    private static final String TAG = "FavoriteAppFragment";
    private FavoriteAppContract.Presenter presenter;
    private FavoriteAppsAdapter adapter;
    @BindView(R.id.rvFavApps)
     RecyclerView rvFavoriteApps;


    public static FavoriteAppFragment newInstance() {
        Bundle args = new Bundle();
        FavoriteAppFragment instance = new FavoriteAppFragment();
        instance.setArguments(args);
        return instance;
    }

    @Override
    public int getID() {
        return R.layout.fragment_favorite_apps;
    }

    @Override
    public void initUI(View view) {
        EventBus.getDefault().register(this);
        setRecyclerView();
        presenter.start();

}



    public void setRecyclerView() {
        adapter = new FavoriteAppsAdapter(getContext(),new ArrayList<>(),this);
        rvFavoriteApps.setLayoutManager(new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false));
        rvFavoriteApps.setHasFixedSize(true);
        rvFavoriteApps.setAdapter(adapter);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Unregister");
        EventBus.getDefault().unregister(this);
    }
    @Override
    public void setPresenter(FavoriteAppContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showNoInternet() {

    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFavoriteAppsLoaded(List<AppsEntity> list) {
         adapter.setSlideItems(list);

    }



    @Override
    public void onSlideItemClick(AppsEntity slideItem) {
        new Handler().postDelayed(() -> {
            if (slideItem.getPackageName() == null) {
                startActivityForResult(new Intent(getContext(), AppsActivity.class), REQ_APPS);
            } else {
                Util.startFromPakage(slideItem.getPkgName(),getContext());
            }

        }, 1);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(NotificationReceiveEvent receiveEvent) {
        if(receiveEvent.getNotificationForSlideType()== Constant.SLIDE_INDEX_FAV_APP){
           /* JSONObject jsonObject = receiveEvent.getNotificationResponse();
           AppsEntity entity =  new Gson().fromJson(jsonObject.toString(),AppsEntity.class);
           if(entity.hasAccess()){
               presenter.updateEntity(entity);
           }*/
           presenter.loadFavApps();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (requestCode == REQ_APPS) {
            if (resultCode == RESULT_OK) {
                AppsEntity entity = (AppsEntity) data.getSerializableExtra(Constant.KEY_SELECTED_APP);
                presenter.saveFavoriteApp(entity);
            }
        }
    }
}
