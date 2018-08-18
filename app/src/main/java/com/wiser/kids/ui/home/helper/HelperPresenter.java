package com.wiser.kids.ui.home.helper;

import com.wiser.kids.model.User;
import com.wiser.kids.model.request.HelperListRequest;
import com.wiser.kids.model.response.HelperResponse;
import com.wiser.kids.source.DataSource;
import com.wiser.kids.source.Repository;
import com.wiser.kids.util.PreferenceUtil;

import java.util.ArrayList;
import java.util.List;

public class HelperPresenter implements HelperContract.Presenter {

    public HelperContract.view view;
    public PreferenceUtil preferenceUtil;
    public Repository repository;
    public List<HelperEntity> mHelperList;
    public List<HelperEntity> mSavedParents;
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
        mSavedParents = preferenceUtil.getAccount().getHelpers();
        loadLost();
    }

    private void loadLost() {

        repository.getHelpers(new DataSource.GetResponseCallback<HelperResponse>() {
            @Override
            public void onSuccess(HelperResponse response) {

                if (response.success) {
                    mHelperList.addAll(response.getHelperEntities());
                    for(HelperEntity entity: mHelperList){
                        for(HelperEntity savedParent: mSavedParents){
                            if(entity.getId().equals(savedParent.getId()))
                                entity.setHelperSelected(true);
                        }
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