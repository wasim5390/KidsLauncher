package com.uiu.kids.source;


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
import com.uiu.kids.model.response.HelperResponse;
import com.uiu.kids.model.response.ReminderResponse;
import com.uiu.kids.ui.home.contact.ContactEntity;

import java.util.HashMap;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by sidhu on 4/11/2018.
 */

public interface DataSource {

    void createAccount(HashMap<String, Object> params, GetResponseCallback<GetAccountResponse> callback);

    void getHelpers(GetResponseCallback<HelperResponse> callback);

    void saveHelper(HelperListRequest helperRequest, GetDataCallback<HelperResponse> callback);

    void savePrimaryHelper(String userId, String helperId, GetDataCallback<HelperResponse> callback);

    void getAccount(LoginRequest request, GetDataCallback<GetAccountResponse> callback);

    void createSlide(CreateSlideRequest request, GetDataCallback<CreateSlideResponse> callback);

    void createDefaultSlides(CreateDefaultSlidesRequest request, GetDataCallback<BaseResponse> callback);

    void deleteSlide(String slideId, GetResponseCallback<BaseResponse> callback);

    void getUserSlides(String userId, GetDataCallback<GetAllSlidesResponse> callback);

    void getFavContacts(String id, GetDataCallback<GetFavContactResponse> callback);
    void addToSlide(String id, ContactEntity cont, GetDataCallback<ContactEntity> callback);

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

    void updateKidsLocation(HashMap<String, Object> params, GetResponseCallback<BaseResponse> callback);

    void getKidDirections(String userId, GetDataCallback<GetDirectionsResponse> callback);

    void getChatMessageList(String userId, String contactId, GetDataCallback<GetAllChatResponse> callback);

    void shareMediaFile(HashMap<String, RequestBody> params, MultipartBody.Part body, String contacts, GetDataCallback<GetAllChatResponse> callback);



    interface GetDataCallback<M> {
        void onDataReceived(M data);

        void onFailed(int code, String message);
    }

    interface GetResponseCallback<M> {
        void onSuccess(M response);

        void onFailed(int code, String message);
    }
}
