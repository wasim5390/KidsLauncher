package com.wiser.kids.source;



import com.wiser.kids.model.Location;
import com.wiser.kids.model.request.CreateDefaultSlidesRequest;
import com.wiser.kids.model.request.CreateSlideRequest;
import com.wiser.kids.model.request.FavAppsRequest;
import com.wiser.kids.model.request.FavLinkRequest;
import com.wiser.kids.model.request.FavSOSRequest;
import com.wiser.kids.model.request.HelperListRequest;
import com.wiser.kids.model.request.LoginRequest;
import com.wiser.kids.model.response.BaseResponse;
import com.wiser.kids.model.response.CreateSlideResponse;
import com.wiser.kids.model.response.GetAccountResponse;

import com.wiser.kids.model.response.GetAllChatResponse;
import com.wiser.kids.model.response.GetAllSlidesResponse;
import com.wiser.kids.model.response.GetDirectionsResponse;
import com.wiser.kids.model.response.GetFavAppsResponse;
import com.wiser.kids.model.response.GetFavContactResponse;
import com.wiser.kids.model.response.GetFavLinkIconResponce;

import com.wiser.kids.model.response.GetFavLinkResponse;
import com.wiser.kids.model.response.GetSOSResponse;
import com.wiser.kids.model.response.HelperResponse;
import com.wiser.kids.model.response.ReminderResponse;
import com.wiser.kids.ui.home.apps.AppsEntity;
import com.wiser.kids.ui.home.contact.ContactEntity;

import java.util.HashMap;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
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

    @GET("contacts/user_contacts")
    Call<GetFavContactResponse> getFavContacts(@Query("user_id") String userId);

    @GET("contacts/registered_contacts") // Registered users
    Call<GetFavContactResponse> getFavoriteContacts(@Query("user_id") String userId);

    @POST("contacts/add_to_slide")
    Call<GetFavContactResponse> saveOnSlide(@Body HashMap<String, Object> params);

    @GET("contacts/slide_contacts")
    Call<GetFavContactResponse> fetchFromSlide(@Query ("slide_id") String var);

    @POST("applications/add_to_slide")
    Call<GetFavAppsResponse> saveAppOnSlide(@Body FavAppsRequest favAppsRequest);

    @GET("applications/slide_applications")
    Call<GetFavAppsResponse> getFavApps(@Query ("slide_id") String slideId);

    @GET("allicons.json")
    Call<GetFavLinkIconResponce> getLinkIcon(@Query ("url") String url);

    @POST("links/add_to_slide")
    Call<GetFavLinkResponse> saveLinkOnSlide(@Body FavLinkRequest favlinkRequest);

    @GET("links/slide_links")
    Call<GetFavLinkResponse> getFavLinks(@Query ("slide_id") String slideId);


    @POST("sos/add_to_slide")
    Call<GetSOSResponse> saveSOSOnSlide(@Body FavSOSRequest favSOSRequest);

    @GET("sos/slide_sos")
    Call<GetSOSResponse> getSOS(@Query ("slide_id") String slideId);

    @GET("reminders")
    Call<ReminderResponse> getReminderList(@Query ("slide_id") String slideId);

    @Multipart
    @POST("contacts/share_picture")
    Call<BaseResponse> shareMedia(@PartMap HashMap<String, RequestBody> params, @Part MultipartBody.Part file, @Part("contact_ids[]") List<String> contacts);


    @GET("users/helpers_list")
    Call<HelperResponse> getHelpers();

    @POST("users/add_helpers")
    Call<HelperResponse> saveHelperList(@Body HelperListRequest helperListRequest);

    @POST("trackers/save_location")
    Call<BaseResponse> updateKidsLocation(@Body HashMap<String, Object> params);

    @GET("directions")
    Call<GetDirectionsResponse> getDirections(@Query("user_id") String userId);

    @GET("contacts/chat")
    Call<GetAllChatResponse> getMessageList(@Query("user_id") String userId, @Query("contact_id") String ContactId);

    @Multipart
    @POST("contacts/share_file")
    Call<GetAllChatResponse> shareMediaFile(@PartMap HashMap<String, RequestBody> params, @Part MultipartBody.Part file, @Query("contact_id") String contacts);




}

