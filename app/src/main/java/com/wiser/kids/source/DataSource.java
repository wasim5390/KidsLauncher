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
import com.wiser.kids.ui.home.contact.ContactEntity;

import java.util.HashMap;
import java.util.List;

/**
 * Created by sidhu on 4/11/2018.
 */

public interface DataSource {

    void createAccount(HashMap<String, Object> params, GetResponseCallback<GetAccountResponse> callback);
    void getAccount(LoginRequest request, GetDataCallback<GetAccountResponse> callback);

    void createSlide(CreateSlideRequest request, GetDataCallback<CreateSlideResponse> callback);
    void createDefaultSlides(CreateDefaultSlidesRequest request, GetDataCallback<BaseResponse> callback);
    void deleteSlide(String slideId, GetResponseCallback<BaseResponse> callback);
    void getUserSlides(String userId, GetDataCallback<GetAllSlidesResponse> callback);

    void addToSlide(String id,ContactEntity cont, GetDataCallback<ContactEntity> callback);
    void fetchFromSlide(String id, GetDataCallback<List<ContactEntity>> callback);

    void addFavAppToSlide(FavAppsRequest appsRequest, GetDataCallback<GetFavAppsResponse> callback);
    void getFavApps(String userId, GetDataCallback<GetFavAppsResponse> callback);



    interface GetDataCallback<M> {
        void onDataReceived(M data);
        void onFailed(int code, String message);
    }

    interface GetResponseCallback<M> {
        void onSuccess(M response);
        void onFailed(int code, String message);
    }
}
