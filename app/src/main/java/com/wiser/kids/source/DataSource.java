package com.wiser.kids.source;



import com.wiser.kids.model.response.GetAccountResponse;

import java.util.HashMap;
import java.util.List;

/**
 * Created by sidhu on 4/11/2018.
 */

public interface DataSource {

    void createAccount(HashMap<String, Object> params, GetResponseCallback<GetAccountResponse> callback);
    void getAccount(String header, GetDataCallback<GetAccountResponse> callback);



    interface GetDataCallback<M> {
        void onDataReceived(M data);
        void onFailed(int code, String message);
    }

    interface GetResponseCallback<M> {
        void onSuccess(M response);
        void onFailed(int code, String message);
    }
}
