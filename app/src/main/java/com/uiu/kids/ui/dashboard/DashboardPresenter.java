package com.uiu.kids.ui.dashboard;

import android.support.v4.app.Fragment;

import com.uiu.kids.Constant;
import com.uiu.kids.KidsLauncherApp;
import com.uiu.kids.model.Slide;
import com.uiu.kids.model.request.CreateDefaultSlidesRequest;
import com.uiu.kids.model.response.BaseResponse;
import com.uiu.kids.model.response.GetAccountResponse;
import com.uiu.kids.model.response.GetAllSlidesResponse;
import com.uiu.kids.model.response.GetDirectionsResponse;
import com.uiu.kids.source.DataSource;
import com.uiu.kids.source.Repository;
import com.uiu.kids.ui.slides.sos.SOSFragment;
import com.uiu.kids.ui.slides.sos.SOSPresenter;
import com.uiu.kids.ui.slides.apps.FavoriteAppFragment;
import com.uiu.kids.ui.slides.apps.FavoriteAppsPresenter;
import com.uiu.kids.ui.slides.links.FavoriteLinksFragment;
import com.uiu.kids.ui.slides.links.FavoriteLinksPresenter;
import com.uiu.kids.ui.slides.people.FavoritePeopleFragment;
import com.uiu.kids.ui.slides.people.FavoritePeoplePresenter;
import com.uiu.kids.ui.home.HomeFragment;
import com.uiu.kids.ui.home.HomePresenter;
import com.uiu.kids.ui.invitation.InviteListFragment;
import com.uiu.kids.ui.invitation.InviteListPresenter;
import com.uiu.kids.ui.slides.reminder.ReminderFragment;
import com.uiu.kids.ui.slides.reminder.ReminderPresenter;
import com.uiu.kids.util.PreferenceUtil;

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
    public void createSlides(List<Slide> slides) {
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
    public void convertSlidesToFragment(List<Slide> slides) {
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

    private void createFragmentsFromSlide(List<Slide> slides, List<Fragment> mSlideFragment){
        if(slides!=null)
            for(Slide slideItem: slides) {
                switch (slideItem.getType()){
                    case SLIDE_INDEX_INVITE:
                        InviteListFragment inviteListFragment = InviteListFragment.newInstance();
                        new InviteListPresenter(inviteListFragment, repository,preferenceUtil.getAccount());
                        mSlideFragment.add(inviteListFragment);
                        break;
                    case SLIDE_INDEX_HOME:
                        HomeFragment homeFragment = HomeFragment.newInstance();
                        new HomePresenter(homeFragment, repository);
                        mSlideFragment.add(homeFragment);
                        break;
                    case SLIDE_INDEX_FAV_PEOPLE:
                        FavoritePeopleFragment favoritePeopleFragment = FavoritePeopleFragment.newInstance();
                        new FavoritePeoplePresenter(favoritePeopleFragment,slideItem, PreferenceUtil.getInstance(KidsLauncherApp.getInstance()), repository);
                        mSlideFragment.add(favoritePeopleFragment);
                        break;
                    case SLIDE_INDEX_FAV_APP:
                        FavoriteAppFragment appsFragment = FavoriteAppFragment.newInstance();
                        new FavoriteAppsPresenter(appsFragment, slideItem, PreferenceUtil.getInstance(KidsLauncherApp.getInstance()), repository);
                        mSlideFragment.add(appsFragment);
                        break;
                    case SLIDE_INDEX_FAV_GAMES:
                        break;
                    case SLIDE_INDEX_FAV_LINKS:
                        FavoriteLinksFragment linksFragment = FavoriteLinksFragment.newInstance();
                        new FavoriteLinksPresenter(linksFragment,slideItem, PreferenceUtil.getInstance(KidsLauncherApp.getInstance()), repository);
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

//        NotificationFragment notificationFragment = NotificationFragment.newInstance();
//        new NotificationPresenter(notificationFragment, PreferenceUtil.getInstance(KidsLauncherApp.getInstance()), repository);
//        mSlideFragment.add(notificationFragment);
        view.hideProgress();
        view.onSlidesCreated(mSlideFragment);
    }
    private CreateDefaultSlidesRequest createSlideRequest(String[] slides){
        List<Slide> slideItems = new ArrayList<>();
        for(String slide: slides) {
            Slide slideItem = new Slide();
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
