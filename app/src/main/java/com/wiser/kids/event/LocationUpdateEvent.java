package com.wiser.kids.event;

import com.wiser.kids.model.Location;

public class LocationUpdateEvent {
    private Location  location;

    public LocationUpdateEvent(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }
}
