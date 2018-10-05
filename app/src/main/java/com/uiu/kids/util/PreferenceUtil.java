package com.uiu.kids.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.uiu.kids.model.Slide;
import com.uiu.kids.model.User;
import com.uiu.kids.ui.home.apps.AppsEntity;
import com.uiu.kids.ui.home.contact.ContactEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

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

        return favPeopleList==null?new ArrayList<>():favPeopleList;
    }

    public void saveFavPeople(String keySlideId,List<ContactEntity> contactEntityList){
        Gson gson = new Gson();
        String str = gson.toJson(contactEntityList);
        SharedPreferences.Editor editor = sPref.edit();
        editor.putString(keySlideId, str);
        editor.apply();
    }

    public List<AppsEntity> getFavAppsList(String key){
        Gson gson = new Gson();
        List<AppsEntity> favAppsList = gson.fromJson(sPref.getString(key,""),new TypeToken<List<AppsEntity>>() {
        }.getType());

        return favAppsList==null?new ArrayList<>():favAppsList;
    }


    public void saveFavApps(String key,List<AppsEntity> appsEntityList){
        Gson gson = new Gson();
        String str = gson.toJson(appsEntityList);
        SharedPreferences.Editor editor = sPref.edit();
        editor.putString(key, str);
        editor.apply();
    }

    public void saveUserSlides(String keyUserId,List<Slide> slideList){
        Gson gson = new Gson();
        String str = gson.toJson(slideList);
        SharedPreferences.Editor editor = sPref.edit();
        editor.putString(keyUserId, str);
        editor.apply();
    }

    public List<Slide> getUserSlideList(String keyUserId){
        Gson gson = new Gson();
        List<Slide> slides = gson.fromJson(sPref.getString(keyUserId,""),new TypeToken<List<Slide>>() {
        }.getType());

        return slides==null?new ArrayList<>():slides;
    }


    public String getPreference(String key) {
        return sPref.getString(key, "");
    }

    public Boolean getBooleanPreference(String key) {
        return sPref.getBoolean(key, true);
    }

    public void clearAllPreferences() {

        SharedPreferences.Editor editor = sPref.edit();
        editor.clear();
        editor.commit();

    }

    public List<String> getHelperList(){
        Gson gson = new Gson();
        List<String> helperList = gson.fromJson(sPref.getString(KEY_HELPERS,null),new TypeToken<List<String>>() {
        }.getType());

        return helperList==null?new ArrayList<>():helperList;
    }


    public void saveHelperList(List<String> helperList){
        Gson gson = new Gson();
        String str = gson.toJson(helperList);
        SharedPreferences.Editor editor = sPref.edit();
        editor.putString(KEY_HELPERS, str);
        editor.apply();
    }


}
