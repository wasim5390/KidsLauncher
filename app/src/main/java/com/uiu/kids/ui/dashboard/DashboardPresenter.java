package com.uiu.kids.ui.dashboard;

import android.support.v4.app.Fragment;

import com.uiu.kids.Constant;
import com.uiu.kids.KidsLauncherApp;
import com.uiu.kids.model.Slide;
import com.uiu.kids.model.User;
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
import com.uiu.kids.util.Util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import static com.uiu.kids.model.response.GetAllSlidesResponse.SlideSerialComparator;

public class DashboardPresenter implements DashboardContract.Presenter,Constant {

    private DashboardContract.View view;
    private PreferenceUtil preferenceUtil;
    private Repository repository;
    private List<Slide> slidesList;
    private int count=0;
    private String userId;

    public DashboardPresenter(DashboardContract.View view, PreferenceUtil preferenceUtil, Repository repository) {
        this.view = view;
        slidesList = new ArrayList<>();
        this.preferenceUtil = preferenceUtil;
        this.repository = repository;
        this.view.setPresenter(this);
    }

    @Override
    public void login() {

    }

    @Override
    public void createAccount(HashMap<String, Object> params) {
        if(!Util.isInternetAvailable()){
            view.showNoInternet();
            return;
        }
        view.showProgress();
        repository.createAccount(params, new DataSource.GetResponseCallback<GetAccountResponse>() {
            @Override
            public void onSuccess(GetAccountResponse response) {
                view.hideProgress();
                userId = response.getUser().getId();
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
        this.userId = userId;
        loadSlidesFromLocal(preferenceUtil.getUserSlideList(userId));
        if(!Util.isInternetAvailable()){
            view.showNoInternet();
            return;
        }
        // view.showProgress();
        repository.getUserSlides(userId, new DataSource.GetDataCallback<GetAllSlidesResponse>() {
            @Override
            public void onDataReceived(GetAllSlidesResponse data) {
                view.hideProgress();
                if(!data.isSuccess()) {
                    view.showMessage(data.getResponseMsg());
                    return;
                }

               slidesList=data.getSlide();
                slidesList.add(createLocalInviteSlide());
                view.onSlidesLoaded(getSortedList(slidesList));
                preferenceUtil.saveUserSlides(userId,slidesList);
            }

            @Override
            public void onFailed(int code, String message) {
                view.showMessage(message);
                view.hideProgress();
            }
        });
    }

    private void loadSlidesFromLocal(List<Slide> localList){

        slidesList.clear();
        slidesList.add(createLocalInviteSlide());
        slidesList.addAll(localList);

        view.onSlidesLoaded(getSortedList(slidesList));
    }

    @Override
    public void addSlide(Slide slideItem) {
        Slide inviteSlide = createLocalInviteSlide();
        List<Slide> slideItems = new ArrayList<>();
        slideItems.addAll(preferenceUtil.getUserSlideList(userId));
        slideItems.add(slideItem);
        slideItems.add(inviteSlide);
        preferenceUtil.saveUserSlides(userId,getSortedList(slideItems));
        view.onSlidesUpdated(preferenceUtil.getUserSlideList(userId));
    }

    @Override
    public void removeSlide(Slide slide) {

        Slide slideToRemove=slide;
        List<Slide> slideItems = new ArrayList<>();
        slideItems.addAll(preferenceUtil.getUserSlideList(userId));
        slideItems.add(createLocalInviteSlide());
        for(Slide slide1:slideItems){
            if(slide.getId().equals(slide1.getId()))
                slideToRemove = slide1;
        }
        slideItems.remove(slideToRemove);
        preferenceUtil.saveUserSlides(userId,getSortedList(slideItems));
        view.onSlidesUpdated(preferenceUtil.getUserSlideList(userId));
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


    @Override
    public void start() {

    }

    public Slide createLocalInviteSlide(){
        Slide slide = new Slide();
        slide.setName("Your Kid Helpers");
        slide.setType(SLIDE_INDEX_INVITE);
        return slide;
    }

    public Slide createLocalHomeSlide(){
        Slide slide = new Slide();
        slide.setType(SLIDE_INDEX_HOME);
        slide.setName("Home");
        slide.setUser_id(preferenceUtil.getAccount().getId());
        return slide;
    }
    private void sortSlides(List<Slide> slidesList) {
        Collections.sort(slidesList,SlideSerialComparator);
    }

    public List<Slide> getSortedList(List<Slide> slidesList){
        /****** Removing duplicates ******/
        Set<Slide> hs = new HashSet();

        hs.addAll(slidesList);

        slidesList.clear();
        slidesList.addAll(hs);
        /****************************/

        /******** Updating counters ****/
        for(Slide slide:slidesList){
            int count=1;
            for(Slide slide1:slidesList){
                if(slide.getType()==slide1.getType() && slide.getId()!=slide1.getId())
                    count++;
            }
            slide.setCount(count);
        }
        /*************************/
        sortSlides(slidesList);
        return slidesList;
    }

}
