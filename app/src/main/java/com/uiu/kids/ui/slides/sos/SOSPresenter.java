package com.uiu.kids.ui.slides.sos;

import com.uiu.kids.model.Slide;
import com.uiu.kids.model.request.FavSOSRequest;
import com.uiu.kids.model.response.GetSOSResponse;
import com.uiu.kids.source.DataSource;
import com.uiu.kids.source.Repository;
import com.uiu.kids.ui.home.contact.ContactEntity;
import com.uiu.kids.util.PreferenceUtil;
import com.uiu.kids.util.Util;

import java.util.ArrayList;
import java.util.List;

public class SOSPresenter implements SOSContract.Presenter {

    public SOSContract.View view;
    public Repository repository;
    public Slide slideItem;
    private List<ContactEntity> mSosList;
    private List<ContactEntity> mhasaccessList;
    public PreferenceUtil preferenceUtil;
    public boolean isItemAdded = false;
    public boolean isLoading=false;

    @Override
    public void start() {
        mSosList = new ArrayList<>();
        mhasaccessList=new ArrayList<>();
        view.slideSerial(slideItem.getSerial(),slideItem.getCount());
        loadFromLocal();
        loadSOSList();
    }


    public SOSPresenter(SOSContract.View view, Slide slideItem, PreferenceUtil preferenceUtil, Repository repository) {
        this.repository = repository;
        this.slideItem = slideItem;
        this.preferenceUtil = preferenceUtil;
        this.view = view;
        this.view.setPresenter(this);

    }

    @Override
    public void loadSOSList() {
        if(!Util.isInternetAvailable()) {
            view.showNoInternet();
            return;
        }
        if(isLoading)
            return;
        isLoading=true;
        repository.fetchSOSForSlide(slideItem.getId(), new DataSource.GetDataCallback<GetSOSResponse>() {
            @Override
            public void onDataReceived(GetSOSResponse data) {
                isLoading=false;
                if (data.isSuccess()) {
                    mSosList.clear();
                    mSosList.addAll(data.getSOSList());
                    preferenceUtil.saveFavSos(slideItem.getId(),mSosList);
                    generateAccessedList(mSosList);
                    view.onSOSListLoaded(mSosList);
                }
            }

            @Override
            public void onFailed(int code, String message) {

                view.showMessage(message);

            }
        });

    }

    @Override
    public void saveFavoriteSOS(ContactEntity entity, String userId) {
        if(preferenceUtil.getAccount().getPrimaryHelper()==null ||
                !preferenceUtil.getAccount().getPrimaryHelper().isPrimaryConnected())
            return;

        for (int i = 0; i < mSosList.size(); i++) {

            if (entity.getMobileNumber().equals(mSosList.get(i).getMobileNumber())) {
                isItemAdded = true;
            }
        }


        if (!isItemAdded) {

            entity.setUserId(userId);
            entity.setSlide_id(slideItem.getId());
            FavSOSRequest request = new FavSOSRequest();
            request.setSOS(entity);
            view.showProgress();
            repository.addSOSToSlide(request, new DataSource.GetDataCallback<GetSOSResponse>() {
                @Override
                public void onDataReceived(GetSOSResponse data) {
                    view.hideProgress();
                    if (data != null) {
                        mSosList.add(data.getSOSEntity());
                        view.onSOSListLoaded(mSosList);
                    }
                }

                @Override
                public void onFailed(int code, String message) {
                    view.hideProgress();
                    view.showMessage(message);

                }
            });
        } else {
            isItemAdded = false;
            view.showMessage("You have already added this SOS number");
        }


    }

    @Override
    public void updateSOSItem(ContactEntity entity) {
        for(ContactEntity contactEntity: mSosList){
            if(contactEntity!=null && contactEntity.getId()!=null && contactEntity.getId().equals(entity.getId())){
                contactEntity.setRequestStatus(entity.getRequestStatus());
                break;
            }
        }
        generateAccessedList(mSosList);
        view.onSOSListLoaded(mSosList);
    }

    @Override
    public void getItemForCall() {
        view.itemLoadForCall(mhasaccessList);
    }
    @Override
    public void generateAccessedList(List<ContactEntity> list) {
        for(ContactEntity contactEntity:list)
        {
            if(contactEntity.hasAccess())
            {
                mhasaccessList.add(contactEntity);
            }
        }
    }

    private void loadFromLocal(){
        List<ContactEntity> localList= preferenceUtil.getSosList(slideItem.getId());
        mSosList.clear();
        mSosList.addAll(localList);
        view.onSOSListLoaded(mSosList);
    }
}