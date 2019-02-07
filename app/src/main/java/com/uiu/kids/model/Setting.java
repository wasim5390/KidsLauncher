package com.uiu.kids.model;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Setting implements Serializable {

    @SerializedName("location")
    boolean isLocationEnable=false;
    @SerializedName("wifi")
    boolean isWifiEnable=true;
    @SerializedName("bluetooth")
    boolean isBlueTooth=false;

    @SerializedName("sleep")
    boolean isSleepMode=false;

    @SerializedName("timer_enabled")
    boolean isTimedSleepEnable=false;

    @SerializedName("sleep_time")
    String sleepTime;

    @SerializedName("sound_state")
    int soundState=0;
    @SerializedName("brightness")
    Float brightnessLevel=50f;
    @SerializedName("volume")
    int volumeLevel=50;
    @SerializedName("battery")
    Float batteryLevel=50f;

    @SerializedName("background")
    String background;

    boolean isVibrate;

    public String getSleepTime() {
        return sleepTime;
    }

    public void setSleepTime(String sleepTime) {
        this.sleepTime = sleepTime;
    }

    public boolean isSleepMode() {
        return isSleepMode;
    }

    public void setSleepMode(boolean sleepMode) {
        isSleepMode = sleepMode;
    }
    public boolean isLocationEnable() {
        return isLocationEnable;
    }

    public void setLocationEnable(boolean locationEnable) {
        isLocationEnable = locationEnable;
    }

    public boolean isWifiEnable() {
        return isWifiEnable;
    }

    public void setWifiEnable(boolean wifiEnable) {
        isWifiEnable = wifiEnable;
    }

    public boolean isBlueToothOn() {
        return isBlueTooth;
    }

    public void setBlueToothOn(boolean blueTooth) {
        isBlueTooth = blueTooth;
    }

    public boolean isVibrate() {
        return isVibrate;
    }

    public void setVibrate(boolean vibrate) {
        isVibrate = vibrate;
    }

    public Float getBrightnessLevel() {
        return brightnessLevel;
    }

    public void setBrightnessLevel(Float brightnessLevel) {
        this.brightnessLevel = brightnessLevel;
    }

    public int getVolumeLevel() {
        return volumeLevel;
    }

    public void setVolumeLevel(int volumeLevel) {
        this.volumeLevel = volumeLevel;
    }

    public Float getBatteryLevel() {
        return batteryLevel;
    }

    public void setBatteryLevel(Float batteryLevel) {
        this.batteryLevel = batteryLevel;
    }
    public int getSoundState() {
        return soundState;
    }

    public void setSoundState(int soundState) {
        this.soundState = soundState;
    }

    public boolean isTimedSleepEnable() {
        return isTimedSleepEnable;
    }

    public void setTimedSleepEnable(boolean timedSleepEnable) {
        isTimedSleepEnable = timedSleepEnable;
    }

    public String getBackground() {
        return background==null?"":background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

}
