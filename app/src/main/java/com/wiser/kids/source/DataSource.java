package com.wiser.kids.source;


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

import com.wiser.kids.model.response.GetAllSlidesResponse;
import com.wiser.kids.model.response.GetFavAppsResponse;
import com.wiser.kids.model.response.GetFavContactResponse;
import com.wiser.kids.model.response.GetFavLinkIconResponce;

import com.wiser.kids.model.response.GetFavLinkResponse;
import com.wiser.kids.model.response.GetSOSResponse;
import com.wiser.kids.model.response.HelperResponse;
import com.wiser.kids.model.response.ReminderResponse;
import com.wiser.kids.ui.home.contact.ContactEntity;

import java.util.HashMap;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Part;

/**
 * Created by sidhu on 4/11/2018.
 */

public interface DataSource {

    void createAccount(HashMap<String, Object> params, GetResponseCallback<GetAccountResponse> callback);

    void getHelpers(GetResponseCallback<HelperResponse> callback);

    void saveHelper(HelperListRequest helperRequest, GetDataCallback<HelperResponse> callback);

    void getAccount(LoginRequest request, GetDataCallback<GetAccountResponse> callback);

    void createSlide(CreateSlideRequest request, GetDataCallback<CreateSlideResponse> callback);

    void createDefaultSlides(CreateDefaultSlidesRequest request, GetDataCallback<BaseResponse> callback);

    void deleteSlide(String slideId, GetResponseCallback<BaseResponse> callback);

    void getUserSlides(String userId, GetDataCallback<GetAllSlidesResponse> callback);

    void getFavContacts(String id, GetDataCallback<GetFavContactResponse> callback);
    void addToSlide(String id,ContactEntity cont, GetDataCallback<ContactEntity> callback);

    void fetchFromSlide(String id, GetDataCallback<GetFavContactResponse> callback);

    void addFavAppToSlide(FavAppsRequest appsRequest, GetDataCallback<GetFavAppsResponse> callback);

    void getFavApps(String userId, GetDataCallback<GetFavAppsResponse> callback);

    void getFavLinkIcon(String url, GetDataCallback<GetFavLinkIconResponce> callback);

    void addFavLinkToSlide(FavLinkRequest linkRequest, GetDataCallback<GetFavLinkResponse> callback);

    void getFavLinks(String userId, GetDataCallback<GetFavLinkResponse> callback);

/////////////SOS

    void addSOSToSlide(FavSOSRequest favSOSRequest, GetDataCallback<GetSOSResponse> callback);

    void fetchSOSForSlide(String user_id, GetDataCallback<GetSOSResponse> callback);

    ////Reminder
    void fetchReminderList(String user_id, GetDataCallback<ReminderResponse> callback);

    void shareMedia(HashMap<String, RequestBody> params, MultipartBody.Part body, List<String> contacts, GetDataCallback<BaseResponse> callback);



    interface GetDataCallback<M> {
        void onDataReceived(M data);

        void onFailed(int code, String message);
    }

    interface GetResponseCallback<M> {
        void onSuccess(M response);

        void onFailed(int code, String message);
    }
}
