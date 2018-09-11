package com.uiu.kids.model.response;

import com.google.gson.annotations.SerializedName;
import com.uiu.kids.model.Location;

import java.util.List;

public class GetDirectionsResponse extends BaseResponse {


    @SerializedName("directions")
    private List<Location> directionsList;

    public List<Location> getDirectionsList() {
        return directionsList;
    }
}
