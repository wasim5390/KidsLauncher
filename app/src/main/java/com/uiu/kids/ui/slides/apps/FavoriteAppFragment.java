package com.uiu.kids.ui.slides.apps;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.uiu.kids.BaseFragment;
import com.uiu.kids.Constant;
import com.uiu.kids.R;
import com.uiu.kids.event.notification.NotificationReceiveEvent;
import com.uiu.kids.event.SlideCreateEvent;
import com.uiu.kids.model.Slide;
import com.uiu.kids.ui.home.apps.AppsActivity;
import com.uiu.kids.ui.home.apps.AppsEntity;
import com.uiu.kids.util.Util;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

public class FavoriteAppFragment extends BaseFragment implements FavoriteAppContract.View, FavoriteAppsAdapter.Callback{

    private static final int REQ_APPS = 987;
    private static final String TAG = "FavoriteAppFragment";
    private FavoriteAppContract.Presenter presenter;
    private FavoriteAppsAdapter adapter;
    @BindView(R.id.rvFavApps)
    RecyclerView rvFavoriteApps;

    @BindView(R.id.progressBar)
    ContentLoadingProgressBar progressBar;
    int currentPage, count;
    Slide slide;

    public static FavoriteAppFragment newInstance(Slide slide) {
        Bundle args = new Bundle();
        args.putSerializable("Slide",slide);
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
        slide = (Slide) getArguments().getSerializable("Slide");
        setRecyclerView();
        if(presenter!=null)
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
    public void setUserVisibleHint(boolean isFragmentVisible_) {
        super.setUserVisibleHint(true);
        if (this.isVisible()) {
// we check that the fragment is becoming visible
            if (isFragmentVisible_ ) {
                progressBar.show();
                presenter.start();
            }
        }
    }

    @Override
    public void setPresenter(FavoriteAppContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showNoInternet() {
        Toast.makeText(mBaseActivity, "No internet connection!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void slideSerial(int serial,int count) {
        serial++;
        this.count = count;
        this.currentPage = serial;
        String pageNum = serial+"/"+count;
        ((TextView)getView().findViewById(R.id.tvFavAppsTitle)).setText(getString(R.string.favorite_apps)+" ("+pageNum+")");
    }

    @Override
    public void itemAddedOnNewSlide(Slide slide) {
        EventBus.getDefault().postSticky(new SlideCreateEvent(slide));
    }


    @Override
    public void showMessage(String message) {
        try {
            Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();
        }catch (Exception e){

        }

    }

    @Override
    public void hideProgress() {
        progressBar.hide();
    }

    @Override
    public void showProgress() {
        progressBar.show();
    }

    @Override
    public void onFavoriteAppsLoaded(List<AppsEntity> list) {
        adapter.setSlideItems(list);
        progressBar.hide();
    }

    @Override
    public void onNewSlideCreated(Slide slide) {

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
    @OnClick(R.id.btnAddNew)
    public void addNew(){
        if(presenter.canAddOnSlide())
        startActivityForResult(new Intent(getContext(), AppsActivity.class), REQ_APPS);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(NotificationReceiveEvent receiveEvent) {
        if(receiveEvent.getNotificationForSlideType()== Constant.SLIDE_INDEX_FAV_APP
                && receiveEvent.isSlideUpdate()
                ){
           /* JSONObject jsonObject = receiveEvent.getNotificationResponse();
           AppsEntity entity =  new Gson().fromJson(jsonObject.toString(),AppsEntity.class);
           if(entity.hasAccess()){
               presenter.updateEntity(entity);
           }*/

            presenter.start();
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
