package com.uiu.kids.ui.home;

import com.uiu.kids.Constant;
import com.uiu.kids.model.User;
import com.uiu.kids.model.response.BaseResponse;
import com.uiu.kids.model.response.GetSOSResponse;
import com.uiu.kids.model.response.UploadProfileImageResponse;
import com.uiu.kids.source.DataSource;
import com.uiu.kids.source.Repository;
import com.uiu.kids.util.PreferenceUtil;
import com.uiu.kids.util.Util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class HomePresenter implements HomeContract.Presenter,Constant {

    private HomeContract.View view;
    private Repository repository;
    private PreferenceUtil preferenceUtil;
    private boolean sosLoaded=false;

    public HomePresenter(HomeContract.View view, PreferenceUtil preferenceUtil, Repository repository) {
        this.view = view;
        this.preferenceUtil = preferenceUtil;
        this.repository = repository;
        this.view.setPresenter(this);
    }


    @Override
    public void getSlideItems() {
        List<String> slideItemList= new ArrayList<>();
        slideItemList.add(DIALER);
        slideItemList.add(CAMERA);
        slideItemList.add(C_ME);
        slideItemList.add(SOS);
        view.slideItemsLoaded(slideItemList);
    }

    @Override
    public void updateProfilePic(File imageFile) {
        if(!Util.isInternetAvailable())
            return;
        RequestBody fBody = RequestBody.create(MediaType.parse("image/*"), imageFile);
        MultipartBody.Part body = MultipartBody.Part.createFormData("image",imageFile.getName(),fBody);

        RequestBody user_id = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(preferenceUtil.getAccount().getId()));

        HashMap<String, RequestBody> params = new HashMap<>();
        params.put("user_id", user_id);
        repository.uploadProfileImage(params, body, new DataSource.GetResponseCallback<UploadProfileImageResponse>() {
            @Override
            public void onSuccess(UploadProfileImageResponse response) {
                if(response.getLink()!=null)
                {
                    User user =preferenceUtil.getAccount();
                    user.setImageLink(response.getLink());
                    preferenceUtil.saveAccount(user);
                }
            }

            @Override
            public void onFailed(int code, String message) {
                view.showMessage(message);
            }
        });
    }


    @Override
    public void start() {
        loadSOSList();
    }

    public void loadSOSList() {
        if(!Util.isInternetAvailable()) {
            return;
        }
        if(preferenceUtil.getAllSosList()==null)
            repository.fetchUserAllSOS(preferenceUtil.getAccount().getId(), new DataSource.GetDataCallback<GetSOSResponse>() {
                @Override
                public void onDataReceived(GetSOSResponse data) {
                    if (data.isSuccess()) {
                        sosLoaded=true;
                        preferenceUtil.saveAllSos(data.getContactEntityList());
                    }
                }

                @Override
                public void onFailed(int code, String message) {


                }
            });

    }

}
