package com.wiser.kids.event;

public class GeofenceEvent {
    public int transition;

    public GeofenceEvent(int enter) {
        this.transition = enter;
    }
}