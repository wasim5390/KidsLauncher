package com.uiu.kids.ui.slides.safe_places;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.uiu.kids.BaseFragment;
import com.uiu.kids.Constant;
import com.uiu.kids.R;
import com.uiu.kids.event.SlideEvent;
import com.uiu.kids.event.notification.NotificationReceiveEvent;
import com.uiu.kids.model.Location;
import com.uiu.kids.model.Slide;
import com.uiu.kids.util.PreferenceUtil;
import com.uiu.kids.util.Util;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class SafePlacesFragment extends BaseFragment implements SafePlacesAdapter.Callback,
        SafePlacesContract.View
{
    private static final int REQ_CONTACT = 0x101;
    public static String TAG ="SafePlacesFragment";

    @BindView(R.id.rvFavApps)
    RecyclerView recyclerView;
    @BindView(R.id.tvFavAppsTitle)
    TextView title;
    @BindView(R.id.progressBar)
    ContentLoadingProgressBar progressBar;

    private SafePlacesAdapter adapter;
    SafePlacesContract.Presenter mPresenter;

    public static SafePlacesFragment newInstance() {
        Bundle args = new Bundle();
        SafePlacesFragment homeFragment = new SafePlacesFragment();
        homeFragment.setArguments(args);
        return homeFragment;
    }

    @Override
    public int getID() {
        return R.layout.fragment_favorite_apps;
    }

    @Override
    public void initUI(View view) {
        EventBus.getDefault().register(this);
        title.setText(getString(R.string.home_title_safe_places));
        title.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.directions_slide_title_colorDark));
        view.findViewById(R.id.btnAddNew).setVisibility(View.GONE);
        setRecyclerView();
        if(mPresenter!=null)
            mPresenter.start();
    }
    public void setRecyclerView(){
        adapter = new SafePlacesAdapter(getContext(),this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false ));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
    }

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
                mPresenter.start();
            }
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
    public void onSlideItemClick(Location slideItem) {
        new Handler().postDelayed(() -> {

        },1);

    }


    @Override
    public void slideSerial(int serial,int count) {
        serial++;
        String pageNum = serial+"/"+count;
        ((TextView)getView().findViewById(R.id.tvFavAppsTitle)).setText(getString(R.string.home_title_safe_places)+" ("+pageNum+")");
    }

    @Override
    public void onSlideCreated(Slide slide) {
        EventBus.getDefault().post(new SlideEvent(slide.getType(),true));
    }

    @Override
    public void onSlideRemoved(Slide slide) {
        EventBus.getDefault().post(new SlideEvent(slide.getType(),false));
    }


    @Override
    public void showMessage(String message) {
        Toast.makeText(mBaseActivity, message, Toast.LENGTH_LONG).show();
       // Util.showDialog(getActivity(),message, ContextCompat.getColor(getContext(),R.color.contacts_bg));
    }

    @Override
    public void onDirectionsLoaded(List<Location> list) {
        adapter.setSlideItems(list);
        hideProgress();
    }




    @Override
    public void setPresenter(SafePlacesContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showNoInternet() {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(NotificationReceiveEvent receiveEvent) {
        if(receiveEvent.getNotificationForSlideType()== Constant.SLIDE_INDEX_DIRECTIONS && receiveEvent.isSlideUpdate()){
            if(mPresenter!=null)
                mPresenter.start();
        }

    }



}
