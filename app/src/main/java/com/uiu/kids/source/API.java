package com.uiu.kids.source;



import com.uiu.kids.model.NotificationsListResponse;
import com.uiu.kids.model.request.CreateDefaultSlidesRequest;
import com.uiu.kids.model.request.CreateSlideRequest;
import com.uiu.kids.model.request.FavAppsRequest;
import com.uiu.kids.model.request.FavLinkRequest;
import com.uiu.kids.model.request.FavSOSRequest;
import com.uiu.kids.model.request.HelperListRequest;
import com.uiu.kids.model.request.LoginRequest;
import com.uiu.kids.model.response.BaseResponse;
import com.uiu.kids.model.response.CreateSlideResponse;
import com.uiu.kids.model.response.GetAccountResponse;
import com.uiu.kids.model.response.GetAllChatResponse;
import com.uiu.kids.model.response.GetAllSlidesResponse;
import com.uiu.kids.model.response.GetDirectionsResponse;
import com.uiu.kids.model.response.GetFavAppsResponse;
import com.uiu.kids.model.response.GetFavContactResponse;
import com.uiu.kids.model.response.GetFavLinkIconResponce;
import com.uiu.kids.model.response.GetFavLinkResponse;
import com.uiu.kids.model.response.GetSOSResponse;
import com.uiu.kids.model.response.GetSettingsResponse;
import com.uiu.kids.model.response.InvitationResponse;
import com.uiu.kids.model.response.ReminderResponse;
import com.uiu.kids.model.response.UploadProfileImageResponse;

import java.util.HashMap;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by sidhu on 4/11/2018.
 */

public interface API {

    @GET("users/login")
    Call<GetAccountResponse> login(@Body LoginRequest request);

    @POST("users/register")
    Call<GetAccountResponse> createAccount(@Body HashMap<String, Object> params);

    @POST("users/update_fcm_key")
    Call<BaseResponse> updateFirebaseToken(@Query("user_id") String userId, @Query("fcm_key") String fcmKey);

    @PATCH("invitations/{id}")
    Call<InvitationResponse> updateInvite(@Path("id") String inviteId, @Query("request_status") int status,@Query("user_id") String userId);


    @GET("invitations")
    Call<InvitationResponse> getInvites(@Query("id") String id);

    @DELETE("invitations/{id}")
    Call<BaseResponse> disconnectKid(@Path("id") String inviteId);


    @POST("slides/create_slides")
    Call<BaseResponse> createDefaultSlides(@Body CreateDefaultSlidesRequest request);

    @POST("slides")
    Call<CreateSlideResponse> createSlide(@Body CreateSlideRequest request);

    @POST("slides")
    Call<BaseResponse> deleteSlide(@Query("id") String slideId);

    @GET("slides/user_slides")
    Call<GetAllSlidesResponse> getUserSlides(@Query("user_id") String userId);

    @GET("contacts/user_contacts")
    Call<GetFavContactResponse> getFavContacts(@Query("user_id") String userId);

    @GET("users/registered_users")
    Call<GetFavContactResponse> getRegisteredContacts(@Query("user_id") String userId);

    @GET("contacts/registered_contacts") // Registered users
    Call<GetFavContactResponse> getFavoriteContacts(@Query("user_id") String userId);

    @POST("contacts/add_to_slide")
    Call<GetFavContactResponse> saveOnSlide(@Body HashMap<String, Object> params);

    @GET("contacts/slide_contacts")
    Call<GetFavContactResponse> fetchFromSlide(@Query("slide_id") String var);

    @POST("applications/add_to_slide")
    Call<GetFavAppsResponse> saveAppOnSlide(@Body FavAppsRequest favAppsRequest);

    @GET("applications/slide_applications")
    Call<GetFavAppsResponse> getFavApps(@Query("slide_id") String slideId);

    @GET("allicons.json")
    Call<GetFavLinkIconResponce> getLinkIcon(@Query("url") String url);

    @POST("links/add_to_slide")
    Call<GetFavLinkResponse> saveLinkOnSlide(@Body FavLinkRequest favlinkRequest);

    @GET("links/slide_links")
    Call<GetFavLinkResponse> getFavLinks(@Query("slide_id") String slideId);


    @POST("sos/add_to_slide")
    Call<GetSOSResponse> saveSOSOnSlide(@Body FavSOSRequest favSOSRequest);

    @GET("sos/slide_sos")
    Call<GetSOSResponse> getSOS(@Query("slide_id") String slideId);

    @GET("sos")
    Call<GetSOSResponse> getAllUserSOS(@Query("user_id") String userId);

    @GET("reminders")
    Call<ReminderResponse> getReminderList(@Query("slide_id") String slideId);

    @Multipart
    @POST("contacts/share_picture")
    Call<BaseResponse> shareMedia(@PartMap HashMap<String, RequestBody> params, @Part MultipartBody.Part file, @Part("contact_ids[]") List<String> contacts);


    @POST("trackers/save_location")
    Call<BaseResponse> updateKidsLocation(@Body HashMap<String, Object> params);


    @POST("directions/out_of_range")
    Call<BaseResponse> updateKidsRangeLocation(@Body HashMap<String, Object> params);

    @GET("directions")
    Call<GetDirectionsResponse> getDirections(@Query("user_id") String userId);

    @GET("directions/slide_directions")
    Call<GetDirectionsResponse> getSlideDirections(@Query("slide_id") String slideId);

    @GET("contacts/chat")
    Call<GetAllChatResponse> getMessageList(@Query("user_id") String userId, @Query("contact_id") String ContactId);

    @Multipart
    @PUT("contacts/update_icon")
    Call<UploadProfileImageResponse> updateContactImage(@PartMap HashMap<String, RequestBody> params, @Part MultipartBody.Part file);


    @Multipart
    @POST("contacts/share_file")
    Call<GetAllChatResponse> shareMediaFile(@PartMap HashMap<String, RequestBody> params, @Part MultipartBody.Part file);

    @Multipart
    @POST("users/update_profile_image")
    Call<UploadProfileImageResponse> updateProfileImage(@PartMap HashMap<String, RequestBody> params, @Part MultipartBody.Part file);


    //================ Settings ==============//

    @POST("actions/battery_alert")
    Call<BaseResponse> batteryAlert(@Query("kid_id") String userId);

    @PATCH("settings/update")
    Call<GetSettingsResponse> updateSettings(@Body HashMap<String,Object> params);

    @GET("settings")
    Call<GetSettingsResponse> getSettings(@Query("user_id") String userId);

    // ================ Notifications ============//
    @GET("notifications")
    Call<NotificationsListResponse> getNotificationsList(@Query("user_id") String userId, @Query("page") String pageNumber);


}

