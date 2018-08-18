package com.wiser.kids.source;

import android.support.annotation.NonNull;



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
    public void getHelpers(GetResponseCallback<HelperResponse> callback) {
        mRemoteDataSource.getHelpers(new GetResponseCallback<HelperResponse>() {
            @Override
            public void onSuccess(HelperResponse response) {
                callback.onSuccess(response);
            }

            @Override
            public void onFailed(int code, String message) {
                callback.onFailed(code,message);
            }
        });
    }

    @Override
    public void saveHelper(HelperListRequest helperRequest, GetDataCallback<HelperResponse> callback) {
        mRemoteDataSource.saveHelper(helperRequest, new GetDataCallback<HelperResponse>() {
            @Override
            public void onDataReceived(HelperResponse data) {
                callback.onDataReceived(data);
            }

            @Override
            public void onFailed(int code, String message) {
                callback.onFailed(code,message);
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

    public void addToSlide(String id,ContactEntity cont,final GetDataCallback<ContactEntity> callback) {
        mRemoteDataSource.addToSlide(id,cont,new GetDataCallback<ContactEntity>() {
            @Override
            public void onDataReceived(ContactEntity data) {
                callback.onDataReceived(data);
            }

            @Override
            public void onFailed(int code, String message) {
                callback.onFailed(code, message);
            }
        });
    }


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
}
