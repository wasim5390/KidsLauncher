package com.wiser.kids.source;


import android.util.Log;

import com.wiser.kids.Constant;
import com.wiser.kids.model.request.CreateDefaultSlidesRequest;
import com.wiser.kids.model.request.CreateSlideRequest;
import com.wiser.kids.model.request.FavAppsRequest;
import com.wiser.kids.model.request.FavLinkRequest;
import com.wiser.kids.model.request.FavSOSRequest;
import com.wiser.kids.model.request.LoginRequest;
import com.wiser.kids.model.response.APIError;
import com.wiser.kids.model.response.BaseResponse;
import com.wiser.kids.model.response.CreateSlideResponse;
import com.wiser.kids.model.response.GetAccountResponse;
import com.wiser.kids.model.response.GetAllSlidesResponse;
import com.wiser.kids.model.response.GetFavAppsResponse;
import com.wiser.kids.model.response.GetFavContactResponse;
import com.wiser.kids.model.response.GetFavLinkIconResponce;
import com.wiser.kids.model.response.GetFavLinkResponse;
import com.wiser.kids.model.response.GetSOSResponse;
import com.wiser.kids.model.response.ReminderResponse;
import com.wiser.kids.ui.favorite.people.Contact;
import com.wiser.kids.ui.home.apps.AppsEntity;
import com.wiser.kids.ui.home.contact.ContactEntity;
import com.wiser.kids.util.PreferenceUtil;
import com.wiser.kids.util.Util;

import java.util.HashMap;
import java.util.List;

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

        Call<GetFavLinkIconResponce> call =RetrofitHelper.getIconApi().getLinkIcon(url);
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


}
