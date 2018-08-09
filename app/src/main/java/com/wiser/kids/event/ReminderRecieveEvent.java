package com.wiser.kids.event;

public class ReminderRecieveEvent {

    int index,type;


    public ReminderRecieveEvent(int index ,int type) {
        this.index = index;
        this.type=type;
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
}
