package com.uiu.kids.model.response;

import com.google.gson.annotations.SerializedName;
import com.uiu.kids.model.Slide;

public class CreateSlideResponse extends BaseResponse {
    @SerializedName("slide")
    Slide slideItem;

    public Slide getSlideItem() {
        return slideItem;
    }
}
