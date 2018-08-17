package com.wiser.kids.ui.home.Helper;

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


    public HelperPresenter(HelperContract.view view, PreferenceUtil preferenceUtil, Repository repository) {
        this.view = view;
        this.preferenceUtil = preferenceUtil;
        this.repository = repository;
        view.setPresenter(this);
    }

    @Override
    public void start() {
        mHelperList=new ArrayList<>();
        loadLost();
    }

    private void loadLost() {

        repository.getHelpers(new DataSource.GetResponseCallback<HelperResponse>() {
            @Override
            public void onSuccess(HelperResponse response) {

                if(response.success)
                {
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
}
