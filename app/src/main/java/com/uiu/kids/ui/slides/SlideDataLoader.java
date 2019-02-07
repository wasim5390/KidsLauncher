package com.uiu.kids.ui.slides;

import android.util.Log;

import com.uiu.kids.Constant;
import com.uiu.kids.KidsLauncherApp;
import com.uiu.kids.model.Slide;
import com.uiu.kids.model.User;
import com.uiu.kids.model.response.GetFavAppsResponse;
import com.uiu.kids.model.response.GetFavContactResponse;
import com.uiu.kids.model.response.GetFavLinkResponse;
import com.uiu.kids.model.response.GetSettingsResponse;
import com.uiu.kids.model.response.ReminderResponse;
import com.uiu.kids.source.DataSource;
import com.uiu.kids.source.Repository;
import com.uiu.kids.util.PreferenceUtil;

import java.util.List;

public class SlideDataLoader implements Constant {
    static SlideDataLoader mInstance;

    private PreferenceUtil preferenceUtil= PreferenceUtil.getInstance(KidsLauncherApp.getInstance());


    public static SlideDataLoader getInstance() {
        if(mInstance==null)
            mInstance = new SlideDataLoader();
        return mInstance;
    }

    public void loadSlidesData(List<Slide> slides){
        for (Slide slide:slides){
            switch (slide.getType()){
                case SLIDE_INDEX_FAV_PEOPLE:
                    loadPeopleSlideData(slide);
                    break;
                case SLIDE_INDEX_FAV_APP:
                    loadAppSlideData(slide);
                    break;
                case SLIDE_INDEX_FAV_LINKS:
                    loadLinkSlideData(slide);
                    break;
                case SLIDE_INDEX_REMINDERS:
                    loadReminderSlideData(slide);
                    break;
            }

        }

    }


    /**
     * Load Favorite App slide data
     * @param favAppSlide
     */
    private void loadAppSlideData(Slide favAppSlide){
        Slide slide = favAppSlide;
        Repository.getInstance().getFavApps(slide.getId(), new DataSource.GetDataCallback<GetFavAppsResponse>() {
            @Override
            public void onDataReceived(GetFavAppsResponse data) {
                if(data.isSuccess()){
                    preferenceUtil.saveFavApps(slide.getId(),data.getFavAppsList());
                }
            }

            @Override
            public void onFailed(int code, String message) {
                Log.e(slide.getName(),message);
            }
        });
    }

    /**
     * Load Favorite People slide data
     * @param favPeopleSlide
     */
    private void loadPeopleSlideData(Slide favPeopleSlide){
        Slide slide = favPeopleSlide;
        Repository.getInstance().fetchFromSlide(slide.getId(),new DataSource.GetDataCallback<GetFavContactResponse>() {
            @Override
            public void onDataReceived(GetFavContactResponse data) {
                if(data.isSuccess()) {
                    preferenceUtil.saveFavPeople(slide.getId(),data.getContactEntityList());
                }else{
                    Log.e(slide.getName(), data.getResponseMsg());
                }

            }

            @Override
            public void onFailed(int code, String message) {
                Log.e(slide.getName(), message);
            }
        });
    }

    /**
     * Load Fav link slide data
     * @param favLinkSlide
     */
    private void loadLinkSlideData(Slide favLinkSlide)
    {
        Repository.getInstance().getFavLinks(favLinkSlide.getId(), new DataSource.GetDataCallback<GetFavLinkResponse>() {
            @Override
            public void onDataReceived(GetFavLinkResponse data) {
                if (data.isSuccess()) {
                    preferenceUtil.saveLinkList(favLinkSlide.getId(),data.getFavLinkList());

                } else {
                    Log.e(favLinkSlide.getName(), data.getResponseMsg());
                }
            }

            @Override
            public void onFailed(int code, String message) {

                Log.e(favLinkSlide.getName(), message);
            }
        });
    }

    /**
     * Load Reminders Slide data
     * @param reminderSlide
     */
    private void loadReminderSlideData(Slide reminderSlide){
        Repository.getInstance().fetchReminderList(reminderSlide.getId(), new DataSource.GetDataCallback<ReminderResponse>() {
            @Override
            public void onDataReceived(ReminderResponse data) {
                if(data.isSuccess())
                    preferenceUtil.saveReminders(reminderSlide.getId(),data.getReminders());
                else {
                    Log.e(reminderSlide.getName(), data.getResponseMsg());
                }
            }

            @Override
            public void onFailed(int code, String message) {
                Log.e(reminderSlide.getName(), message);
            }
        });

    }
}
