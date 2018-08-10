package com.wiser.kids.ui.camera.editor;

import android.util.Log;

import com.wiser.kids.model.response.BaseResponse;
import com.wiser.kids.model.response.GetFavContactResponse;
import com.wiser.kids.source.DataSource;
import com.wiser.kids.source.Repository;
import com.wiser.kids.ui.home.contact.ContactEntity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PhotoEditorPresenter implements PhotoEditorContract.Presenter {


    private static final String TAG = PhotoEditorPresenter.class.getName();
    private PhotoEditorContract.View view;
    private Repository repository;
    private String userId;
    private List<ContactEntity> favPeopleList;

    public PhotoEditorPresenter(PhotoEditorContract.View view, String userId ,Repository repository) {
        this.view = view;
        this.userId = userId;
        this.repository = repository;
        this.view.setPresenter(this);
        favPeopleList =  new ArrayList<>();
    }

    @Override
    public void sharePicToFav(List<String> contacts, int media_type, File file) {
        view.showProgress();
        HashMap<String,Object> params = new HashMap<>();
        params.put("File",file);
        params.put("type",media_type);
        params.put("contact_ids",contacts);
        params.put("user_id",userId);
    repository.shareMedia(params, new DataSource.GetDataCallback<BaseResponse>() {
        @Override
        public void onDataReceived(BaseResponse data) {
            view.hideProgress();
            if(data.isSuccess()) {
                view.showMessage(data.getResponseMsg());
                view.onPicShared();
            }
        }

        @Override
        public void onFailed(int code, String message) {
            view.hideProgress();
            view.showMessage(message);
        }
    });
    }

    @Override
    public void loadFavPeoples() {
        repository.getFavContacts(userId,new DataSource.GetDataCallback<GetFavContactResponse>() {

            @Override
            public void onDataReceived(GetFavContactResponse data) {

                if(data.isSuccess()) {
                    favPeopleList.clear();
                    favPeopleList.addAll(data.getContactEntityList());
                    view.onFavPeopleLoaded(favPeopleList);
                }else{
                    view.showMessage(data.getResponseMsg());
                }


            }

            @Override
            public void onFailed(int code, String message) {
                Log.d(TAG, "onFailed: "+ message);
            }
        });
    }

    @Override
    public void start() {

    }


}
