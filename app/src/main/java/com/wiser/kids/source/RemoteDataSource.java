package com.wiser.kids.source;


import android.util.Log;

import com.wiser.kids.Constant;
import com.wiser.kids.model.response.APIError;
import com.wiser.kids.model.response.GetAccountResponse;
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
                    String errorMsg;
                    APIError error = Util.parseError(response);


                    if (error.getHttpCode() == BAD_REQUEST) {
                        if (error.getResponseData().getEmail_address() != null) {
                            errorMsg = error.getResponseData().getEmail_address()[0];
                        } else if (error.getResponseData().getMobile() != null) {
                            errorMsg = error.getResponseData().getMobile()[0];
                        } else {
                            errorMsg = error.getResponseMsg();
                        }
                    } else {
                        errorMsg = error.getResponseMsg();
                    }

                    callback.onFailed(error.getHttpCode(), errorMsg);
                }
            }

            @Override
            public void onFailure(Call<GetAccountResponse> call, Throwable t) {
                callback.onFailed(0, ERROR_MESSAGE);
            }
        });
    }


    @Override
    public void getAccount(String authorization, final GetDataCallback<GetAccountResponse> callback) {
        Call<GetAccountResponse> call = RetrofitHelper.getInstance().getApi().getAccount(authorization);
        call.enqueue(new Callback<GetAccountResponse>() {
            @Override
            public void onResponse(Call<GetAccountResponse> call, Response<GetAccountResponse> response) {
                if (response.isSuccessful()) {
                    callback.onDataReceived(response.body());
                } else {
                    APIError error = Util.parseError(response);
                    callback.onFailed(error.getHttpCode(), error.getResponseMsg());
                }
            }

            @Override
            public void onFailure(Call<GetAccountResponse> call, Throwable t) {
                callback.onFailed(0, ERROR_MESSAGE);
            }
        });
    }


    @Override
    public void addToSlide(String id, ContactEntity data, final GetDataCallback<ContactEntity> callback) {

        HashMap<String, Object> params = new HashMap<>();
        ContactEntity cont = new ContactEntity();
        /*cont.setId(Integer.parseInt(id));*/
        cont.setUserId(4567);
        cont.setSlideId("5ac610cea45f12274c2fca5a");
        cont.setName(data.getName());
        cont.setmPhoneNumber(data.getmPhoneNumber());
        //cont.setContactIcon();
        //cont.setPhotoUrl(Integer.parseInt(data.getPhotoUri()));
        cont.setEmail(data.getEmail());
        cont.setRequestStatus(data.getRequestStatus());

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


}
