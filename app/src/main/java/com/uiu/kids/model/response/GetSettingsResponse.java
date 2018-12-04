package com.uiu.kids.model.response;

import com.google.gson.annotations.SerializedName;
import com.uiu.kids.model.Setting;

public class GetSettingsResponse extends BaseResponse {
    @SerializedName("setting")
    public Setting settings;

    public Setting getSettings() {
        return settings;
    }


    @Override
    public String toString(){
        return
                "GetSettingsResponse{" +
                        "settings = '" + settings + '\'' +
                        "}";
    }
}
