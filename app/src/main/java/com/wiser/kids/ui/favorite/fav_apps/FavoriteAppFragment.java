package com.wiser.kids.ui.favorite.fav_apps;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.wiser.kids.BaseFragment;
import com.wiser.kids.Constant;
import com.wiser.kids.R;
import com.wiser.kids.ui.favorite.people.FavoritePeopleAdapter;
import com.wiser.kids.ui.home.apps.AppsActivity;
import com.wiser.kids.ui.home.apps.AppsEntity;
import com.wiser.kids.ui.home.contact.ContactActivity;
import com.wiser.kids.ui.home.contact.ContactEntity;
import com.wiser.kids.ui.home.contact.info.ContactInfoActivity;

import java.util.List;

import static android.app.Activity.RESULT_OK;

public class FavoriteAppFragment extends BaseFragment implements FavoriteAppContract.View,FavoriteAppsAdapter.Callback{

    private static final int REQ_APPS =987 ;
    private FavoriteAppContract.Presenter presenter;
    private FavoriteAppsAdapter adapter;
    private RecyclerView rvFavoriteApps;


    public static FavoriteAppFragment newInstance()
    {
        Bundle args=new Bundle();
        FavoriteAppFragment instance=new FavoriteAppFragment();
        instance.setArguments(args);
        return instance;
    }

    @Override
    public int getID() {
        return R.layout.fragment_favorite_apps;
    }

    @Override
    public void initUI(View view) {

        init(view);
        setRecyclerView();
        presenter.start();
    }
    public void init(View view)
    {
        rvFavoriteApps=(RecyclerView) view.findViewById(R.id.rvFavApps);
    }
    public void setRecyclerView(){
        adapter = new FavoriteAppsAdapter(getContext(),this);
        rvFavoriteApps.setLayoutManager(new GridLayoutManager(getContext(),2,GridLayoutManager.VERTICAL,false));
        rvFavoriteApps.setHasFixedSize(true);
        rvFavoriteApps.setAdapter(adapter);
    }

    @Override
    public void setPresenter(FavoriteAppContract.Presenter presenter) {
        this.presenter=presenter;
    }

    @Override
    public void showNoInternet() {

    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public void onFavoriteAppsLoaded(List<AppsEntity> list) {
       // adapter.setSlideItems(list);
    }

    @Override
    public void onFavoriteAppAdded() {

    }


    @Override
    public void onSlideItemClick(AppsEntity slideItem) {
        new Handler().postDelayed(() -> {
            if(slideItem.getName()==null)
            {
                startActivityForResult(new Intent(getContext(), AppsActivity.class),REQ_APPS);
            }
            else {
                Toast.makeText(getContext(),"You don't have access yet ",Toast.LENGTH_SHORT).show();
            }
        },1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==REQ_APPS){
            if(resultCode==RESULT_OK){
                AppsEntity entity=(AppsEntity) data.getSerializableExtra(Constant.KEY_SELECTED_APP);
                presenter.saveFavoriteApp(entity);
            }
        }
    }
}
