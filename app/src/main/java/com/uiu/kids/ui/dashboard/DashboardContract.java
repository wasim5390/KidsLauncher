package com.uiu.kids.ui.dashboard;

import android.support.v4.app.Fragment;

import com.uiu.kids.BasePresenter;
import com.uiu.kids.BaseView;
import com.uiu.kids.model.Location;
import com.uiu.kids.model.SlideItem;

import java.util.HashMap;
import java.util.List;

public class DashboardContract {

    interface View extends BaseView<Presenter> {
        void showMessage(String message);
        void onLoginSuccessful();
        void onLoginFailed(String message);
        void onSlidesCreated(List<Fragment> fragments);
        void onSlidesLoaded(List<SlideItem> slideItems);
        void onDirectionsLoaded(List<Location> directions);

    }

    interface Presenter extends BasePresenter {

        void login();
        void createAccount(HashMap<String, Object> params);
        void getUserSlides(String userId);
        void createSlides(List<SlideItem> slides);
        void convertSlidesToFragment(List<SlideItem> slides);
        void updateKidLocation(HashMap<String, Object> params);
        void getKidsDirections(String userId);
    }

}