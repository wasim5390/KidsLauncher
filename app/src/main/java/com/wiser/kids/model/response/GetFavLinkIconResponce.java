package com.wiser.kids.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class GetFavLinkIconResponce extends BaseResponse {

    @SerializedName("url")
    public String url;

    @SerializedName("icons")
    public List<Icons> getIcons=new ArrayList<Icons>();

    public class Icons
    {

        @SerializedName("url")
        public String url;

        @SerializedName("width")
        public int width;

    }
}
