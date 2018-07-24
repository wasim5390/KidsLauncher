package com.wiser.kids.source;


import android.util.Log;

import com.wiser.kids.Constant;
import com.wiser.kids.model.request.CreateDefaultSlidesRequest;
import com.wiser.kids.model.request.CreateSlideRequest;
import com.wiser.kids.model.request.FavAppsRequest;
import com.wiser.kids.model.request.LoginRequest;
import com.wiser.kids.model.response.APIError;
import com.wiser.kids.model.response.BaseResponse;
import com.wiser.kids.model.response.CreateSlideResponse;
import com.wiser.kids.model.response.GetAccountResponse;
import com.wiser.kids.model.response.GetAllSlidesResponse;
import com.wiser.kids.model.response.GetFavAppsResponse;
import com.wiser.kids.ui.favorite.people.Contact;
import com.wiser.kids.ui.home.apps.AppsEntity;
import com.wiser.kids.ui.home.contact.ContactEntity;
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
    public void addToSlide(String id, ContactEntity data, final GetDataCallback<ContactEntity> callback) {

        HashMap<String, Object> params = new HashMap<>();
        Contact cont = new Contact();
        /*cont.setId(Integer.parseInt(id));*/
        cont.setId(4567);
        cont.setSlideId("5ac610cea45f12274c2fca5a");
        cont.setContactName(data.getName());
        cont.setPhoneNumber(data.getmPhoneNumber());
        //cont.setContactIcon();
        //cont.setPhotoUrl(Integer.parseInt(data.getPhotoUri()));
        cont.setContactEmail(data.getEmail());
        cont.setRequestStatus(String.valueOf(data.getRequestStatus()));

        params.put("contact", cont);

        Call<ContactEntity> call = RetrofitHelper.getInstance().getApi().saveOnSlide(params);
        call.enqueue(new Callback<ContactEntity>() {
            @Override
            public void onResponse(Call<ContactEntity> call, Response<ContactEntity> response) {
                if (response.isSuccessful()) {
                    callback.onDataReceived(response.body());
                } else {
                    Log.i("ContactEntity", "Error response-->" + response.body().toString());
                    APIError error = Util.parseError(response);
                    callback.onFailed(error.getHttpCode(), error.getResponseMsg());
                }
            }

            @Override
            public void onFailure(Call<ContactEntity> call, Throwable t) {
                Log.i("ContactEntity", "Error response--> " + t.getMessage());
                callback.onFailed(0, ERROR_MESSAGE);
            }
        });
    }


    @Override
    public void fetchFromSlide(String id, final GetDataCallback<List<ContactEntity>> callback) {

        Call<List<ContactEntity>> call = RetrofitHelper.getInstance().getApi().fetchFromSlide(id);
        call.enqueue(new Callback<List<ContactEntity>>() {
            @Override
            public void onResponse(Call<List<ContactEntity>> call, Response<List<ContactEntity>> response) {
                if (response.isSuccessful()) {
                    callback.onDataReceived(response.body());
                } else {
                    APIError error = Util.parseError(response);
                    callback.onFailed(error.getHttpCode(), error.getResponseMsg());
                }
            }

            @Override
            public void onFailure(Call<List<ContactEntity>> call, Throwable t) {
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




}
