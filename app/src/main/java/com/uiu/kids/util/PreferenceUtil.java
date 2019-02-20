package com.uiu.kids.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.uiu.kids.Constant;
import com.uiu.kids.model.Invitation;
import com.uiu.kids.model.LinksEntity;
import com.uiu.kids.model.Location;
import com.uiu.kids.model.Slide;
import com.uiu.kids.model.User;
import com.uiu.kids.ui.home.apps.AppsEntity;
import com.uiu.kids.ui.home.contact.Contact;
import com.uiu.kids.ui.home.contact.ContactEntity;
import com.uiu.kids.ui.slides.reminder.ReminderEntity;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by sidhu on 6/8/2018.
 */

public class PreferenceUtil {


    public static final String KEY_IS_SIGN_IN = "is_sign_in";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_USER = "user";
    public static final String KEY_HELPERS = "helpers";
    public static final String KEY_LAST_SYNC = "last_sync";
    private static final String PREFERENCE_NAME = "send_signal_preference";
    private static final String KEY_APP_MODE ="app_mode" ;
    private static final String KEY_FAV_PEOPLE ="fav_peoples" ;
    private static final String KEY_FAV_APP ="fav_apps" ;
    private static final String KEY_ALL_SOS ="all_sos" ;
    private String defaultAppMode="Production";

    private static PreferenceUtil instance;

    private SharedPreferences sPref;
    private PreferenceUtil (Context context) {
        sPref = context.getSharedPreferences(PREFERENCE_NAME,MODE_PRIVATE);
    }
    public static PreferenceUtil getInstance(Context context) {
        if (instance == null) {
            instance = new PreferenceUtil(context);
        }
        return instance;
    }

    public boolean isSignIn() {
        return sPref.getBoolean(KEY_IS_SIGN_IN, false);
    }

    public void setSignIn(boolean value) {
        SharedPreferences.Editor editor = sPref.edit();
        editor.putBoolean(KEY_IS_SIGN_IN, value);
        editor.apply();
    }

    public String getUsername() {
        return sPref.getString(KEY_EMAIL, "");
    }

    public String getPassword() {
        return sPref.getString(KEY_PASSWORD, "");
    }

    public String getAppMode() {
        return sPref.getString(KEY_APP_MODE, defaultAppMode);
    }

    public void saveEmail(String email) {
        SharedPreferences.Editor editor = sPref.edit();
        editor.putString(KEY_EMAIL, email);
        editor.apply();
    }

    public void savePassword(String password) {
        SharedPreferences.Editor editor = sPref.edit();
        editor.putString(KEY_PASSWORD, password);
        editor.apply();
    }

    public void saveAppMode(String mode) {
        SharedPreferences.Editor editor = sPref.edit();
        editor.putString(KEY_APP_MODE, mode);
        editor.apply();
    }
    public void saveAccount(User user) {
        Gson gson = new Gson();
        String str = gson.toJson(user);
        SharedPreferences.Editor editor = sPref.edit();
        editor.putString(KEY_USER, str);
        editor.apply();
    }

    public User getAccount() {
        Gson gson = new Gson();
        User user = gson.fromJson(sPref.getString(KEY_USER,""), User.class);

        if (user==null) {
            user = new User();
        }
        return user;
    }

    public void savePreference(String key, String value) {
        SharedPreferences.Editor editor = sPref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public void savePreference(String key, Boolean value) {
        SharedPreferences.Editor editor = sPref.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public List<ContactEntity> getFavPeopleList(String key){
        Gson gson = new Gson();
        List<ContactEntity> favPeopleList = gson.fromJson(sPref.getString(key,""),new TypeToken<List<ContactEntity>>() {
        }.getType());

        return favPeopleList;//==null?new ArrayList<>():favPeopleList;
    }

    // this belongs to all saved favorite people
    public List<ContactEntity> getAllFavPeoples(String userId){
        Gson gson = new Gson();
        List<ContactEntity> favPeopleList = gson.fromJson(sPref.getString(userId+"_peoples",""),new TypeToken<List<ContactEntity>>() {
        }.getType());

        return favPeopleList;//==null?new ArrayList<>():favPeopleList;
    }

    public void removeFavPeople(ContactEntity entityToRemove){
        List<ContactEntity> allFavorite = getAllFavPeoples(getAccount().getId());
        if(allFavorite!=null ){
            ContactEntity entity = contactExistInList(entityToRemove,allFavorite);
            if(entity!=null) {
                allFavorite.remove(entity);
                saveAllFavoritePeople(getAccount().getId(), allFavorite);
            }
        }
    }
    public void addFavPeopleInAllContacts(ContactEntity entityToAdd){
        List<ContactEntity> allFavorite = getAllFavPeoples(getAccount().getId());
        if(allFavorite!=null ){
            ContactEntity entity = contactExistInList(entityToAdd,allFavorite);
            if(entity==null) {
                allFavorite.add(entityToAdd);
                saveAllFavoritePeople(getAccount().getId(), allFavorite);
            }
        }
    }
    public ContactEntity contactExistInList(ContactEntity contactEntity, List<ContactEntity> list){
       ContactEntity entityExist=null;
        for(ContactEntity entity:list){
            if(entity.getId().equals(contactEntity.getId())) {
                entityExist=entity;
                break;
            }

        }
        return entityExist;
    }

    public void saveFavPeople(String keySlideId,List<ContactEntity> contactEntityList){
        Gson gson = new Gson();
        String str = gson.toJson(contactEntityList);
        SharedPreferences.Editor editor = sPref.edit();
        editor.putString(keySlideId, str);
        editor.apply();
    }

    public void saveFavPeople(String keySlideId,ContactEntity contactEntity){
        List<ContactEntity> favPeoples=getFavPeopleList(keySlideId);
        favPeoples=favPeoples==null?new ArrayList<>():favPeoples;
        favPeoples.add(contactEntity);
        Gson gson = new Gson();
        String str = gson.toJson(favPeoples);
        SharedPreferences.Editor editor = sPref.edit();
        editor.putString(keySlideId, str);
        editor.apply();
    }

    public void updateFavPeople(String keySlideId,ContactEntity contactEntity){
        List<ContactEntity> favPeoples=getFavPeopleList(keySlideId);
        favPeoples=favPeoples==null?new ArrayList<>():favPeoples;
        for(ContactEntity entity:favPeoples){
            if(entity.getId().equals(contactEntity.getId()))
                entity.setBase64ProfilePic(contactEntity.getProfilePic());
        }
        Gson gson = new Gson();
        String str = gson.toJson(favPeoples);
        SharedPreferences.Editor editor = sPref.edit();
        editor.putString(keySlideId, str);
        editor.apply();
    }

    public void saveAllFavoritePeople(String userId, List<ContactEntity> contactEntities){
        Gson gson = new Gson();
        String str = gson.toJson(contactEntities);
        SharedPreferences.Editor editor = sPref.edit();
        editor.putString(userId+"_peoples", str);
        editor.apply();
    }


    public List<AppsEntity> getFavAppsList(String key){
        Gson gson = new Gson();
        List<AppsEntity> favAppsList = gson.fromJson(sPref.getString(key,""),new TypeToken<List<AppsEntity>>() {
        }.getType());

        return favAppsList;//==null?new ArrayList<>():favAppsList;
    }


    public void saveFavApps(String key,List<AppsEntity> appsEntityList){
        Gson gson = new Gson();
        String str = gson.toJson(appsEntityList);
        SharedPreferences.Editor editor = sPref.edit();
        editor.putString(key, str);
        editor.apply();
    }

    public void saveFavApp(String keySlideId,AppsEntity appsEntity){
        List<AppsEntity> favAppsList=getFavAppsList(keySlideId);
        favAppsList=favAppsList==null?new ArrayList<>():favAppsList;
        favAppsList.add(appsEntity);
        Gson gson = new Gson();
        String str = gson.toJson(favAppsList);
        SharedPreferences.Editor editor = sPref.edit();
        editor.putString(keySlideId, str);
        editor.apply();
    }

    public List<LinksEntity> getFavLinkList(String key){
        Gson gson = new Gson();
        List<LinksEntity> list = gson.fromJson(sPref.getString(key,""),new TypeToken<List<LinksEntity>>() {
        }.getType());

        return list;//==null?new ArrayList<>():list;
    }


    public void saveLinkList(String key,List<LinksEntity> entities){
        Gson gson = new Gson();
        String str = gson.toJson(entities);
        SharedPreferences.Editor editor = sPref.edit();
        editor.putString(key, str);
        editor.apply();
    }

    public void saveLink(String keySlideId,LinksEntity linksEntity){
        List<LinksEntity> favLinkList=getFavLinkList(keySlideId);
        favLinkList=favLinkList==null?new ArrayList<>():favLinkList;
        favLinkList.add(linksEntity);
        Gson gson = new Gson();
        String str = gson.toJson(favLinkList);
        SharedPreferences.Editor editor = sPref.edit();
        editor.putString(keySlideId, str);
        editor.apply();
    }


    public void saveFavSos(String keySlideId,List<ContactEntity> contactEntityList){

        Gson gson = new Gson();
        String str = gson.toJson(contactEntityList);
        SharedPreferences.Editor editor = sPref.edit();
        editor.putString(keySlideId, str);
        editor.apply();
    }

    public List<ContactEntity> getSosList(String key){
        Gson gson = new Gson();
        List<ContactEntity> sosList = gson.fromJson(sPref.getString(key,""),new TypeToken<List<ContactEntity>>() {
        }.getType());

        return sosList;
    }

    public void saveReminders(String keySlideId,List<ReminderEntity> contactEntityList){
        Gson gson = new Gson();
        String str = gson.toJson(contactEntityList);
        SharedPreferences.Editor editor = sPref.edit();
        editor.putString(keySlideId, str);
        editor.apply();
    }

    public List<ReminderEntity> getReminderList(String key){
        Gson gson = new Gson();
        List<ReminderEntity> sosList = gson.fromJson(sPref.getString(key,""),new TypeToken<List<ReminderEntity>>() {
        }.getType());

        return sosList;//==null?new ArrayList<>():sosList;
    }

    public void saveSafePlaces(String keySlideId,List<Location> safePlaces){
        Gson gson = new Gson();
        String str = gson.toJson(safePlaces);
        SharedPreferences.Editor editor = sPref.edit();
        editor.putString(keySlideId, str);
        editor.apply();
    }

    public List<Location> getSafePlacesList(String key){
        Gson gson = new Gson();
        List<Location> sosList = gson.fromJson(sPref.getString(key,""),new TypeToken<List<Location>>() {
        }.getType());

        return sosList;//==null?new ArrayList<>():sosList;
    }

    public Location getSafePlaces(String id){
        Location mLocation=null;
        List<Location> mList = getSafePlacesList(Constant.KEY_SAFE_PLACES);
        if(mList==null)
            return null;
        for(Location location:mList){
            if(location.getId().equals(id)){
                mLocation = location;
            }
        }
        return mLocation;
    }

    public void saveUserSlides(String keyUserId,List<Slide> slideList){
        Gson gson = new Gson();
        String str = gson.toJson(slideList);
        SharedPreferences.Editor editor = sPref.edit();
        editor.putString(keyUserId+"_slides", str);
        editor.apply();
    }

    public List<Slide> getUserSlideList(String keyUserId){
        Gson gson = new Gson();
        List<Slide> slides = gson.fromJson(sPref.getString(keyUserId+"_slides",""),new TypeToken<List<Slide>>() {
        }.getType());

        return slides==null?new ArrayList<>():slides;
    }


    public String getPreference(String key) {
        return sPref.getString(key, "");
    }

    public String getColorPreference(String key,String defaultColor) {
        return sPref.getString(key, defaultColor);
    }

    public Boolean getBooleanPreference(String key,boolean defaultVal) {
        return sPref.getBoolean(key, defaultVal);
    }

    public void clearAllPreferences() {

        SharedPreferences.Editor editor = sPref.edit();
        editor.clear();
        editor.commit();

    }

    public List<Invitation> getInvitationList(){
        Gson gson = new Gson();
        List<Invitation> helperList = gson.fromJson(sPref.getString(KEY_HELPERS,null),new TypeToken<List<Invitation>>() {
        }.getType());

        return helperList==null?new ArrayList<>():helperList;
    }


    public void saveInvitationList(List<Invitation> helperList){
        Gson gson = new Gson();
        String str = gson.toJson(helperList);
        SharedPreferences.Editor editor = sPref.edit();
        editor.putString(KEY_HELPERS, str);
        editor.apply();
    }

    public void saveAllSos(List<ContactEntity> contactEntityList){
        Gson gson = new Gson();
        String str = gson.toJson(contactEntityList);
        SharedPreferences.Editor editor = sPref.edit();
        editor.putString(KEY_ALL_SOS, str);
        editor.apply();
    }

    public List<ContactEntity> getAllSosList(){
        Gson gson = new Gson();
        List<ContactEntity> sosList = gson.fromJson(sPref.getString(KEY_ALL_SOS,""),new TypeToken<List<ContactEntity>>() {
        }.getType());

        return sosList;
    }

}
