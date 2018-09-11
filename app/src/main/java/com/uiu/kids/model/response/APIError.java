package com.uiu.kids.model.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sidhu on 4/11/2018.
 */

public class APIError {
    @SerializedName("http_code")
    private int httpCode;

    @SerializedName("response_code")
    private String responseCode;

    @SerializedName("response_msg")
    private String responseMsg;


    @SerializedName("data")
    private Data responseData;

    public int getHttpCode() {
        return httpCode;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public String getResponseMsg() {
        return responseMsg;
    }

    public void setHttpCode(int httpCode) {
        this.httpCode = httpCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public void setResponseMsg(String responseMsg) {
        this.responseMsg = responseMsg;
    }

    public Data getResponseData() {
        return responseData;
    }

    public class Data{
        public String[] getEmail_address() {
            return email_address;
        }

        public void setEmail_address(String[] email_address) {
            this.email_address = email_address;
        }

        public String[] getMobile() {
            return mobile;
        }

        public void setMobile(String[] mobile) {
            this.mobile = mobile;
        }

        @SerializedName("email_address")
        String email_address[];
        @SerializedName("mobile")
        String mobile[];
    }
}
