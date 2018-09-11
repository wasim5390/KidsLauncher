package com.uiu.kids.source;


import android.util.Log;

import com.uiu.kids.Constant;
import com.uiu.kids.model.request.CreateDefaultSlidesRequest;
import com.uiu.kids.model.request.CreateSlideRequest;
import com.uiu.kids.model.request.FavAppsRequest;
import com.uiu.kids.model.request.FavLinkRequest;
import com.uiu.kids.model.request.FavSOSRequest;
import com.uiu.kids.model.request.HelperListRequest;
import com.uiu.kids.model.request.LoginRequest;
import com.uiu.kids.model.response.APIError;
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
import com.uiu.kids.ui.favorite.people.Contact;
import com.uiu.kids.ui.home.contact.ContactEntity;
import com.uiu.kids.util.Util;

import java.util.HashMap;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by sidhu on 4/11/2018.
 */

public class RemoteDataSource implements DataSource, Constant {

    private static RemoteDataSource instance;
    private static final String ERROR_MESSAGE = "Fail";

    private RemoteDataSource() {

    }

    public static RemoteDataSource getInstance() {
        if (instance == null) {
            instance = new RemoteDataSource();
        }
        return instance;
    }

    @Override
    public void createAccount(HashMap<String, Object> params, final GetResponseCallback<GetAccountResponse> callback) {
        Call<GetAccountResponse> call = RetrofitHelper.getInstance().getApi().createAccount(params);
        call.enqueue(new Callback<GetAccountResponse>() {
            @Override
            public void onResponse(Call<GetAccountResponse> call, Response<GetAccountResponse> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    String errorMsg=response.message();

                    callback.onFailed(response.code(), errorMsg);
                }
            }

            @Override
            public void onFailure(Call<GetAccountResponse> call, Throwable t) {
                callback.onFailed(0, ERROR_MESSAGE);
            }
        });
    }

    @Override
    public void getRegdContacts(String userId, GetDataCallback<GetFavContactResponse> callback) {
        Call<GetFavContactResponse> call = RetrofitHelper.getInstance().getApi().getRegisteredContacts(userId);
        call.enqueue(new Callback<GetFavContactResponse>() {
            @Override
            public void onResponse(Call<GetFavContactResponse> call, Response<GetFavContactResponse> response) {
                if (response.isSuccessful()) {
                    callback.onDataReceived(response.body());
                } else {
                    APIError error = Util.parseError(response);
                    callback.onFailed(error.getHttpCode(), error.getResponseMsg());
                }
            }

            @Override
            public void onFailure(Call<GetFavContactResponse> call, Throwable t) {
                Log.i("ContactEntity", "Error response--> " + t.getMessage());
                callback.onFailed(0, ERROR_MESSAGE);
            }
        });
    }

    @Override
    public void getHelpers(GetResponseCallback<HelperResponse> callback) {

        Call<HelperResponse> call = RetrofitHelper.getInstance().getApi().getHelpers();
        call.enqueue(new Callback<HelperResponse>() {
            @Override
            public void onResponse(Call<HelperResponse> call, Response<HelperResponse> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    String errorMsg=response.message();

                    callback.onFailed(response.code(), errorMsg);
                }
            }

            @Override
            public void onFailure(Call<HelperResponse> call, Throwable t) {
                callback.onFailed(0, ERROR_MESSAGE);
            }
        });
    }

    @Override
    public void saveHelper(HelperListRequest helperRequest, GetDataCallback<HelperResponse> callback) {

        Call<HelperResponse> call = RetrofitHelper.getInstance().getApi().saveHelperList(helperRequest);
        call.enqueue(new Callback<HelperResponse>() {
            @Override
            public void onResponse(Call<HelperResponse> call, Response<HelperResponse> response) {
                if(response.isSuccessful())
                    callback.onDataReceived(response.body());
                else {

                    callback.onFailed(response.code(), response.message());
                }
            }

            @Override
            public void onFailure(Call<HelperResponse> call, Throwable t) {
                callback.onFailed(0, ERROR_MESSAGE);
            }
        });

    }

    @Override
    public void savePrimaryHelper(String userId, String helperId, GetDataCallback<HelperResponse> callback) {
        Call<HelperResponse> call = RetrofitHelper.getInstance().getApi().savePrimaryHelper(userId,helperId);
        call.enqueue(new Callback<HelperResponse>() {
            @Override
            public void onResponse(Call<HelperResponse> call, Response<HelperResponse> response) {
                if(response.isSuccessful())
                    callback.onDataReceived(response.body());
                else {

                    callback.onFailed(response.code(), response.message());
                }
            }

            @Override
            public void onFailure(Call<HelperResponse> call, Throwable t) {
                callback.onFailed(0, ERROR_MESSAGE);
            }
        });
    }


    @Override
    public void getAccount(LoginRequest request, final GetDataCallback<GetAccountResponse> callback) {
        Call<GetAccountResponse> call = RetrofitHelper.getInstance().getApi().login(request);
        call.enqueue(new Callback<GetAccountResponse>() {
            @Override
            public void onResponse(Call<GetAccountResponse> call, Response<GetAccountResponse> response) {
                if (response.isSuccessful()) {
                    callback.onDataReceived(response.body());
                } else {

                    callback.onFailed(response.code(), response.message());
                }
            }

            @Override
            public void onFailure(Call<GetAccountResponse> call, Throwable t) {
                callback.onFailed(0, ERROR_MESSAGE);
            }
        });
    }

    @Override
    public void createSlide(CreateSlideRequest request, GetDataCallback<CreateSlideResponse> callback) {
        Call<CreateSlideResponse> call = RetrofitHelper.getInstance().getApi().createSlide(request);
        call.enqueue(new Callback<CreateSlideResponse>() {
            @Override
            public void onResponse(Call<CreateSlideResponse> call, Response<CreateSlideResponse> response) {
                if (response.isSuccessful()) {
                    callback.onDataReceived(response.body());
                } else {
                    String errorMsg=response.message();
                    callback.onFailed(response.code(), errorMsg);
                }
            }

            @Override
            public void onFailure(Call<CreateSlideResponse> call, Throwable t) {
                callback.onFailed(0, ERROR_MESSAGE);
            }
        });
    }

    @Override
    public void createDefaultSlides(CreateDefaultSlidesRequest request, GetDataCallback<BaseResponse> callback) {
        Call<BaseResponse> call = RetrofitHelper.getInstance().getApi().createDefaultSlides(request);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.isSuccessful()) {
                    callback.onDataReceived(response.body());
                } else {
                    String errorMsg=response.message();
                    callback.onFailed(response.code(), errorMsg);
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                callback.onFailed(0, ERROR_MESSAGE);
            }
        });
    }

    @Override
    public void deleteSlide(String slideId, GetResponseCallback<BaseResponse> callback) {
        Call<BaseResponse> call = RetrofitHelper.getInstance().getApi().deleteSlide(slideId);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    String errorMsg=response.message();
                    callback.onFailed(response.code(), errorMsg);
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                callback.onFailed(0, ERROR_MESSAGE);
            }
        });
    }

    @Override
    public void getUserSlides(String userId, GetDataCallback<GetAllSlidesResponse> callback) {
        Call<GetAllSlidesResponse> call = RetrofitHelper.getInstance().getApi().getUserSlides(userId);
        call.enqueue(new Callback<GetAllSlidesResponse>() {
            @Override
            public void onResponse(Call<GetAllSlidesResponse> call, Response<GetAllSlidesResponse> response) {
                if (response.isSuccessful()) {
                    callback.onDataReceived(response.body());
                } else {
                    String errorMsg=response.message();
                    callback.onFailed(response.code(), errorMsg);
                }
            }

            @Override
            public void onFailure(Call<GetAllSlidesResponse> call, Throwable t) {
                callback.onFailed(0, ERROR_MESSAGE);
            }
        });
    }
    @Override
    public void getFavContacts(String id, final GetDataCallback<GetFavContactResponse> callback) {

        Call<GetFavContactResponse> call = RetrofitHelper.getInstance().getApi().getFavoriteContacts(id);
        call.enqueue(new Callback<GetFavContactResponse>() {
            @Override
            public void onResponse(Call<GetFavContactResponse> call, Response<GetFavContactResponse> response) {
                if (response.isSuccessful()) {
                    callback.onDataReceived(response.body());
                } else {
                    APIError error = Util.parseError(response);
                    callback.onFailed(error.getHttpCode(), error.getResponseMsg());
                }
            }

            @Override
            public void onFailure(Call<GetFavContactResponse> call, Throwable t) {
                Log.i("ContactEntity", "Error response--> " + t.getMessage());
                callback.onFailed(0, ERROR_MESSAGE);
            }
        });
    }

    @Override
    public void addToSlide(String id, ContactEntity data, final GetDataCallback<ContactEntity> callback) {

        HashMap<String, Object> params = new HashMap<>();
        Contact cont = new Contact();
        cont.setSlideId(id);
        cont.setPhotoUrl(data.getPhotoUri());
        cont.setUserId(data.getUserId());
        cont.setContactName(data.getName());
        cont.setPhoneNumbers(data.getAllNumbers());
        cont.setContactEmail(data.getEmail());
        cont.setRequestStatus(data.getRequestStatus());

        params.put("contact", cont);

        Call<GetFavContactResponse> call = RetrofitHelper.getInstance().getApi().saveOnSlide(params);
        call.enqueue(new Callback<GetFavContactResponse>() {
            @Override
            public void onResponse(Call<GetFavContactResponse> call, Response<GetFavContactResponse> response) {
                if (response.isSuccessful()) {
                    callback.onDataReceived(response.body().getFavoriteContact());
                } else {

                    APIError error = Util.parseError(response);
                    callback.onFailed(error.getHttpCode(), error.getResponseMsg());
                }
            }

            @Override
            public void onFailure(Call<GetFavContactResponse> call, Throwable t) {
                Log.i("ContactEntity", "Error response--> " + t.getMessage());
                callback.onFailed(0, ERROR_MESSAGE);
            }
        });
    }


    @Override
    public void fetchFromSlide(String id, final GetDataCallback<GetFavContactResponse> callback) {

        Call<GetFavContactResponse> call = RetrofitHelper.getInstance().getApi().fetchFromSlide(id);
        call.enqueue(new Callback<GetFavContactResponse>() {
            @Override
            public void onResponse(Call<GetFavContactResponse> call, Response<GetFavContactResponse> response) {
                if (response.isSuccessful()) {
                    callback.onDataReceived(response.body());
                } else {
                    APIError error = Util.parseError(response);
                    callback.onFailed(error.getHttpCode(), error.getResponseMsg());
                }
            }

            @Override
            public void onFailure(Call<GetFavContactResponse> call, Throwable t) {
                Log.i("ContactEntity", "Error response--> " + t.getMessage());
                callback.onFailed(0, ERROR_MESSAGE);
            }
        });
    }

    @Override
    public void addFavAppToSlide(FavAppsRequest appsRequest, GetDataCallback<GetFavAppsResponse> callback) {
        Call<GetFavAppsResponse> call = RetrofitHelper.getInstance().getApi().saveAppOnSlide(appsRequest);
        call.enqueue(new Callback<GetFavAppsResponse>() {
            @Override
            public void onResponse(Call<GetFavAppsResponse> call, Response<GetFavAppsResponse> response) {
                if(response.isSuccessful())
                    callback.onDataReceived(response.body());
                else {

                    callback.onFailed(response.code(), response.message());
                }
            }

            @Override
            public void onFailure(Call<GetFavAppsResponse> call, Throwable t) {
                callback.onFailed(0, ERROR_MESSAGE);
            }
        });
    }

    @Override
    public void getFavApps(String userId, GetDataCallback<GetFavAppsResponse> callback) {
        Call<GetFavAppsResponse> call = RetrofitHelper.getInstance().getApi().getFavApps(userId);
        call.enqueue(new Callback<GetFavAppsResponse>() {
            @Override
            public void onResponse(Call<GetFavAppsResponse> call, Response<GetFavAppsResponse> response) {
                if(response.isSuccessful())
                    callback.onDataReceived(response.body());
                else {

                    callback.onFailed(response.code(), response.message());
                }
            }

            @Override
            public void onFailure(Call<GetFavAppsResponse> call, Throwable t) {
                callback.onFailed(0, ERROR_MESSAGE);
            }
        });
    }

    @Override
    public void getFavLinkIcon(String url, GetDataCallback<GetFavLinkIconResponce> callback) {

        Call<GetFavLinkIconResponce> call = RetrofitHelper.getIconApi().getLinkIcon(url);
        call.enqueue(new Callback<GetFavLinkIconResponce>() {
            @Override
            public void onResponse(Call<GetFavLinkIconResponce> call, Response<GetFavLinkIconResponce> response) {
                if(response.isSuccessful())
                    callback.onDataReceived(response.body());
                else {

                    callback.onFailed(response.code(), response.message());
                }
            }

            @Override
            public void onFailure(Call<GetFavLinkIconResponce> call, Throwable t) {
                callback.onFailed(0, ERROR_MESSAGE);
            }
        });

    }

    @Override
    public void addFavLinkToSlide(FavLinkRequest linkRequest, GetDataCallback<GetFavLinkResponse> callback) {

        Call<GetFavLinkResponse> call = RetrofitHelper.getInstance().getApi().saveLinkOnSlide(linkRequest);
        call.enqueue(new Callback<GetFavLinkResponse>() {
            @Override
            public void onResponse(Call<GetFavLinkResponse> call, Response<GetFavLinkResponse> response) {
                if(response.isSuccessful())
                    callback.onDataReceived(response.body());
                else {

                    callback.onFailed(response.code(), response.message());
                }
            }

            @Override
            public void onFailure(Call<GetFavLinkResponse> call, Throwable t) {
                callback.onFailed(0, ERROR_MESSAGE);
            }
        });

    }

    @Override
    public void getFavLinks(String userId, GetDataCallback<GetFavLinkResponse> callback) {

        Call<GetFavLinkResponse> call = RetrofitHelper.getInstance().getApi().getFavLinks(userId);
        call.enqueue(new Callback<GetFavLinkResponse>() {
            @Override
            public void onResponse(Call<GetFavLinkResponse> call, Response<GetFavLinkResponse> response) {
                if(response.isSuccessful())
                    callback.onDataReceived(response.body());
                else {

                    callback.onFailed(response.code(), response.message());
                }
            }

            @Override
            public void onFailure(Call<GetFavLinkResponse> call, Throwable t) {
                callback.onFailed(0, ERROR_MESSAGE);
            }
        });

    }

    @Override
    public void addSOSToSlide(FavSOSRequest favSOSRequest, GetDataCallback<GetSOSResponse> callback) {

        Call<GetSOSResponse> call = RetrofitHelper.getInstance().getApi().saveSOSOnSlide(favSOSRequest);
        call.enqueue(new Callback<GetSOSResponse>() {
            @Override
            public void onResponse(Call<GetSOSResponse> call, Response<GetSOSResponse> response) {
                if(response.isSuccessful())
                    callback.onDataReceived(response.body());
                else {

                    callback.onFailed(response.code(), response.message());
                }
            }

            @Override
            public void onFailure(Call<GetSOSResponse> call, Throwable t) {
                callback.onFailed(0, ERROR_MESSAGE);
            }
        });
    }

    @Override
    public void fetchSOSForSlide(String id, GetDataCallback<GetSOSResponse> callback) {

        Call<GetSOSResponse> call = RetrofitHelper.getInstance().getApi().getSOS(id);
        call.enqueue(new Callback<GetSOSResponse>() {
            @Override
            public void onResponse(Call<GetSOSResponse> call, Response<GetSOSResponse> response) {
                if(response.isSuccessful())
                    callback.onDataReceived(response.body());
                else {

                    callback.onFailed(response.code(), response.message());
                }
            }

            @Override
            public void onFailure(Call<GetSOSResponse> call, Throwable t) {
                callback.onFailed(0, ERROR_MESSAGE);
            }
        });

    }

    @Override
    public void fetchReminderList(String user_id, GetDataCallback<ReminderResponse> callback) {

        Call<ReminderResponse> call = RetrofitHelper.getInstance().getApi().getReminderList(user_id);
        call.enqueue(new Callback<ReminderResponse>() {
            @Override
            public void onResponse(Call<ReminderResponse> call, Response<ReminderResponse> response) {
                if(response.isSuccessful())
                    callback.onDataReceived(response.body());
                else {

                    callback.onFailed(response.code(), response.message());
                }
            }

            @Override
            public void onFailure(Call<ReminderResponse> call, Throwable t) {
                callback.onFailed(0, ERROR_MESSAGE);
            }
        });

    }

    @Override
    public void shareMedia(HashMap<String, RequestBody> params, MultipartBody.Part file, List<String> contacts, GetDataCallback<BaseResponse> callback) {
        Call<BaseResponse> call = RetrofitHelper.getInstance().getApi().shareMedia(params,file,contacts);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if(response.isSuccessful())
                    callback.onDataReceived(response.body());
                else {

                    callback.onFailed(response.code(), response.message());
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                callback.onFailed(0, ERROR_MESSAGE);
            }
        });
    }

    @Override
    public void updateKidsLocation(HashMap<String, Object> params, GetResponseCallback<BaseResponse> callback) {
        Call<BaseResponse> call = RetrofitHelper.getInstance().getApi().updateKidsLocation(params);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if(response.isSuccessful())
                    callback.onSuccess(response.body());
                else {

                    callback.onFailed(response.code(), response.message());
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                callback.onFailed(0, ERROR_MESSAGE);
            }
        });
    }

    @Override
    public void getKidDirections(String userId, GetDataCallback<GetDirectionsResponse> callback) {
        Call<GetDirectionsResponse> call = RetrofitHelper.getInstance().getApi().getDirections(userId);
        call.enqueue(new Callback<GetDirectionsResponse>() {
            @Override
            public void onResponse(Call<GetDirectionsResponse> call, Response<GetDirectionsResponse> response) {
                if(response.isSuccessful())
                    callback.onDataReceived(response.body());
                else {

                    callback.onFailed(response.code(), response.message());
                }
            }

            @Override
            public void onFailure(Call<GetDirectionsResponse> call, Throwable t) {
                callback.onFailed(0, ERROR_MESSAGE);
            }
        });
    }

    @Override
    public void getChatMessageList(String userId, String contactId, GetDataCallback<GetAllChatResponse> callback) {
        Call<GetAllChatResponse> call = RetrofitHelper.getInstance().getApi().getMessageList(userId,contactId);
        call.enqueue(new Callback<GetAllChatResponse>() {
            @Override
            public void onResponse(Call<GetAllChatResponse> call, Response<GetAllChatResponse> response) {
                if(response.isSuccessful())
                    callback.onDataReceived(response.body());
                else {

                    callback.onFailed(response.code(), response.message());
                }
            }

            @Override
            public void onFailure(Call<GetAllChatResponse> call, Throwable t) {
                callback.onFailed(0, ERROR_MESSAGE);
            }
        });
    }

    @Override
    public void shareMediaFile(HashMap<String, RequestBody> params, MultipartBody.Part file, String contact, GetDataCallback<GetAllChatResponse> callback) {
        Call<GetAllChatResponse> call = RetrofitHelper.getInstance().getApi().shareMediaFile(params,file,contact);
        call.enqueue(new Callback<GetAllChatResponse>() {
            @Override
            public void onResponse(Call<GetAllChatResponse> call, Response<GetAllChatResponse> response) {
                if(response.isSuccessful())
                    callback.onDataReceived(response.body());
                else {

                    callback.onFailed(response.code(), response.message());
                }
            }

            @Override
            public void onFailure(Call<GetAllChatResponse> call, Throwable t) {
                callback.onFailed(0, ERROR_MESSAGE);
            }
        });
    }


}