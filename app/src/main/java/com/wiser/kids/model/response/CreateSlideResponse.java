package com.wiser.kids.model.response;

import com.google.gson.annotations.SerializedName;
import com.wiser.kids.model.SlideItem;

public class CreateSlideResponse extends BaseResponse {
    @SerializedName("slide")
    SlideItem slideItem;

    public SlideItem getSlideItem() {
        return slideItem;
    }
}
