package com.uiu.kids.location;

import com.google.android.gms.common.api.ResolvableApiException;

public class LocationServiceEnableEvent {
    public LocationServiceEnableEvent(boolean enableLocations) {
        this.enableLocations = enableLocations;
    }

    public ResolvableApiException getException() {
        return exception;
    }

    public void setException(ResolvableApiException exception) {
        this.exception = exception;
    }

    ResolvableApiException exception;

    boolean enableLocations;

}
