package com.wiser.kids.util;

import android.content.Context;
import android.content.SharedPreferences;
import com.wiser.kids.model.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wiser.kids.ui.home.apps.AppsEntity;
import com.wiser.kids.ui.home.contact.ContactEntity;

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
    public static final String KEY_COUNTRIES = "countries";
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

    public List<ContactEntity> getFavPeopleList(){
        Gson gson = new Gson();
        List<ContactEntity> favPeopleList = gson.fromJson(sPref.getString(KEY_FAV_PEOPLE,""),new TypeToken<List<ContactEntity>>() {
        }.getType());

        return favPeopleList==null?new ArrayList<>():favPeopleList;
    }

    public void saveFavPeople(List<ContactEntity> contactEntityList){
        Gson gson = new Gson();
        String str = gson.toJson(contactEntityList);
        SharedPreferences.Editor editor = sPref.edit();
        editor.putString(KEY_FAV_PEOPLE, str);
        editor.apply();
    }

    public List<AppsEntity> getFavAppsList(){
        Gson gson = new Gson();
        List<AppsEntity> favPeopleList = gson.fromJson(sPref.getString(KEY_FAV_APP,""),new TypeToken<List<AppsEntity>>() {
        }.getType());

        return favPeopleList==null?new ArrayList<>():favPeopleList;
    }


    public void saveFavApps(List<AppsEntity> appsEntityList){
        Gson gson = new Gson();
        String str = gson.toJson(appsEntityList);
        SharedPreferences.Editor editor = sPref.edit();
        editor.putString(KEY_FAV_APP, str);
        editor.apply();
    }


    public String getPreference(String key) {
        return sPref.getString(key, "");
    }

    public void clearAllPreferences() {

        SharedPreferences.Editor editor = sPref.edit();
        editor.clear();
       editor.apply();

    }

}
