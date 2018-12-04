package com.uiu.kids.ui.slides.reminder;

import com.google.gson.annotations.SerializedName;
import com.uiu.kids.util.Util;

import java.util.Date;

public class ReminderEntity {

    @SerializedName("id")
    public String id;

    @SerializedName("slide_id")
    public String slide_id;

    @SerializedName("title")
    public String title;

    @SerializedName("note")
    public String note;

    @SerializedName("time")
    public String time;

    @SerializedName("date")
    public String date;

    @SerializedName("file_url")
    public String reminderNoteLink;

    @SerializedName("request_status")
    public int requestStatus;

    @SerializedName("is_repeated")
    public boolean is_repeated;

    public boolean isActive;

    public Date connverteddate;

    public Date getdate() {
        Date date = Util.getDateFromMilliseconds(Long.parseLong(time));
        return date;
    }
    public int getRequestStatus() {
        return requestStatus;
    }
    public void setdate(Date calendar) {
        this.connverteddate = calendar;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSlide_id() {
        return slide_id;
    }

    public void setSlide_id(String slide_id) {
        this.slide_id = slide_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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
    public String getReminderNoteLink() {
        return reminderNoteLink;
    }
    public boolean isActiveReminder() {
        return !Util.isTimeOlder(getTime());
    }
    public void setIsActiveReminder(boolean active) {
        isActive = active;
    }

    public boolean getIs_repeated() {
        return is_repeated;
    }

    public void setIs_repeated(boolean is_repeated) {
        this.is_repeated = is_repeated;
    }

    public void setRequestStatus(int requestStatus) {
        this.requestStatus = requestStatus;
    }
}
