package com.uiu.kids.event;

public class ReminderRecieveEvent {

    int index,type;
    String title,note;


    public ReminderRecieveEvent(int index ,int type,String title,String note) {
        this.index = index;
        this.type=type;
        this.title=title;
        this.note=note;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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
}
