package com.uiu.kids.ui.slides.invitation;

import android.os.Bundle;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.uiu.kids.BaseFragment;
import com.uiu.kids.Constant;
import com.uiu.kids.Injection;
import com.uiu.kids.R;
import com.uiu.kids.event.InviteUpdatedEvent;
import com.uiu.kids.event.notification.InviteNotificationEvent;
import com.uiu.kids.event.notification.NotificationReceiveEvent;
import com.uiu.kids.event.SlideEvent;
import com.uiu.kids.model.Invitation;
import com.uiu.kids.model.User;
import com.uiu.kids.util.PreferenceUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InviteListFragment extends BaseFragment implements InviteContract.View,
        InviteListAdapter.Callback,
        InvitationConfirmationCallback,
        SwipeRefreshLayout.OnRefreshListener
{

    private static final String TAG = "InviteListFragment";
    public InviteContract.Presenter presenter;
    public InviteListAdapter adapter;

    @BindView(R.id.rvHelpers)
    RecyclerView recyclerView;

    @BindView(R.id.swipe_to_refresh)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.message)
    public TextView tvMessage;

    @BindView(R.id.progressBar)
    ContentLoadingProgressBar progressBar;


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
        swipeRefreshLayout.setOnRefreshListener(this);
        setAdapter();
        if(presenter==null)
            presenter = new InviteListPresenter(this,PreferenceUtil.getInstance(getActivity())
                    ,Injection.provideRepository(getContext()),PreferenceUtil.getInstance(getContext()).getAccount());
        presenter.start();

    }

    @Override
    public void setUserVisibleHint(boolean isFragmentVisible_) {
        super.setUserVisibleHint(true);
        if (this.isVisible()) {
// we check that the fragment is becoming visible
            if (isFragmentVisible_ ) {
                showProgress();
                presenter.start();
            }
        }
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
        progressBar.show();
    }

    @Override
    public void hideProgress() {
        progressBar.hide();
    }

    @Override
    public void onInvitesLoaded(List<Invitation> list) {
        hideProgress();
        swipeRefreshLayout.setRefreshing(false);
        tvMessage.setVisibility(!list.isEmpty()?View.GONE:View.VISIBLE);
        //  if(PreferenceUtil.getInstance(getContext()).getAccount().getPrimaryHelper()!=null)
        //       EventBus.getDefault().post(new SlideEvent(SLIDE_INDEX_INVITE,false));
        adapter.setItems(list);
    }

    @Override
    public void onInvitationAccepted(Invitation invitation) {
        updateLocationInvitationList(invitation);
    }

    @Override
    public void onInvitationRejected(Invitation invitation) {
        updateLocationInvitationList(invitation);
    }
    private void updateLocationInvitationList(Invitation invitation){
        User user = PreferenceUtil.getInstance(getContext()).getAccount();
        List<Invitation> helpers = new ArrayList<>();
        for(Invitation mInvitation: user.getInvitations()){
            if(mInvitation.getInviteId().equals(invitation.getInviteId()))
                mInvitation=invitation;
            helpers.add(mInvitation);
        }

        user.setInvitations(helpers);
        PreferenceUtil.getInstance(getContext()).saveAccount(user);
        PreferenceUtil.getInstance(getContext()).saveInvitationList(helpers);
        adapter.setItems(helpers);
        EventBus.getDefault().post(new SlideEvent(SLIDE_INDEX_INVITE,false));
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
            //onInvitesLoaded(PreferenceUtil.getInstance(getContext()).getInvitationList());
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(InviteNotificationEvent receiveEvent) {
        if(adapter!=null)
            presenter.start();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(InviteUpdatedEvent receiveEvent) {
        if(adapter!=null)
            adapter.setItems(PreferenceUtil.getInstance(getContext()).getInvitationList());
        else
            onInvitesLoaded(PreferenceUtil.getInstance(getContext()).getInvitationList());

    }

    @Override
    public void onItemClick(Invitation slideItem) {

        if(slideItem.getStatus() == INVITE.INVITED ){
            String title = getString(R.string.please_choose_an_option);
            String msg = getString(R.string.helper_invite,slideItem.getSender().getEmail());
            mBaseActivity.showInvitationActionsDialog(getActivity(),title,msg,slideItem,this);
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

    @Override
    public void onRefresh() {
        presenter.getInvites();
    }
}
