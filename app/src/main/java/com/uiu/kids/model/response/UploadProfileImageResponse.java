package com.uiu.kids.model.response;

import com.google.gson.annotations.SerializedName;

public class UploadProfileImageResponse extends BaseResponse {

    @SerializedName("image_link")
    private String link;

    public String getLink() {
        return link;
    }
}
