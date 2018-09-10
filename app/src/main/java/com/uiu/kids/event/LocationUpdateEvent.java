package com.uiu.kids.event;

import com.uiu.kids.model.Location;

public class LocationUpdateEvent {
    private Location location;

    public LocationUpdateEvent(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }
}
