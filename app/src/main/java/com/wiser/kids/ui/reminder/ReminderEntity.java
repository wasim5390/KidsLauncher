package com.wiser.kids.ui.reminder;

public class ReminderEntity {

    public String name;
    public String time;
    public String date;
    public String alarmText;
    public boolean isFlagEmpty;

    public boolean isFlagEmpty() {
        return isFlagEmpty;
    }

    public void setFlagEmpty(boolean flagEmpty) {
        isFlagEmpty = flagEmpty;
    }

    public ReminderEntity(String name, String time, String date) {
        this.name = name;
        this.time = time;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAlarmText() {
        return alarmText;
    }

    public void setAlarmText(String alarmText) {
        this.alarmText = alarmText;
    }
}
