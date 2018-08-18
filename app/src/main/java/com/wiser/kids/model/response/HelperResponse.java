package com.wiser.kids.model.response;

import com.google.gson.annotations.SerializedName;
import com.wiser.kids.ui.home.helper.HelperEntity;

import java.io.Serializable;
import java.util.List;

public class HelperResponse implements Serializable{

    @SerializedName("success")
    public boolean success;

    @SerializedName("message")
    public String message;

    @SerializedName("users")
    public List<HelperEntity> helperEntities;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<HelperEntity> getHelperEntities() {
        return helperEntities;
    }

    public void setHelperEntities(List<HelperEntity> helperEntities) {
        this.helperEntities = helperEntities;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
