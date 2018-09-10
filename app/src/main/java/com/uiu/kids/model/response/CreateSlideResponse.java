package com.uiu.kids.model.response;

import com.google.gson.annotations.SerializedName;
import com.uiu.kids.model.SlideItem;

public class CreateSlideResponse extends BaseResponse {
    @SerializedName("slide")
    SlideItem slideItem;

    public SlideItem getSlideItem() {
        return slideItem;
    }
}
