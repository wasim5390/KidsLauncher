package com.uiu.kids.source;

import android.support.annotation.NonNull;

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
import com.uiu.kids.ui.home.contact.ContactEntity;

import java.util.HashMap;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by sidhu on 4/11/2018.
 */

public class Repository implements DataSource {

    private static Repository instance;
    private final RemoteDataSource mRemoteDataSource;

    private Repository(@NonNull RemoteDataSource remoteDataSource) {
        mRemoteDataSource =  remoteDataSource;
    }

    public static Repository getInstance() {

        if (instance==null) {
            instance = new Repository(RemoteDataSource.getInstance());
        }
        return instance;
    }

    @Override
    public void createAccount(HashMap<String, Object> params, final GetResponseCallback<GetAccountResponse> callback) {
        mRemoteDataSource.createAccount(params, new GetResponseCallback<GetAccountResponse>() {
            @Override
            public void onSuccess(GetAccountResponse response) {
                callback.onSuccess(response);
            }

            @Override
            public void onFailed(int code, String message) {
                callback.onFailed(code,message);
            }
        });
    }


    @Override
    public void getInvites(String id, GetDataCallback<InvitationResponse> callback) {
        mRemoteDataSource.getInvites(id, new GetDataCallback<InvitationResponse>() {
            @Override
            public void onDataReceived(InvitationResponse data) {
                callback.onDataReceived(data);
            }

            @Override
            public void onFailed(int code, String message) {
                callback.onFailed(code,message);
            }
        });
    }



    @Override
    public void updateInvite(String inviteId,int status, String userId,GetResponseCallback<InvitationResponse> callback) {
        mRemoteDataSource.updateInvite(inviteId, status,userId,new GetResponseCallback<InvitationResponse>() {
            @Override
            public void onSuccess(InvitationResponse response) {
                if(response.isSuccess())
                    callback.onSuccess(response);
                else
                    callback.onFailed(response.getHttpCode(), response.getResponseMsg());
            }

            @Override
            public void onFailed(int code, String message) {
                callback.onFailed(code,message);
            }
        });
    }

    @Override
    public void disconnect(String inviteId, GetResponseCallback<BaseResponse> callback) {
        mRemoteDataSource.disconnect(inviteId, new GetResponseCallback<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse response) {
                if(response.isSuccess())
                    callback.onSuccess(response);
                else
                    callback.onFailed(response.getHttpCode(), response.getResponseMsg());
            }

            @Override
            public void onFailed(int code, String message) {
                callback.onFailed(code,message);
            }
        });
    }

    @Override
    public void getRegdContacts(String userId, GetDataCallback<GetFavContactResponse> callback) {
        mRemoteDataSource.getRegdContacts(userId,new GetDataCallback<GetFavContactResponse>() {
            @Override
            public void onDataReceived(GetFavContactResponse data) {
                callback.onDataReceived(data);
            }

            @Override
            public void onFailed(int code, String message) {
                callback.onFailed(code, message);
            }
        });
    }



    @Override
    public void getAccount(LoginRequest request, final GetDataCallback<GetAccountResponse> callback) {
        mRemoteDataSource.getAccount(request,new GetDataCallback<GetAccountResponse>() {
            @Override
            public void onDataReceived(GetAccountResponse data) {
                callback.onDataReceived(data);
            }

            @Override
            public void onFailed(int code, String message) {
                callback.onFailed(code, message);
            }
        });
    }

    @Override
    public void uploadProfileImage(HashMap<String, RequestBody> params, MultipartBody.Part body, GetResponseCallback<UploadProfileImageResponse> callback) {
        mRemoteDataSource.uploadProfileImage(params, body, new GetResponseCallback<UploadProfileImageResponse>() {
            @Override
            public void onSuccess(UploadProfileImageResponse response) {
                callback.onSuccess(response);
            }

            @Override
            public void onFailed(int code, String message) {
                callback.onFailed(code,message);
            }
        });
    }

    @Override
    public void createSlide(CreateSlideRequest request, GetDataCallback<CreateSlideResponse> callback) {
        mRemoteDataSource.createSlide(request,new GetDataCallback<CreateSlideResponse>() {
            @Override
            public void onDataReceived(CreateSlideResponse data) {
                callback.onDataReceived(data);
            }

            @Override
            public void onFailed(int code, String message) {
                callback.onFailed(code, message);
            }
        });
    }

    @Override
    public void createDefaultSlides(CreateDefaultSlidesRequest request, GetDataCallback<BaseResponse> callback) {
        mRemoteDataSource.createDefaultSlides(request,new GetDataCallback<BaseResponse>() {
            @Override
            public void onDataReceived(BaseResponse data) {
                callback.onDataReceived(data);
            }

            @Override
            public void onFailed(int code, String message) {
                callback.onFailed(code, message);
            }
        });
    }

    @Override
    public void deleteSlide(String slideId, GetResponseCallback<BaseResponse> callback) {
        mRemoteDataSource.deleteSlide(slideId, new GetResponseCallback<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse response) {
                callback.onSuccess(response);

            }

            @Override
            public void onFailed(int code, String message) {
                callback.onFailed(code,message);
            }
        });
    }


    @Override
    public void getUserSlides(String userId, GetDataCallback<GetAllSlidesResponse> callback) {
        mRemoteDataSource.getUserSlides(userId, new GetDataCallback<GetAllSlidesResponse>() {
            @Override
            public void onDataReceived(GetAllSlidesResponse data) {
                callback.onDataReceived(data);
            }

            @Override
            public void onFailed(int code, String message) {
                callback.onFailed(code,message);
            }
        });

    }

    @Override
    public void getFavContacts(String id, GetDataCallback<GetFavContactResponse> callback) {
        mRemoteDataSource.getFavContacts(id,new GetDataCallback<GetFavContactResponse>() {
            @Override
            public void onDataReceived(GetFavContactResponse data) {
                callback.onDataReceived(data);
            }

            @Override
            public void onFailed(int code, String message) {
                callback.onFailed(code, message);
            }
        });
    }

    @Override
    public void addFavPeopleToSlide(String id, ContactEntity cont, final GetDataCallback<GetFavContactResponse> callback) {
        mRemoteDataSource.addFavPeopleToSlide(id,cont,new GetDataCallback<GetFavContactResponse>() {
            @Override
            public void onDataReceived(GetFavContactResponse data) {
                callback.onDataReceived(data);
            }

            @Override
            public void onFailed(int code, String message) {
                callback.onFailed(code, message);
            }
        });
    }

    @Override
    public void fetchFromSlide(String id, final GetDataCallback<GetFavContactResponse> callback) {
        mRemoteDataSource.fetchFromSlide(id,new GetDataCallback<GetFavContactResponse>() {
            @Override
            public void onDataReceived(GetFavContactResponse data) {
                callback.onDataReceived(data);
            }

            @Override
            public void onFailed(int code, String message) {
                callback.onFailed(code, message);
            }
        });
    }

    @Override
    public void addFavAppToSlide(FavAppsRequest appsRequest, GetDataCallback<GetFavAppsResponse> callback) {
        mRemoteDataSource.addFavAppToSlide(appsRequest, new GetDataCallback<GetFavAppsResponse>() {
            @Override
            public void onDataReceived(GetFavAppsResponse data) {
                callback.onDataReceived(data);
            }

            @Override
            public void onFailed(int code, String message) {
                callback.onFailed(code,message);
            }
        });
    }

    @Override
    public void getFavApps(String userId, GetDataCallback<GetFavAppsResponse> callback) {
        mRemoteDataSource.getFavApps(userId, new GetDataCallback<GetFavAppsResponse>() {
            @Override
            public void onDataReceived(GetFavAppsResponse data) {
                callback.onDataReceived(data);
            }

            @Override
            public void onFailed(int code, String message) {
                callback.onFailed(code,message);
            }
        });
    }

    @Override
    public void getFavLinkIcon(String url, GetDataCallback<GetFavLinkIconResponce> callback) {
        mRemoteDataSource.getFavLinkIcon(url, new GetDataCallback<GetFavLinkIconResponce>() {
            @Override
            public void onDataReceived(GetFavLinkIconResponce data) {
                callback.onDataReceived(data);
            }

            @Override
            public void onFailed(int code, String message) {
                callback.onFailed(code,message);
            }
        });


    }

    @Override
    public void addFavLinkToSlide(FavLinkRequest linkRequest, GetDataCallback<GetFavLinkResponse> callback) {
        mRemoteDataSource.addFavLinkToSlide(linkRequest, new GetDataCallback<GetFavLinkResponse>() {
            @Override
            public void onDataReceived(GetFavLinkResponse data) {
                callback.onDataReceived(data);
            }

            @Override
            public void onFailed(int code, String message) {
                callback.onFailed(code,message);
            }
        });
    }

    @Override
    public void getFavLinks(String userId, GetDataCallback<GetFavLinkResponse> callback) {
        mRemoteDataSource.getFavLinks(userId, new GetDataCallback<GetFavLinkResponse>() {
            @Override
            public void onDataReceived(GetFavLinkResponse data) {
                callback.onDataReceived(data);
            }

            @Override
            public void onFailed(int code, String message) {
                callback.onFailed(code,message);
            }
        });

    }

    @Override
    public void addSOSToSlide(FavSOSRequest favSOSRequest, GetDataCallback<GetSOSResponse> callback) {

        mRemoteDataSource.addSOSToSlide(favSOSRequest, new GetDataCallback<GetSOSResponse>() {
            @Override
            public void onDataReceived(GetSOSResponse data) {
                callback.onDataReceived(data);
            }

            @Override
            public void onFailed(int code, String message) {
                callback.onFailed(code,message);
            }
        });

    }

    @Override
    public void fetchSOSForSlide(String id, GetDataCallback<GetSOSResponse> callback) {
        mRemoteDataSource.fetchSOSForSlide(id, new GetDataCallback<GetSOSResponse>() {
            @Override
            public void onDataReceived(GetSOSResponse data) {
                callback.onDataReceived(data);
            }

            @Override
            public void onFailed(int code, String message) {
                callback.onFailed(code,message);
            }
        });
    }

    @Override
    public void fetchUserAllSOS(String user_id, GetDataCallback<GetSOSResponse> callback) {
        mRemoteDataSource.fetchUserAllSOS(user_id, new GetDataCallback<GetSOSResponse>() {
            @Override
            public void onDataReceived(GetSOSResponse data) {
                callback.onDataReceived(data);
            }

            @Override
            public void onFailed(int code, String message) {
                callback.onFailed(code,message);
            }
        });
    }

    @Override
    public void fetchReminderList(String user_id, GetDataCallback<ReminderResponse> callback) {
        mRemoteDataSource.fetchReminderList(user_id, new GetDataCallback<ReminderResponse>() {
            @Override
            public void onDataReceived(ReminderResponse data) {
                callback.onDataReceived(data);
            }

            @Override
            public void onFailed(int code, String message) {
                callback.onFailed(code,message);
            }
        });
    }

    @Override
    public void shareMedia(HashMap<String, RequestBody> params, MultipartBody.Part body, List<String> contacts,GetDataCallback<BaseResponse> callback) {
        mRemoteDataSource.shareMedia(params,body,contacts, new GetDataCallback<BaseResponse>() {
            @Override
            public void onDataReceived(BaseResponse data) {
                callback.onDataReceived(data);
            }

            @Override
            public void onFailed(int code, String message) {
                callback.onFailed(code,message);
            }
        });
    }

    @Override
    public void updateKidsLocation(HashMap<String, Object> params, GetResponseCallback<BaseResponse> callback) {
        mRemoteDataSource.updateKidsLocation(params, new GetResponseCallback<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse response) {
                callback.onSuccess(response);
            }

            @Override
            public void onFailed(int code, String message) {
                callback.onFailed(code,message);
            }
        });
    }

    @Override
    public void updateKidsRangeLocation(HashMap<String, Object> params, GetResponseCallback<BaseResponse> callback) {
        mRemoteDataSource.updateKidsRangeLocation(params, new GetResponseCallback<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse response) {
                callback.onSuccess(response);
            }

            @Override
            public void onFailed(int code, String message) {
                callback.onFailed(code,message);
            }
        });
    }

    @Override
    public void getKidDirections(String userId, GetDataCallback<GetDirectionsResponse> callback) {
        mRemoteDataSource.getKidDirections(userId, new GetDataCallback<GetDirectionsResponse>() {
            @Override
            public void onDataReceived(GetDirectionsResponse data) {
                callback.onDataReceived(data);
            }

            @Override
            public void onFailed(int code, String message) {
                callback.onFailed(code,message);
            }
        });
    }

    @Override
    public void getChatMessageList(String userId, String contactId, GetDataCallback<GetAllChatResponse> callback) {
        mRemoteDataSource.getChatMessageList(userId,contactId ,new GetDataCallback<GetAllChatResponse>() {
            @Override
            public void onDataReceived(GetAllChatResponse data) {
                callback.onDataReceived(data);
            }

            @Override
            public void onFailed(int code, String message) {
                callback.onFailed(code,message);
            }
        });
    }

    @Override
    public void shareMediaFile(HashMap<String, RequestBody> params, MultipartBody.Part body, String contacts, GetDataCallback<GetAllChatResponse> callback) {
        mRemoteDataSource.shareMediaFile(params,body,contacts, new GetDataCallback<GetAllChatResponse>() {
            @Override
            public void onDataReceived(GetAllChatResponse data) {
                callback.onDataReceived(data);
            }

            @Override
            public void onFailed(int code, String message) {
                callback.onFailed(code,message);
            }
        });
    }

    @Override
    public void batteryAlert(String kidId, GetResponseCallback<BaseResponse> callback) {
        mRemoteDataSource.batteryAlert(kidId, new GetResponseCallback<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse response) {
                if (response.isSuccess())
                    callback.onSuccess(response);
                else
                    callback.onFailed(response.getHttpCode(),response.getResponseMsg());
            }

            @Override
            public void onFailed(int code, String message) {
                callback.onFailed(code,message);
            }
        });

    }

    @Override
    public void updateKidSettings(HashMap<String, Object> params, GetDataCallback<GetSettingsResponse> callback) {
        mRemoteDataSource.updateKidSettings(params, new GetDataCallback<GetSettingsResponse>() {
            @Override
            public void onDataReceived(GetSettingsResponse data) {
                if(data.isSuccess())
                    callback.onDataReceived(data);
                else
                    callback.onFailed(data.getHttpCode(),data.getResponseMsg());
            }

            @Override
            public void onFailed(int code, String message) {
                callback.onFailed(code,message);
            }
        });
    }

    @Override
    public void getNotificationsList(String userId, String pageNumber, GetDataCallback<NotificationsListResponse> callback) {
        mRemoteDataSource.getNotificationsList(userId,pageNumber ,new GetDataCallback<NotificationsListResponse>() {
            @Override
            public void onDataReceived(NotificationsListResponse data) {
                if(data.isSuccess())
                callback.onDataReceived(data);
                else
                    callback.onFailed(data.getHttpCode(),data.getResponseMsg());
            }

            @Override
            public void onFailed(int code, String message) {
                callback.onFailed(code,message);
            }
        });
    }
}
