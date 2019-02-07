package com.uiu.kids.ui.dashboard;

import android.support.v4.app.Fragment;

import com.uiu.kids.BasePresenter;
import com.uiu.kids.BaseView;
import com.uiu.kids.model.Location;
import com.uiu.kids.model.Setting;
import com.uiu.kids.model.Slide;
import com.uiu.kids.model.User;

import java.util.HashMap;
import java.util.List;

public class DashboardContract {

    interface View extends BaseView<Presenter> {
        void showMessage(String message);
        void onLoginSuccessful(User user);
        void onLoginFailed(String message);
        void onSlidesLoaded(List<Slide> slideItems);
        void onSlidesUpdated(List<Slide> slides);
        void onDirectionsLoaded(List<Location> directions);
        void onSettingsUpdated(Setting setting);
        void phoneNumberExist(HashMap<String, Object> params);

    }

    interface Presenter extends BasePresenter {

        void login();
        void createAccount(HashMap<String, Object> params);
        void getUserSlides(String userId);
        void getInvites(String userId);
        void addSlide(Slide slideItem);
        void loadSlidesFromLocal();
        void removeSlide(Slide slide);
        void removeSlideByType(int slideType);
        void updateKidLocation(HashMap<String, Object> params);
        void notifyBatteryAlert(String kidId);
        void getKidsDirections(String userId);
        void updateKidsSettings(Setting setting);
        void getSettings();
        void updateKidLocationRange(HashMap<String, Object> params);
        void loadUpdatedSlides();
    }

}
