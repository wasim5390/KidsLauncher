package com.uiu.kids.event;

import com.uiu.kids.model.Location;

public class GeofenceEvent {
    public int transition;

    public Location getLocation() {
        return location;
    }

    public Location location;
    public GeofenceEvent(int enter, Location location) {
        this.transition = enter;
        this.location = location;
    }
}
