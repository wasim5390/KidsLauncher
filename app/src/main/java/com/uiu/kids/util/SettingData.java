package com.uiu.kids.util;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.crashlytics.android.Crashlytics;

public class SettingData {


    // SCALE_VOLUME for LG = 14.285; For Samsung = 6.67;
    public static double SCALE_VOLUME = 6.67;
    public static boolean isWifiConnected(Context context){
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        return  mWifi.isConnected();
    }

    public static void setWifiConnected(Context context, boolean isConnected){
        try {
            WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            wifiManager.setWifiEnabled(isConnected);
        }catch (NullPointerException e){}
    }

    public static double getFontScale(Context context){
        return  (double)context.getResources().getConfiguration().fontScale;
    }

    public static void setFontScale(Context context, double fontScale){
        context.getResources().getConfiguration().fontScale = (float)fontScale;
        Settings.System.putFloat(context.getContentResolver(), Settings.System.FONT_SCALE,  (float)fontScale);
    }

    public static float getBrightnessLevel(Context context){
        float curBrightnessValue = 0;
        try {
            curBrightnessValue= Settings.System.getInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
            Crashlytics.logException(e);
        }

        return curBrightnessValue;
    }

    public static void setBrightnessLevel(Context context, int brightness){
        //if(!isAutoBrigthness(activity)) {

        //constrain the value of brightness
        if(brightness < 0)
            brightness = 0;
        else if(brightness > 255)
            brightness = 255;


        ContentResolver cResolver = context.getContentResolver();
        Settings.System.putInt(cResolver, Settings.System.SCREEN_BRIGHTNESS, brightness);
    }


    public static boolean isAutoBrigthness(Context context){

        boolean isAutoBrigthness=false;
        try {
            isAutoBrigthness= Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC ==Settings.System.getInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();Crashlytics.logException(e);
        }
        return isAutoBrigthness;
    }



    public static void setAutoBrigthness(Context context, boolean isAutoBrightness){
        if(isAutoBrightness) {
            Settings.System.putInt(context.getContentResolver(),
                    Settings.System.SCREEN_BRIGHTNESS_MODE,
                    Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
        }else{
            Settings.System.putInt(context.getContentResolver(),
                    Settings.System.SCREEN_BRIGHTNESS_MODE,
                    Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
        }

    }


    public static int getSoundState(Context context){
        AudioManager am = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        return am.getRingerMode();
    }

    public static void setSoundState(Context context, int soundState ){
        AudioManager am = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        am.setRingerMode(soundState);
    }

    public static int getVolume(Context context){
        AudioManager audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        int currentVolume = audio.getStreamVolume(AudioManager.STREAM_RING);
        currentVolume = (int) (currentVolume * SCALE_VOLUME);
        return currentVolume;
    }

    public static void setVolume(Context context, int volume){
        int newVolume = (int) (volume/6.5);
        AudioManager audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        audio.setStreamVolume(AudioManager.STREAM_MUSIC,newVolume, AudioManager.AUDIOFOCUS_NONE);
        audio.setStreamVolume(AudioManager.STREAM_RING,newVolume, AudioManager.AUDIOFOCUS_NONE);
        audio.setStreamVolume(AudioManager.STREAM_ALARM,volume, AudioManager.AUDIOFOCUS_NONE);
        audio.setStreamVolume(AudioManager.STREAM_VOICE_CALL,volume, AudioManager.AUDIOFOCUS_NONE);

        audio.setStreamVolume(AudioManager.STREAM_NOTIFICATION,newVolume, AudioManager.AUDIOFOCUS_NONE);
        audio.setStreamVolume(AudioManager.STREAM_SYSTEM,newVolume, AudioManager.AUDIOFOCUS_NONE);
    }

    public static float getBatteryLevel(Context context) {
        Intent batteryIntent = context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        // Error checking that probably isn't needed but I added just in case.
        if(level == -1 || scale == -1) {
            return 50.0f;
        }

        return ((float)level / (float)scale) * 100.0f;
    }

    public static boolean isGpsOn(Context context) {
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {Crashlytics.logException(ex);
        }
        return gps_enabled;

    }
    public static void setGpsOnOff(Context context,boolean isGpsOn) {
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        try {
            lm.setTestProviderEnabled(LocationManager.GPS_PROVIDER,isGpsOn);
        } catch (Exception ex) {
            Crashlytics.logException(ex);
        }
    }
    public static boolean isBluetoothOn() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        try{
            return bluetoothAdapter.isEnabled();
        }catch (NullPointerException e){
            return false;
        }
    }
    public static boolean setBluetoothOnOff(boolean enable) {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            return false;
        }

        boolean isEnabled = bluetoothAdapter.isEnabled();
        if (enable && !isEnabled) {
            return bluetoothAdapter.enable();
        }
        else if(!enable && isEnabled) {
            return bluetoothAdapter.disable();
        }
        // No need to change bluetooth state
        return true;
    }
    public static boolean isCharging(Context context) {
        Intent intent = context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        return plugged == BatteryManager.BATTERY_PLUGGED_AC || plugged == BatteryManager.BATTERY_PLUGGED_USB;
    }

    public static void wakeupDevice(AppCompatActivity activity){
        Window window = activity.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

    }

    public static void sleepDevice(AppCompatActivity activity){
        int defaultTurnOffTime = Settings.System.getInt(activity.getContentResolver(),Settings.System.SCREEN_OFF_TIMEOUT, 60000);
      // PreferenceUtil.getInstance(activity).savePreference("DefaultScreenTime",String.valueOf(defaultTurnOffTime));
       Settings.System.putInt(activity.getContentResolver(),Settings.System.SCREEN_OFF_TIMEOUT, 1000);
    }
}
