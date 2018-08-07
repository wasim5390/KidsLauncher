package com.wiser.kids.ui.SOS;

import com.wiser.kids.model.SlideItem;
import com.wiser.kids.model.request.FavSOSRequest;
import com.wiser.kids.model.response.GetSOSResponse;
import com.wiser.kids.source.DataSource;
import com.wiser.kids.source.Repository;
import com.wiser.kids.ui.favorite.links.FavoriteLinksContract;
import com.wiser.kids.ui.home.contact.ContactEntity;
import com.wiser.kids.util.PreferenceUtil;

import java.util.ArrayList;
import java.util.List;

public class SOSPresenter implements SOSContract.Presenter {

    public SOSContract.View view;
    public Repository repository;
    public SlideItem slideItem;
    private List<ContactEntity> mSosList;
    public PreferenceUtil preferenceUtil;
    public boolean isItemAdded = true;


    @Override
    public void start() {
        mSosList = new ArrayList<>();
        ContactEntity entity = new ContactEntity();
        entity.setName(null);
        mSosList.add(entity);
        view.onSOSListLoaded(mSosList);

    }


    public SOSPresenter(SOSContract.View view, SlideItem slideItem, PreferenceUtil preferenceUtil, Repository repository) {
        this.repository = repository;
        this.slideItem = slideItem;
        this.preferenceUtil = preferenceUtil;
        this.view = view;
        this.view.setPresenter(this);
        loadSOSList();
    }

    private void loadSOSList() {

        repository.fetchSOSForSlide(slideItem.getId(), new DataSource.GetDataCallback<GetSOSResponse>() {
            @Override
            public void onDataReceived(GetSOSResponse data) {

                if (data != null) {
                    ContactEntity addNewEntity = mSosList.get(mSosList.size() - 1);
                    mSosList.clear();
                    mSosList.addAll(data.getContactEntityList());
                    mSosList.add(addNewEntity);
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
                isItemAdded = false;
            }
        }


        if (isItemAdded) {
            ContactEntity addNewEntity = mSosList.get(mSosList.size() - 1);
            entity.setUserId(userId);
            entity.setSlide_id(slideItem.getId());
            FavSOSRequest request = new FavSOSRequest();
            request.setSOS(entity);
            repository.addSOSToSlide(request, new DataSource.GetDataCallback<GetSOSResponse>() {
                @Override
                public void onDataReceived(GetSOSResponse data) {

                    if (data != null) {
                        mSosList.remove(addNewEntity);

                        mSosList.add(data.getContactEntity());

                        mSosList.add(addNewEntity);

                        view.onSOSListLoaded(mSosList);
                    }
                }

                @Override
                public void onFailed(int code, String message) {

                    view.showMessage(message);

                }
            });
        } else {
            isItemAdded = true;
            view.showMessage("You are already added this SOS");
        }


    }

    @Override
    public void updateSOSItem(ContactEntity entity) {
        for(ContactEntity contactEntity: mSosList){
            if(contactEntity!=null && contactEntity.getId().equals(entity.getId())){
                contactEntity.setRequestStatus(entity.getRequestStatus());
                break;
            }
        }
        view.onSOSListLoaded(mSosList);
    }

    @Override
    public void getItemForCall() {
        view.itemLoadForCall(mSosList);
    }
}