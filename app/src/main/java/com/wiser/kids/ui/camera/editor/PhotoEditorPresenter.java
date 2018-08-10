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

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

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

        RequestBody fBody = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file",file.getName(),fBody);

        RequestBody mediaType = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(media_type));
        RequestBody user_id = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(userId));

        HashMap<String,RequestBody> params = new HashMap<>();
        params.put("type",mediaType);
        params.put("user_id",user_id);
        repository.shareMedia(params,body,contacts, new DataSource.GetDataCallback<BaseResponse>() {
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
