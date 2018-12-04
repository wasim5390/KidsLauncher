package com.uiu.kids.ui.invitation;


import com.uiu.kids.Constant;
import com.uiu.kids.model.Invitation;
import com.uiu.kids.model.User;
import com.uiu.kids.model.response.BaseResponse;
import com.uiu.kids.model.response.InvitationResponse;
import com.uiu.kids.source.DataSource;
import com.uiu.kids.source.Repository;
import com.uiu.kids.util.PreferenceUtil;

import java.util.ArrayList;
import java.util.List;

public class InviteListPresenter implements InviteContract.Presenter {

    public InviteContract.View view;
    public Repository repository;
    private PreferenceUtil preferenceUtil;

    public List<Invitation> mInviteList;
    public boolean isLoading=false;
    private User userEntity;

    public InviteListPresenter(InviteContract.View view, PreferenceUtil preferenceUtil, Repository repository, User mUserObj) {
        this.view = view;
        this.repository = repository;
        this.preferenceUtil = preferenceUtil;
        this.userEntity = mUserObj;
        view.setPresenter(this);
    }

    @Override
    public void getInvites() {
        if(isLoading || !userEntity.getInvitations().isEmpty())
            return;
        isLoading=true;
        view.showProgress();
        repository.getInvites(userEntity.getId(), new DataSource.GetDataCallback<InvitationResponse>() {
            @Override
            public void onDataReceived(InvitationResponse data) {
                view.hideProgress();
                isLoading=false;
                if(data.isSuccess()) {

                    mInviteList.clear();
                    mInviteList.addAll(data.getInvitationList());
                    User user = preferenceUtil.getAccount();
                    user.setInvitations(mInviteList);
                    preferenceUtil.saveAccount(user);
                    view.onInvitesLoaded(mInviteList);
                }
            }

            @Override
            public void onFailed(int code, String message) {
                isLoading=false;
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
                if(invitation.getStatus()== Constant.INVITE.CONNECTED)
                    view.onInvitationAccepted(invitation);
                else
                    view.onInvitationRejected(invitation);
              //  mInviteList.clear();
              //  getInvites();
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
        loadFromLocal();
      //  getInvites();
    }

    private void loadFromLocal(){
        List<Invitation> localList= preferenceUtil.getAccount().getInvitations();
        mInviteList.clear();
        mInviteList.addAll(localList);
        view.onInvitesLoaded(mInviteList);
    }
}
