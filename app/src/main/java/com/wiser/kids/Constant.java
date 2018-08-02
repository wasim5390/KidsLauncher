package com.wiser.kids;

/**
 * Created by sidhu on 4/18/2018.
 */

public interface Constant {


    /** App Modes*/
    String DEVELOPMENT="Development";
    String PRODUCTION="Production";
    String DEBUG="Debugging";

    //Status Codes
    int SUCCESS = 200;
    int BAD_REQUEST = 400;

    // Slide
    String PARSE_ID = "parseSlideId";
    String LOCAL_ID = "localSlideId";

    String CONTACTS="Contacts";
    String DIALER="Dialer";
    String MESSAGING="Messaging";
    String CAMERA = "Camera";
    String APPLICATIONS= "Applications";
    String CALL_FROM_INFO="CallFromContactInfo";

    int RES_CONTACTS= R.drawable.ic_contacts;
    int RES_DIALER= R.drawable.ic_dial;
    int RES_APPS= R.drawable.ic_apps;
    int RES_ADD_NEW= R.drawable.ic_add_icon;
    int RES_CAMERA= R.drawable.ic_camera;
    int RES_CALL_SMALL=R.mipmap.circle_call_button;
    int RES_AVATAR=R.mipmap.wiser_avatar;

    String SELECTED_CONTACT="selected_contact";

    int TAB_MODE_MOBILE = 0;
    int TAB_MODE_HOME = 1;
    int TAB_MODE_EMAIL = 2;

    String KEY_SELECTED_CONTACT="add_selected_contact";
    String KEY_SELECTED_APP="add_selected_apps";
    String KEY_SELECTED_APP_NAME="add_selected_apps";
    String KEY_SELECTED_APP_ICON="add_selected_apps";
    String KEY_SELECTED_APP_PKGNAME="add_selected_apps";

    String FAV_APP_SLIDE_NAME="Favorite Applications";
    String FAV_PEOPLE_SLIDE_NAME="Favorite Peoples";
    String[] SLIDES ={FAV_PEOPLE_SLIDE_NAME,FAV_APP_SLIDE_NAME};


    String PREF_NOTIFICATION_TOKEN="notification_token";

    int REQ_REQUESTED=1;
    int REJECTED=2;
    int ACCEPTED=3;

    int SLIDE_INDEX_HOME=1;
    int SLIDE_INDEX_FAV_PEOPLE=2;
    int SLIDE_INDEX_FAV_APP=3;
    int SLIDE_INDEX_FAV_GAMES=4;
    int SLIDE_INDEX_FAV_LINKS=5;
    int SLIDE_INDEX_SOS=6;
    int SLIDE_INDEX_REMINDERS=7;

}
