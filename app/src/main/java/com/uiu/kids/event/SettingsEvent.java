package com.uiu.kids.event;

import android.content.Intent;

public class SettingsEvent {


    Integer brightness,volume;

    public SettingsEvent(int brightness) {
        this.brightness = brightness;
    }

    public SettingsEvent(Integer brightness, Integer volume) {
        this.brightness = brightness;
        this.volume = volume;
    }

    public int getBrightness() {
        return brightness==null?100:brightness;
    }

    public int getVolume() {
        return volume==null?1:volume;
    }
}
