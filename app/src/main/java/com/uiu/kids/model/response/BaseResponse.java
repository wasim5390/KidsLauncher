package com.uiu.kids.model.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sidhu on 4/11/2018.
 */

public class BaseResponse {

    @SerializedName("http_code")
    private int httpCode;
    @SerializedName("response_code")
    private String responseCode;

    @SerializedName("message")
    private String responseMsg;

    @SerializedName("success")
    private boolean success;



    public int getHttpCode() {
        return httpCode;
    }

    public void setHttpCode(int httpCode) {
        this.httpCode = httpCode;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMsg() {
        return responseMsg;
    }

    public void setResponseMsg(String responseMsg) {
        this.responseMsg = responseMsg;
    }
    public boolean isSuccess() {
        return success;
    }
}
