package com.uiu.kids.ui.message;

import com.uiu.kids.model.response.BaseResponse;
import com.uiu.kids.model.response.GetFavContactResponse;
import com.uiu.kids.source.DataSource;
import com.uiu.kids.source.Repository;
import com.uiu.kids.ui.home.contact.ContactEntity;
import com.uiu.kids.util.PreferenceUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class MessagePresenter implements MessageContract.Presenter {

    public MessageContract.View view;
    public PreferenceUtil preferenceUtil;
    public Repository repository;
    public List<ContactEntity> registerContactList;

    public MessagePresenter(MessageContract.View view, PreferenceUtil preferenceUtil, Repository repository) {
        this.view = view;
        this.preferenceUtil = preferenceUtil;
        this.repository = repository;
        view.setPresenter(this);
    }





    @Override
    public void start() {
        registerContactList = new ArrayList<>();
        loadRegisterPeople();
    }

    @Override
    public void loadRegisterPeople() {

        repository.getFavContacts(preferenceUtil.getAccount().getId(), new DataSource.GetDataCallback<GetFavContactResponse>() {
            @Override
            public void onDataReceived(GetFavContactResponse data) {
                if (data != null) {
                    if (data.isSuccess()) {
                        registerContactList.clear();
                        registerContactList.addAll(data.getRegdContactEntityList());
                        view.loadPeople(registerContactList);
                    }
                }


            }

            @Override
            public void onFailed(int code, String message) {

            }
        });

    }

    @Override
    public void shareFileToContact(List<ContactEntity> list, File file, int type) {

        RequestBody fBody = null;
        if (list.isEmpty()) {
            view.showMessage("Please select any Contact");

        } else {

            List<String> contactIds = new ArrayList<>();
            for (ContactEntity entity : list) {
                contactIds.add(entity.getId());
            }
            if(type==2) {
                fBody = RequestBody.create(MediaType.parse("video/*"), file);
            }
            else if(type==3)
            {
                 fBody = RequestBody.create(MediaType.parse("audio/*"), file);

            }
            MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), fBody);

            RequestBody mediaType = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(type));
            RequestBody user_id = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(preferenceUtil.getAccount().getId()));

            HashMap<String, RequestBody> params = new HashMap<>();
            params.put("type", mediaType);
            params.put("user_id", user_id);
            view.showProgress();
            repository.shareMedia(params, body, contactIds, new DataSource.GetDataCallback<BaseResponse>() {
                @Override
                public void onDataReceived(BaseResponse data) {
                    view.hideProgress();
                    if (data.isSuccess()) {
                        view.showMessage(data.getResponseMsg());
                        file.delete();
                        view.onFileShared();
                    }
                }
                @Override
                public void onFailed(int code, String message) {
                    view.hideProgress();
                    view.showMessage(message);
                }
            });

        }
    }
}
