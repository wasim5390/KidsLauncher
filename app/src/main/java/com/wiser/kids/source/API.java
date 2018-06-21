package com.wiser.kids.source;



import com.wiser.kids.model.response.GetAccountResponse;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by sidhu on 4/11/2018.
 */

public interface API {

    @GET("/v3/post/delivery-contractors/account")
    Call<GetAccountResponse> getAccount(@Header("Authorization") String authorizationHeader);


    @POST("/v3/post/delivery-contractors/account")
    Call<GetAccountResponse> createAccount(@Body HashMap<String, Object> params);



}

