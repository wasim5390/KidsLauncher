package com.wiser.kids.ui.home;

import android.os.Bundle;
import android.view.View;

import com.wiser.kids.BaseFragment;
import com.wiser.kids.R;


public class HomeFragment extends BaseFragment implements HomeContract.View{

    HomeContract.Presenter presenter;
    @Override
    public int getID() {
        return R.layout.fragment_home;
    }

    @Override
    public void initUI(View view) {

    }

    public static HomeFragment newInstance() {
        Bundle args = new Bundle();
        HomeFragment homeFragment = new HomeFragment();
        homeFragment.setArguments(args);
        return homeFragment;
    }

    @Override
    public void showMessage() {

    }

    @Override
    public void onLoginSuccessful() {

    }


    @Override
    public void setPresenter(HomeContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showNoInternet() {

    }
}
