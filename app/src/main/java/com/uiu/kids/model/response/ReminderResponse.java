package com.uiu.kids.model.response;

import com.google.gson.annotations.SerializedName;
import com.uiu.kids.ui.slides.reminder.ReminderEntity;

import java.io.Serializable;
import java.util.List;

public class ReminderResponse implements Serializable {

    @SerializedName("success")
    public boolean success;

    @SerializedName("reminders")
    public List<ReminderEntity> reminder;

    public List<ReminderEntity> getReminders() {
        return reminder;
    }

    public boolean isSuccess() {
        return success;
    }

    @Override
    public String toString(){
        return
                "GetReminderResponse{" +
                        "reminder = '" + reminder + '\'' +
                        "}";
    }
}
