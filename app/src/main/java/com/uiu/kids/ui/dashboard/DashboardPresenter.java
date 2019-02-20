package com.uiu.kids.ui.dashboard;

import android.util.Log;

import com.uiu.kids.Constant;
import com.uiu.kids.model.LinksEntity;
import com.uiu.kids.model.Setting;
import com.uiu.kids.model.Slide;
import com.uiu.kids.model.User;
import com.uiu.kids.model.response.BaseResponse;
import com.uiu.kids.model.response.GetAccountResponse;
import com.uiu.kids.model.response.GetAllSlidesResponse;
import com.uiu.kids.model.response.GetDirectionsResponse;
import com.uiu.kids.model.response.GetFavAppsResponse;
import com.uiu.kids.model.response.GetFavContactResponse;
import com.uiu.kids.model.response.GetFavLinkResponse;
import com.uiu.kids.model.response.GetSettingsResponse;
import com.uiu.kids.model.response.InvitationResponse;
import com.uiu.kids.model.response.ReminderResponse;
import com.uiu.kids.source.DataSource;
import com.uiu.kids.source.Repository;
import com.uiu.kids.ui.home.apps.AppsEntity;
import com.uiu.kids.ui.home.contact.ContactEntity;
import com.uiu.kids.ui.slides.SlideDataLoader;
import com.uiu.kids.util.PreferenceUtil;
import com.uiu.kids.util.Util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.uiu.kids.model.response.GetAllSlidesResponse.SlideSerialComparator;

public class DashboardPresenter implements DashboardContract.Presenter,Constant {

    private DashboardContract.View view;
    private PreferenceUtil preferenceUtil;
    private Repository repository;
    private List<Slide> slidesList;
    private int count=0;
    private String userId;
    boolean isSettingsUpdating=false;

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
                if(response.isSuccess()) {
                    userId = response.getUser().getId();
                    preferenceUtil.saveAccount(response.getUser());
                    view.onLoginSuccessful(response.getUser());
                    getInvites(response.getUser().getId());
                }else
                {
                    if(response.getResponseMsg().toLowerCase().contains("mobile")){
                        params.remove("mobile_number");
                        view.phoneNumberExist(params);
                    }
                }
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
    public void loadSlides(String userId){
        if(!preferenceUtil.getUserSlideList(userId).isEmpty())
            loadSlidesFromLocal();
        else
            getUserSlides(userId);

    }

    @Override
    public void getUserSlides(String userId) {
        this.userId = userId;
        {
            if (!Util.isInternetAvailable()) {
                view.showNoInternet();
                return;
            }

            repository.getUserSlides(userId, new DataSource.GetDataCallback<GetAllSlidesResponse>() {
                @Override
                public void onDataReceived(GetAllSlidesResponse data) {
                    view.hideProgress();
                    if (!data.isSuccess()) {
                        view.showMessage(data.getResponseMsg());
                        return;
                    }
                    preferenceUtil.saveUserSlides(userId, data.getSlide());
                    SlideDataLoader.getInstance().loadSlidesData(data.getSlide());
                    slidesList.clear();
                    slidesList.add(createClockSlide());
                    slidesList.addAll(data.getSlide());
                    User primaryHelper = preferenceUtil.getAccount().getPrimaryHelper();
                  //  if (primaryHelper == null
                  //          || !primaryHelper.isPrimaryConnected()
                  //          )
                        slidesList.add(createLocalInviteSlide());
                    preferenceUtil.savePreference(Constant.LAST_SYNC_TIME,Util.formatDate(Util.DATE_FORMAT_1,new Date().getTime()));
                    List<Slide> slides = Util.getSortedList(slidesList);
                    view.onSlidesLoaded(slides);

                }

                @Override
                public void onFailed(int code, String message) {
                    view.showMessage(message);
                    view.hideProgress();
                }
            });

            // view.showProgress();
        }

    }
    @Override
    public void loadInvites(String userId){
        this.userId = userId;
        if(!preferenceUtil.getAccount().getInvitations().isEmpty() ) {
            loadSlides(userId);
        }else
            getInvites(userId);
    }

    @Override
    public void getInvites(String userId){
        this.userId = userId;
        if (!Util.isInternetAvailable()) {
            view.showNoInternet();
            return;
        }
        repository.getInvites(userId, new DataSource.GetDataCallback<InvitationResponse>() {
            @Override
            public void onDataReceived(InvitationResponse data) {
                if(data.isSuccess()) {
                    User account = preferenceUtil.getAccount();
                    account.setInvitations(data.getInvitationList());
                    preferenceUtil.saveAccount(account);
                    getUserSlides(userId);

                }else{
                    view.hideProgress();
                }
            }

            @Override
            public void onFailed(int code, String message) {
                view.hideProgress();
                getUserSlides(userId);
            }
        });
    }

    @Override
    public void getSettings(){
       repository.getSettings(preferenceUtil.getAccount().getId(), new DataSource.GetDataCallback<GetSettingsResponse>() {
            @Override
            public void onDataReceived(GetSettingsResponse data) {
            view.onSettingsUpdated(data.getSettings());
            }

            @Override
            public void onFailed(int code, String message) {

            }
        });
    }

    public void loadSlidesFromLocal(){
        List<Slide> localList = preferenceUtil.getUserSlideList(userId);
        slidesList.clear();
        slidesList.add(createClockSlide());
      //  User primaryHelper = preferenceUtil.getAccount().getPrimaryHelper();
      //  if ( primaryHelper == null || !primaryHelper.isPrimaryConnected())
            slidesList.add(createLocalInviteSlide());
        slidesList.addAll(localList);
        List<Slide> slides = Util.getSortedList(slidesList);

        view.onSlidesLoaded(slides);
    }

    @Override
    public void addSlide(Slide slideItem) {
        Slide inviteSlide = createLocalInviteSlide();
        Slide clockSlide = createClockSlide();
        List<Slide> slideItems = new ArrayList<>();

        slideItems.addAll(preferenceUtil.getUserSlideList(userId));
        slideItems.add(slideItem);
        preferenceUtil.saveUserSlides(userId,slideItems);

        slideItems.add(clockSlide);
        slideItems.add(inviteSlide);
        List<Slide> slides = Util.getSortedList(slideItems);


        User primaryHelper = preferenceUtil.getAccount().getPrimaryHelper();
       // if ( primaryHelper == null || !primaryHelper.isPrimaryConnected())



        view.onSlidesUpdated(slides);
    }

    @Override
    public void removeSlide(Slide slide) {

        Slide slideToRemove=slide;
        List<Slide> slideItems = new ArrayList<>();
        List<Slide> prefList = preferenceUtil.getUserSlideList(userId);
        slideItems.add(createClockSlide());
        slideItems.addAll(prefList);
        User primaryHelper = preferenceUtil.getAccount().getPrimaryHelper();
       // if ( primaryHelper == null
       //         || !primaryHelper.isPrimaryConnected()
       //         )
            slideItems.add(createLocalInviteSlide());

        for(Slide slide1:slideItems){
            if(slide.getId().equals(slide1.getId()))
                slideToRemove = slide1;
        }
        prefList.remove(slideToRemove);
        slideItems.remove(slideToRemove);
        List<Slide> slides = Util.getSortedList(prefList);

        preferenceUtil.saveUserSlides(userId,slides);
        view.onSlidesUpdated(slideItems);
    }

    @Override
    public void removeSlideByType(int type) {

        Slide slideToRemove=null;
        List<Slide> slideItems = new ArrayList<>();
        List<Slide> prefList = preferenceUtil.getUserSlideList(userId);
        slideItems.add(createClockSlide());
        slideItems.addAll(prefList);
        User primaryHelper = preferenceUtil.getAccount().getPrimaryHelper();
      //  if ( primaryHelper == null || !primaryHelper.isPrimaryConnected())
            slideItems.add(createLocalInviteSlide());

        for(Slide slide1:slideItems){
            if(type==slide1.getType())
                slideToRemove = slide1;
        }
        if(slideToRemove!=null) {
            slideItems.remove(slideToRemove);
            prefList.remove(slideToRemove);

        }
        preferenceUtil.saveUserSlides(userId,prefList);
        List<Slide> slides = Util.getSortedList(slideItems);
        view.onSlidesUpdated(slides);

    }

    @Override
    public void updateKidLocation(HashMap<String, Object> params) {
        if (!Util.isInternetAvailable()) {
            view.showNoInternet();
            return;
        }
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
    public void updateKidLocationRange(HashMap<String, Object> params) {
        params.put("user_id",userId);
        if (!Util.isInternetAvailable()) {
            view.showNoInternet();
            return;
        }
        repository.updateKidsRangeLocation(params, new DataSource.GetResponseCallback<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse response) {

            }

            @Override
            public void onFailed(int code, String message) {

            }
        });
    }

    @Override
    public void loadUpdatedSlides() {
        List<Slide> localList = preferenceUtil.getUserSlideList(userId);
        slidesList.clear();
        slidesList.add(createClockSlide());
        //  User primaryHelper = preferenceUtil.getAccount().getPrimaryHelper();
        //  if ( primaryHelper == null || !primaryHelper.isPrimaryConnected())
        slidesList.add(createLocalInviteSlide());
        slidesList.addAll(localList);
        List<Slide> slides = Util.getSortedList(slidesList);

        view.onSlidesUpdated(slides);
    }

    @Override
    public void notifyBatteryAlert(String kidId) {
        if(!Util.isInternetAvailable())
            return;
        repository.batteryAlert(kidId, new DataSource.GetResponseCallback<BaseResponse>() {
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
        if (!Util.isInternetAvailable()) {
            view.showNoInternet();
            return;
        }
        if(userId!=null)
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
    public void updateKidsSettings(Setting setting) {
        if(!Util.isInternetAvailable())
            return;
        if(userId==null || isSettingsUpdating) return;

        isSettingsUpdating=true;
        HashMap<String,Object> params = new HashMap<>();
        params.put("kid_id",userId);
        params.put("setting",setting);
       // view.showProgress();
        repository.updateKidSettings(params, new DataSource.GetDataCallback<GetSettingsResponse>() {
            @Override
            public void onDataReceived(GetSettingsResponse data) {
                isSettingsUpdating=false;
                view.hideProgress();
               // if(data.isSuccess()) {
                   // view.onSettingsUpdated(data.getSettings());
               // }
               // else
               //     view.showMessage(data.getResponseMsg());
            }

            @Override
            public void onFailed(int code, String message) {
                isSettingsUpdating=false;
                view.hideProgress();
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
    public Slide createClockSlide(){
        Slide slide = new Slide();
        slide.setType(SLIDE_INDEX_CLOCK);
        slide.setName(Constant.CLOCK);
        slide.setUser_id(preferenceUtil.getAccount().getId());
        return slide;
    }








}
