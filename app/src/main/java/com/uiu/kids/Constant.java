package com.uiu.kids;


import android.media.MediaPlayer;

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

    String CONTACTS="Contacts";
    String DIALER="Dialer";
    String MESSAGING="Messaging";
    String CAMERA = "Camera";
    String C_ME="C Me";
    String SOS="Sos";
    String CLOCK="Clock";
    String APPLICATIONS= "Applications";
    String CALL_FROM_INFO="CallFromContactInfo";

    int RES_DIALER= R.drawable.ic_dialpad;

    int RES_SOS= R.mipmap.ic_sos_home;
    int RES_SEE_ME= R.mipmap.ic_cme_home;
    int RES_ADD_NEW= R.drawable.ic_add_icon;
    int RES_CAMERA= R.mipmap.ic_camera_home;
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

    String KEY_SELECTED_BG="key_selected_bg";
    String KEY_SAFE_PLACES="key_safe_places";

    String FAV_APP_SLIDE_NAME="Favorite Applications";
    String FAV_PEOPLE_SLIDE_NAME="Favorite Peoples";
    String FAV_LINK_SLIDE_NAME="Favorite Links";
    String[] SLIDES ={FAV_PEOPLE_SLIDE_NAME,FAV_APP_SLIDE_NAME};


    String PREF_NOTIFICATION_TOKEN="notification_token";
    String KEY_BRIGHTNESS="key_brightness";
    String KEY_VOLUME="key_volume";

    int REQ_REQUESTED=1;
    int REJECTED=2;
    int ACCEPTED=3;


    int SLIDE_INDEX_NOTIFICATION=0;
    int SLIDE_INDEX_CLOCK=-2;
    int SLIDE_INDEX_INVITE=-1;
    int SLIDE_INDEX_HOME=1;
    int SLIDE_INDEX_FAV_PEOPLE=2;
    int SLIDE_INDEX_FAV_APP=3;
    int SLIDE_INDEX_FAV_GAMES=4;
    int SLIDE_INDEX_FAV_LINKS=5;
    int SLIDE_INDEX_SOS=6;
    int SLIDE_INDEX_REMINDERS=7;
    int SLIDE_INDEX_DIRECTIONS=8;
    int SLIDE_CREATE_INDEX=9;
    int SLIDE_REMOVE_INDEX=10;

    int INVITE_CODE=11;
    int PRIMARY_PARENT_REMOVE=12;

    int REQ_BEEP=13;
    int REQ_SLEEP=14;
    int REQ_BATTERY_ALERT=15;
    int REQ_SETTINGS=16;
    int REQ_LOCATION=17;
    int REQ_KID_OUT_OF_FENCE=18;

    int MEDIA_IMAGE=1;
    int MEDIA_VIDEO=2;
    int MEDIA_AUDIO=3;
    int MEDIA_FILE=4;

    int REQUEST_CODE_UPDATE_PIC = 0x1;
    int REQUEST_CODE_UPDATE_PIC_ID = 0x2;

    String PREF_KEY_SLEEP_MODE="sleep_mode";
    String PREF_KEY_SLEEP_TIME="sleep_time";

    //====== Home Btn Colors Pref=====//
    String HOME_DIAL_BG = "HOME_DIAL_BG";
    String HOME_CAMERA_BG = "HOME_CAMERA_BG";
    String HOME_CME_BG = "HOME_CME_BG";
    String HOME_SOS_BG = "HOME_SOS_BG" ;
    String NO_SPACE_ON_SLIDE = "No space available on this slide, add on other slide";
    String CAMERA_IMG_PATH ="camera_img_path" ;
    String RECORDED_FILE_PATH ="recorded_file_path" ;
    String RECORDED_FILE_TYPE ="recorded_file_type" ;

     String key_camera_type="CAMERA_TYPE";
     String key_camera_for_result="CAMERA_FOR_RESULT";
    String LAST_SYNC_TIME ="last_sync_time" ;


    interface IntentExtras {
        String ACTION_CAMERA = "action-camera";
        String ACTION_GALLERY = "action-gallery";
        String IMAGE_PATH = "image-path";
        String IMAGE_PATH_ID = "image-path-identity";
    }

    interface PicModes {
        String CAMERA = "Camera";
        String GALLERY = "Gallery";
        String CANCEL = "Cancel";
    }


    interface INVITE {

        int ACCEPTED = 0;
        int CONNECTED= 1;
        int REJECTED= 2;
        int INVITED= 3;
    }
    MediaPlayer mMediaPlayerForService = new MediaPlayer();

}
