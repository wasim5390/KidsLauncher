package com.uiu.kids.ui.home.helper;

import com.uiu.kids.model.User;
import com.uiu.kids.model.request.HelperListRequest;
import com.uiu.kids.model.response.HelperResponse;
import com.uiu.kids.source.DataSource;
import com.uiu.kids.source.Repository;
import com.uiu.kids.util.PreferenceUtil;

import java.util.ArrayList;
import java.util.List;

public class HelperPresenter implements HelperContract.Presenter {

    public HelperContract.view view;
    public PreferenceUtil preferenceUtil;
    public Repository repository;
    public List<HelperEntity> mHelperList;
    public HelperEntity primaryParent;
    public List<String> helpesID;


    public HelperPresenter(HelperContract.view view, PreferenceUtil preferenceUtil, Repository repository) {
        this.view = view;
        this.preferenceUtil = preferenceUtil;
        this.repository = repository;
        view.setPresenter(this);
    }

    @Override
    public void start() {
        mHelperList = new ArrayList<>();
        helpesID=new ArrayList<>();
        primaryParent = preferenceUtil.getAccount().getPrimaryHelper();
        if(preferenceUtil.getAccount().getPrimaryHelper()==null)
            view.onPrimarySelection(true);
        loadHelpers();
    }

    private void loadHelpers() {

        repository.getHelpers(new DataSource.GetResponseCallback<HelperResponse>() {
            @Override
            public void onSuccess(HelperResponse response) {

                if (response.success) {
                    mHelperList.addAll(response.getHelperEntities());
                    if(primaryParent!=null)
                        for(HelperEntity entity: mHelperList){
                            if(entity.getId().equals(primaryParent.getId()))
                                mHelperList.remove(entity);
                        }
                    view.loadHelperList(mHelperList);
                }

            }

            @Override
            public void onFailed(int code, String message) {
                view.loadHelperList(mHelperList);
                view.showMessage(message);
            }
        });
    }

    @Override
    public void updateHelpers(List<HelperEntity> selectedHelpers) {
        saveHelpersList(selectedHelpers);
    }

    @Override
    public void savePrimaryHelper(String helperId) {
        view.showProgress();
        repository.savePrimaryHelper(preferenceUtil.getAccount().getId(), helperId, new DataSource.GetDataCallback<HelperResponse>() {
            @Override
            public void onDataReceived(HelperResponse data) {
                view.hideProgress();
                if(data.isSuccess())
                    view.onHelpersSaved();
                else
                    view.showMessage(data.getMessage());
            }

            @Override
            public void onFailed(int code, String message) {
                view.hideProgress();
                view.showMessage(message);
            }
        });
    }

    @Override
    public void getPrimaryHelper() {
        view.setPrimaryHelper(preferenceUtil.getAccount().getPrimaryHelper());
    }


    private void saveHelpersList(List<HelperEntity> approveHelperList) {
        HelperListRequest helperListRequest = new HelperListRequest();

        for (int i=0;i<approveHelperList.size();i++)
        {
            helpesID.add(approveHelperList.get(i).id);
        }

        helperListRequest.setUserId(preferenceUtil.getAccount().getId());
        helperListRequest.setHelpersID(helpesID);
        view.showProgress();
        repository.saveHelper(helperListRequest, new DataSource.GetDataCallback<HelperResponse>() {
            @Override
            public void onDataReceived(HelperResponse data) {
                view.hideProgress();
                User user = preferenceUtil.getAccount();
                user.setHelpers(approveHelperList);
                preferenceUtil.saveAccount(user);
                view.onHelpersSaved();
            }

            @Override
            public void onFailed(int code, String message) {
                view.hideProgress();
                view.showMessage(message);
            }
        });

    }
}
