package com.wiser.kids.ui.dashboard;

import android.support.v4.app.Fragment;

import com.wiser.kids.Constant;
import com.wiser.kids.KidsLauncherApp;
import com.wiser.kids.model.SlideItem;
import com.wiser.kids.model.request.CreateDefaultSlidesRequest;
import com.wiser.kids.model.response.BaseResponse;
import com.wiser.kids.model.response.GetAccountResponse;
import com.wiser.kids.model.response.GetAllSlidesResponse;
import com.wiser.kids.model.response.GetDirectionsResponse;
import com.wiser.kids.source.DataSource;
import com.wiser.kids.source.Repository;
import com.wiser.kids.ui.SOS.SOSFragment;
import com.wiser.kids.ui.SOS.SOSPresenter;
import com.wiser.kids.ui.favorite.fav_apps.FavoriteAppFragment;
import com.wiser.kids.ui.favorite.fav_apps.FavoriteAppsPresenter;
import com.wiser.kids.ui.favorite.links.FavoriteLinksFragment;
import com.wiser.kids.ui.favorite.links.FavoriteLinksPresenter;
import com.wiser.kids.ui.favorite.people.FavoritePeopleFragment;
import com.wiser.kids.ui.favorite.people.FavoritePeoplePresenter;
import com.wiser.kids.ui.home.HomeFragment;
import com.wiser.kids.ui.home.HomePresenter;
import com.wiser.kids.ui.reminder.ReminderFragment;
import com.wiser.kids.ui.reminder.ReminderPresenter;
import com.wiser.kids.util.PreferenceUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class DashboardPresenter implements DashboardContract.Presenter,Constant {

    private DashboardContract.View view;
    private PreferenceUtil preferenceUtil;
    private Repository repository;
    private int count=0;

    public DashboardPresenter(DashboardContract.View view, PreferenceUtil preferenceUtil, Repository repository) {
        this.view = view;
        this.preferenceUtil = preferenceUtil;
        this.repository = repository;
        this.view.setPresenter(this);
    }

    @Override
    public void login() {

    }

    @Override
    public void createAccount(HashMap<String, Object> params) {
        view.showProgress();
        repository.createAccount(params, new DataSource.GetResponseCallback<GetAccountResponse>() {
            @Override
            public void onSuccess(GetAccountResponse response) {
                view.hideProgress();
                preferenceUtil.saveAccount(response.getUser());
                getUserSlides(response.getUser().getId());

            }

            @Override
            public void onFailed(int code, String message) {
                count++;
            view.onLoginFailed(message);
            view.hideProgress();
            if(count<3)
            createAccount(params);
            }
        });
    }

    @Override
    public void getUserSlides(String userId) {

        view.showProgress();
        repository.getUserSlides(userId, new DataSource.GetDataCallback<GetAllSlidesResponse>() {
            @Override
            public void onDataReceived(GetAllSlidesResponse data) {
                view.onSlidesLoaded(data.getSlide());
            }

            @Override
            public void onFailed(int code, String message) {
            view.showMessage(message);
            view.hideProgress();
            }
        });
    }

    @Override
    public void createSlides(List<SlideItem> slides) {
        CreateDefaultSlidesRequest request = createSlideRequest(SLIDES);
        if(slides.isEmpty()){
            repository.createDefaultSlides(request, new DataSource.GetDataCallback<BaseResponse>() {
                @Override
                public void onDataReceived(BaseResponse data) {
                    if(data.isSuccess()) {
                        getUserSlides(preferenceUtil.getAccount().getId());
                    }else
                        view.showMessage(data.getResponseMsg());
                }

                @Override
                public void onFailed(int code, String message) {
                view.hideProgress();
                }
            });
        }





    }

    @Override
    public void convertSlidesToFragment(List<SlideItem> slides) {
        createFragmentsFromSlide(slides, new ArrayList<>());
    }

    @Override
    public void updateKidLocation(HashMap<String, Object> params) {
    repository.updateKidsLocation(params, new DataSource.GetResponseCallback<BaseResponse>() {
        @Override
        public void onSuccess(BaseResponse response) {

        }

        @Override
        public void onFailed(int code, String message) {

        }
    });
    }

    @Override
    public void getKidsDirections(String userId) {
        repository.getKidDirections(userId, new DataSource.GetDataCallback<GetDirectionsResponse>() {
            @Override
            public void onDataReceived(GetDirectionsResponse data) {
                if(data.getDirectionsList()!=null)
                view.onDirectionsLoaded(data.getDirectionsList());
                else
                    view.showMessage("Tracker not added by parent yet");
            }

            @Override
            public void onFailed(int code, String message) {
            view.showMessage(message);
            }
        });
    }

    private void createFragmentsFromSlide(List<SlideItem> slides,List<Fragment> mSlideFragment){
        if(slides!=null)
        for(SlideItem slideItem: slides) {
            switch (slideItem.getType()){
                case SLIDE_INDEX_HOME:
                    HomeFragment homeFragment = HomeFragment.newInstance();
                    new HomePresenter(homeFragment, repository);
                    mSlideFragment.add(homeFragment);
                    break;
                case SLIDE_INDEX_FAV_PEOPLE:
                    FavoritePeopleFragment favoritePeopleFragment = FavoritePeopleFragment.newInstance();
                    new FavoritePeoplePresenter(favoritePeopleFragment,slideItem.getId(), PreferenceUtil.getInstance(KidsLauncherApp.getInstance()), repository);
                    mSlideFragment.add(favoritePeopleFragment);
                    break;
                case SLIDE_INDEX_FAV_APP:
                    FavoriteAppFragment appsFragment = FavoriteAppFragment.newInstance();
                    new FavoriteAppsPresenter(appsFragment, slideItem,PreferenceUtil.getInstance(KidsLauncherApp.getInstance()), repository);
                    mSlideFragment.add(appsFragment);
                    break;
                case SLIDE_INDEX_FAV_GAMES:
                    break;
                case SLIDE_INDEX_FAV_LINKS:
                    FavoriteLinksFragment linksFragment = FavoriteLinksFragment.newInstance();
                    new FavoriteLinksPresenter(linksFragment,slideItem,PreferenceUtil.getInstance(KidsLauncherApp.getInstance()), repository);
                    mSlideFragment.add(linksFragment);
                    break;
                case SLIDE_INDEX_SOS:
                    SOSFragment sosFragment = SOSFragment.newInstance();
                    new SOSPresenter(sosFragment,slideItem, PreferenceUtil.getInstance(KidsLauncherApp.getInstance()), repository);
                    mSlideFragment.add(sosFragment);
                    break;
                case SLIDE_INDEX_REMINDERS:
                    ReminderFragment reminderFragment = ReminderFragment.newInstance();
                    new ReminderPresenter(reminderFragment,slideItem, PreferenceUtil.getInstance(KidsLauncherApp.getInstance()), repository);
                    mSlideFragment.add(reminderFragment);
                    break;
            }
            }


        view.hideProgress();
        view.onSlidesCreated(mSlideFragment);
    }

    private CreateDefaultSlidesRequest createSlideRequest(String[] slides){
        List<SlideItem> slideItems = new ArrayList<>();
        for(String slide: slides) {
            SlideItem slideItem = new SlideItem();
            slideItem.setUser_id(preferenceUtil.getAccount().getId());
            slideItem.setName(slide);
            slideItem.setType(1);
            slideItem.setSerial(new Random(589656).nextInt());
            slideItems.add(slideItem);
        }
        CreateDefaultSlidesRequest request = new CreateDefaultSlidesRequest();
        request.setDefaultSlides(slideItems);
        return request;
    }





    @Override
    public void start() {
        HomeFragment homeFragment = HomeFragment.newInstance();
        new HomePresenter(homeFragment, repository);
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(homeFragment);
        view.onSlidesCreated(fragments);
    }
}
