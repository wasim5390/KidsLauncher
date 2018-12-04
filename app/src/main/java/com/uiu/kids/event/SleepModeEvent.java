package com.uiu.kids.event;

public class SleepModeEvent {
    public boolean isEnable() {
        return enable;
    }

    boolean enable;

    public String getTime() {
        return time;
    }

    String time;

    public SleepModeEvent(boolean enable,String time) {
        this.enable = enable;
        this.time = time;
    }

}
