package com.uiu.kids.model.request;

import java.util.List;

public class HelperListRequest{

    String user_id;

    List<String> helper_ids;




    public String getUserId() {
        return user_id;
    }

    public void setUserId(String userId) {
        this.user_id = userId;
    }

    public List<String> getHelpersID() {
        return helper_ids;
    }

    public void setHelpersID(List<String> helpersID) {
        this.helper_ids = helpersID;
    }



    @Override
    public String toString(){
        return
                "GetFavAppsResponse{" +
                        "helper_add = '" + helper_ids + '\'' +
                        "}";
    }

}
