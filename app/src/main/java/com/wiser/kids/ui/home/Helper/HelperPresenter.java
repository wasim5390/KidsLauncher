package com.wiser.kids.ui.home.Helper;

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
    public List<HelperEntity> approveHelperList;
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
        approveHelperList = new ArrayList<>();
        helpesID=new ArrayList<>();
        loadLost();
    }

    private void loadLost() {

        repository.getHelpers(new DataSource.GetResponseCallback<HelperResponse>() {
            @Override
            public void onSuccess(HelperResponse response) {

                if (response.success) {
                    mHelperList.addAll(response.getHelperEntities());
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
    public void addHelper(HelperEntity helper) {

        approveHelperList.clear();
        approveHelperList.addAll(preferenceUtil.getAccount().getHelpers());
        approveHelperList.add(helper);
        preferenceUtil.getAccount().setHelpers(approveHelperList);
        saveHelpersList(approveHelperList);
    }

    @Override
    public void removeHelper(HelperEntity Helper) {

        approveHelperList.clear();
        approveHelperList.addAll(preferenceUtil.getAccount().getHelpers());

        for (int i = 0; i < approveHelperList.size(); i++) {
            if (approveHelperList.get(i).getId().equalsIgnoreCase(Helper.id)) {
                approveHelperList.remove(i);
            }
        }
        preferenceUtil.getAccount().setHelpers(approveHelperList);

        saveHelpersList(approveHelperList);

    }

    private void saveHelpersList(List<HelperEntity> approveHelperList) {
        HelperListRequest helperListRequest = new HelperListRequest();

        for (int i=0;i<approveHelperList.size();i++)
        {
            helpesID.add(approveHelperList.get(i).id);
        }

        helperListRequest.setUserId(preferenceUtil.getAccount().getId());
        helperListRequest.setHelpersID(helpesID);

        repository.saveHelper(helperListRequest, new DataSource.GetDataCallback<HelperResponse>() {
            @Override
            public void onDataReceived(HelperResponse data) {
                view.showMessage(data.getMessage());
            }

            @Override
            public void onFailed(int code, String message) {

                view.showMessage(message);
            }
        });

    }
}
