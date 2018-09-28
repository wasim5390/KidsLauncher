package com.uiu.kids.ui.home;

import com.uiu.kids.Constant;
import com.uiu.kids.source.Repository;

import java.util.ArrayList;
import java.util.List;

public class HomePresenter implements HomeContract.Presenter,Constant {

    private HomeContract.View view;
    private Repository repository;

    public HomePresenter(HomeContract.View view, Repository repository) {
        this.view = view;
        this.repository = repository;
        this.view.setPresenter(this);
    }


    @Override
    public void getSlideItems() {
        List<String> slideItemList= new ArrayList<>();
        slideItemList.add(DIALER);
        slideItemList.add(CAMERA);
        slideItemList.add(C_ME);
        slideItemList.add(SOS);
        view.slideItemsLoaded(slideItemList);
    }


    @Override
    public void start() {

    }
}
