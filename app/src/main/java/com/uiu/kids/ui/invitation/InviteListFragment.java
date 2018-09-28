package com.uiu.kids.ui.invitation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.uiu.kids.BaseActivity;
import com.uiu.kids.BaseFragment;
import com.uiu.kids.Constant;
import com.uiu.kids.Injection;
import com.uiu.kids.R;
import com.uiu.kids.event.NotificationReceiveEvent;
import com.uiu.kids.model.Invitation;
import com.uiu.kids.util.PreferenceUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InviteListFragment extends BaseFragment implements InviteContract.View,
        InviteListAdapter.Callback,
        InvitationConfirmationCallback{

    private static final String TAG = "InviteListFragment";
    public InviteContract.Presenter presenter;
    public InviteListAdapter adapter;

    @BindView(R.id.rvHelpers)
    RecyclerView recyclerView;

    @BindView(R.id.message)
    public TextView tvMessage;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;


    public static InviteListFragment newInstance()
    {
        Bundle args=new Bundle();
        InviteListFragment instance=new InviteListFragment();
        instance.setArguments(args);
        return instance;
    }

    @Override
    public int getID() {
        return R.layout.fragment_invite;
    }

    @Override
    public void initUI(View view) {
        ButterKnife.bind(getActivity());
        EventBus.getDefault().register(this);
        setAdapter();
        presenter.start();
    }

    private void setAdapter() {
        adapter = new InviteListAdapter(getContext(), new ArrayList<>(), this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Unregister");
        EventBus.getDefault().unregister(this);
    }
    @Override
    public void showMessage(String message) {
        Toast.makeText(mBaseActivity, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onInvitesLoaded(List<Invitation> list) {

        tvMessage.setVisibility(!list.isEmpty()?View.GONE:View.VISIBLE);

        adapter.setItems(list);
    }

    @Override
    public void setPresenter(InviteContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showNoInternet() {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(NotificationReceiveEvent receiveEvent) {
        if(receiveEvent.getNotificationForSlideType()== Constant.INVITE_CODE){
           /* JSONObject jsonObject = receiveEvent.getNotificationResponse();
           AppsEntity entity =  new Gson().fromJson(jsonObject.toString(),AppsEntity.class);
           if(entity.hasAccess()){
               presenter.updateEntity(entity);
           }*/
           if(receiveEvent.getStatus()!=INVITE.CONNECTED)
               BaseActivity.primaryParentId=null;
            presenter.getInvites();
        }

    }

    @Override
    public void onItemClick(Invitation slideItem) {

        if(slideItem.getStatus() == INVITE.INVITED ){
            String title = getString(R.string.please_choose_an_option);
            String msg = getString(R.string.helper_invite,slideItem.getSender().getEmail());
            mBaseActivity.showInvitationActionsDialog(getContext(),title,msg,slideItem,this);
        }
        /*if(slideItem.getStatus() == INVITE.REJECTED ){
            String title = getString(R.string.please_choose_an_option);
            String msg = getString(R.string.helper_invite,slideItem.getSender().getEmail());
            mBaseActivity.showInvitationActionsDialog(getContext(),title,msg,slideItem,this);
        }*/
    }

    @Override
    public void onItemLongClick(final Invitation slideItem) {
       // mBaseActivity.showInvitationActionsDialog(getContext(),slideItem,this);
    }


    @Override
    public void onAcceptInvitation(Invitation invitation) {
        invitation.setStatus(INVITE.CONNECTED);
        presenter.updateInvitation(invitation);
    }

    @Override
    public void onRejectInvitation(Invitation invitation) {
        invitation.setStatus(INVITE.REJECTED);
        presenter.updateInvitation(invitation);
    }

    @Override
    public void onDeleteInvitation(Invitation invitation) {
        presenter.disconnect(invitation);
    }
}
