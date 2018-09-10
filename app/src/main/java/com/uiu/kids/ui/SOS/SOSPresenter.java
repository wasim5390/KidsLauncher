package com.uiu.kids.ui.SOS;

import com.uiu.kids.model.SlideItem;
import com.uiu.kids.model.request.FavSOSRequest;
import com.uiu.kids.model.response.GetSOSResponse;
import com.uiu.kids.source.DataSource;
import com.uiu.kids.source.Repository;
import com.uiu.kids.ui.home.contact.ContactEntity;
import com.uiu.kids.util.PreferenceUtil;

import java.util.ArrayList;
import java.util.List;

public class SOSPresenter implements SOSContract.Presenter {

    public SOSContract.View view;
    public Repository repository;
    public SlideItem slideItem;
    private List<ContactEntity> mSosList;
    private List<ContactEntity> mhasaccessList;
    public PreferenceUtil preferenceUtil;
    public boolean isItemAdded = false;


    @Override
    public void start() {
        mSosList = new ArrayList<>();
        mhasaccessList=new ArrayList<>();
        ContactEntity entity = new ContactEntity();
        entity.setName(null);
        mSosList.add(entity);
        view.onSOSListLoaded(mSosList);
        loadSOSList();
    }


    public SOSPresenter(SOSContract.View view, SlideItem slideItem, PreferenceUtil preferenceUtil, Repository repository) {
        this.repository = repository;
        this.slideItem = slideItem;
        this.preferenceUtil = preferenceUtil;
        this.view = view;
        this.view.setPresenter(this);

    }

    @Override
    public void loadSOSList() {

        repository.fetchSOSForSlide(slideItem.getId(), new DataSource.GetDataCallback<GetSOSResponse>() {
            @Override
            public void onDataReceived(GetSOSResponse data) {

                if (data != null) {
                    ContactEntity addNewEntity = mSosList.get(mSosList.size() - 1);
                    mSosList.clear();
                    mSosList.addAll(data.getContactEntityList());
                    mSosList.add(addNewEntity);
//                   mSosList.get(0).setRequestStatus(3);
//                   mSosList.get(1).setRequestStatus(3);
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

        for (int i = 0; i < mSosList.size(); i++) {

            if (entity.getmPhoneNumber().equals(mSosList.get(i).getmPhoneNumber())) {
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
                        ContactEntity addNewEntity = mSosList.get(mSosList.size() - 1);
                        mSosList.remove(addNewEntity);
                        mSosList.add(data.getContactEntity());
                        mSosList.add(addNewEntity);
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
}