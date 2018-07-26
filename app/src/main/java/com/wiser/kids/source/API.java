package com.wiser.kids.source;



import com.wiser.kids.model.request.CreateDefaultSlidesRequest;
import com.wiser.kids.model.request.CreateSlideRequest;
import com.wiser.kids.model.request.FavAppsRequest;
import com.wiser.kids.model.request.LoginRequest;
import com.wiser.kids.model.response.BaseResponse;
import com.wiser.kids.model.response.CreateSlideResponse;
import com.wiser.kids.model.response.GetAccountResponse;

import com.wiser.kids.model.response.GetAllSlidesResponse;
import com.wiser.kids.model.response.GetFavAppsResponse;
import com.wiser.kids.ui.home.apps.AppsEntity;
import com.wiser.kids.ui.home.contact.ContactEntity;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by sidhu on 4/11/2018.
 */

public interface API {

    @GET("users/login")
    Call<GetAccountResponse> login(@Body LoginRequest request);

    @POST("users/register")
    Call<GetAccountResponse> createAccount(@Body HashMap<String, Object> params);

    @POST("slides/create_slides")
    Call<BaseResponse> createDefaultSlides(@Body CreateDefaultSlidesRequest request);

    @POST("slides")
    Call<CreateSlideResponse> createSlide(@Body CreateSlideRequest request);

    @POST("slides")
    Call<BaseResponse> deleteSlide(@Query("id") String slideId);

    @GET("slides/user_slides")
    Call<GetAllSlidesResponse> getUserSlides(@Query ("user_id") String userId);

    @POST("contacts/add_to_slide")
    Call<ContactEntity> saveOnSlide(@Body HashMap<String, Object> params);

    @GET("contacts/slide_contacts")
    Call<List<ContactEntity>> fetchFromSlide(@Query ("slide_id") String var);

    @POST("applications/add_to_slide")
    Call<GetFavAppsResponse> saveAppOnSlide(@Body FavAppsRequest favAppsRequest);

    @GET("applications/user_applications")
    Call<GetFavAppsResponse> getFavApps(@Query ("user_id") String userId);

}

