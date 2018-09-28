package com.uiu.kids.ui.invitation;


import com.uiu.kids.model.Invitation;
import com.uiu.kids.model.User;
import com.uiu.kids.model.response.BaseResponse;
import com.uiu.kids.model.response.InvitationResponse;
import com.uiu.kids.source.DataSource;
import com.uiu.kids.source.Repository;

import java.util.ArrayList;
import java.util.List;

public class InviteListPresenter implements InviteContract.Presenter {

    public InviteContract.View view;
    public Repository repository;

    public List<Invitation> mInviteList;

    private User userEntity;

    public InviteListPresenter(InviteContract.View view, Repository repository, User mUserObj) {
        this.view = view;
        this.repository = repository;
        this.userEntity = mUserObj;
        view.setPresenter(this);
    }

    @Override
    public void getInvites() {
        view.showProgress();
            repository.getInvites(userEntity.getId(), new DataSource.GetDataCallback<InvitationResponse>() {
                @Override
                public void onDataReceived(InvitationResponse data) {
                    view.hideProgress();
                    mInviteList.clear();
                    mInviteList.addAll(data.getInvitationList());
                    view.onInvitesLoaded(mInviteList);
                }

                @Override
                public void onFailed(int code, String message) {
                    view.hideProgress();
                    view.showMessage(message);
                }
            });
    }

    @Override
    public void disconnect(Invitation invitation) {
        view.showProgress();
        repository.disconnect(invitation.getInviteId(), new DataSource.GetResponseCallback<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse response) {
                view.hideProgress();
                mInviteList.remove(invitation);
                view.onInvitesLoaded(mInviteList);
            }

            @Override
            public void onFailed(int code, String message) {
            view.hideProgress();
            view.showMessage(message);
            }
        });
    }

    @Override
    public void updateInvitation(Invitation invitation) {
        view.showProgress();
        repository.updateInvite(invitation.getInviteId(),invitation.getStatus(),userEntity.getId(), new DataSource.GetResponseCallback<InvitationResponse>() {
            @Override
            public void onSuccess(InvitationResponse response) {
                view.hideProgress();
               // view.showMessage("Just resent an invite to "+response.getInvitation().getSender().getEmail());
                //view.onInvitesLoaded(mInviteList);
                mInviteList.clear();
                getInvites();
            }

            @Override
            public void onFailed(int code, String message) {
                view.hideProgress();
                view.showMessage(message);
            }
        });
    }

    @Override
    public void start() {
        mInviteList  = new ArrayList<>();
        getInvites();
    }
}
