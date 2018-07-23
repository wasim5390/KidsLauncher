package com.wiser.kids.source;

import android.support.annotation.NonNull;


import com.wiser.kids.model.ContactPerson;
import com.wiser.kids.model.response.GetAccountResponse;
import com.wiser.kids.ui.home.contact.ContactEntity;

import java.util.HashMap;
import java.util.List;

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
    public void getAccount(String header, final GetDataCallback<GetAccountResponse> callback) {
        mRemoteDataSource.getAccount(header,new GetDataCallback<GetAccountResponse>() {
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


    public void fetchFromSlide(String id,final GetDataCallback<List<ContactEntity>> callback) {
        mRemoteDataSource.fetchFromSlide(id,new GetDataCallback<List<ContactEntity>>() {
            @Override
            public void onDataReceived(List<ContactEntity> data) {
                callback.onDataReceived(data);
            }

            @Override
            public void onFailed(int code, String message) {
                callback.onFailed(code, message);
            }
        });
    }


}
