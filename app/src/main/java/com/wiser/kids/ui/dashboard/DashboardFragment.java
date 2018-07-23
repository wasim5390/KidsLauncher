package com.wiser.kids.ui.dashboard;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;

import android.view.View;


import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.wiser.kids.BaseFragment;
import com.wiser.kids.R;
import com.wiser.kids.event.GoogleLoginEvent;
import com.wiser.kids.util.PermissionUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;


public class DashboardFragment extends BaseFragment implements DashboardContract.View,
        GoogleLoginDialog,PermissionUtil.PermissionCallback
{

    private static final int RC_SIGN_IN = 0x006;
    DashboardContract.Presenter presenter;
    protected GoogleSignInClient mGoogleSignInClient;

    @BindView(R.id.dashboard_pager)
    ViewPager fragmentPager;


    @Override
    public int getID() {
        return R.layout.fragment_dashboard;
    }

    @Override
    public void initUI(View view) {

        googleSignInClient();
        presenter.createSlides();

    }



    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    private void getProfileInformation(GoogleSignInAccount acct){
        if (acct != null) {
            Log.i("UserIDCheck","--->"+acct.getId());
            PermissionUtil.requestPermissions(getActivity(),this);

            new Handler().postDelayed(() -> EventBus.getDefault().postSticky(new GoogleLoginEvent(acct)),1500);

        }
    }

    private void googleSignInClient(){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
    }

    public static DashboardFragment newInstance() {
        Bundle args = new Bundle();
        DashboardFragment dashboardFragment = new DashboardFragment();
        dashboardFragment.setArguments(args);
        return dashboardFragment;
    }



    private void setViewPager(List<Fragment> slides){
        PagerAdapter  pagerAdapter = new PagerAdapter(getChildFragmentManager(), slides);
        fragmentPager.setAdapter(pagerAdapter);
        fragmentPager.setOffscreenPageLimit(pagerAdapter.getCount()-1);
        fragmentPager.setCurrentItem(0);
        pagerAdapter.notifyDataSetChanged();
        fragmentPager.setPageMargin(32);
        fragmentPager.invalidate();

    }

    @Override
    public void showMessage() {

    }

    @Override
    public void onLoginSuccessful() {

    }

    @Override
    public void onSlidesCreated(List<Fragment> fragments) {
        setViewPager(fragments);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getContext());
        if (account == null) {
            showGoogleLoginDialog(this);
        }else{
            getProfileInformation(account);
        }
    }


    @Override
    public void setPresenter(DashboardContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showNoInternet() {

    }

    @Override
    public void onGoogleLoginClicked() {
        signIn();
    }


    @Override
    public void onPermissionGranted(String permission) {

    }

    @Override
    public void onPermissionGranted() {
             /**
             * Call Auth Service For Login
                *//*
                service.auth.register(prepareInput(acct), new IService() {
                    @Override
                    public void onSuccess(CustomHttpResponse response) {

                        UserResponse userResponse = (UserResponse) response.getResponse();
                    *//**
             * Set Default Launcher
                                *//*
                                showSetAsDefaultLauncherDialog();
                    }

                    @Override
                    public void onFailure(CustomHttpResponse response) {
                        hideLoader();
                    }
                });*/
    }

    @Override
    public void onPermissionDenied() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                // Signed in successfully, show authenticated UI.
                getProfileInformation(account);
            } catch (ApiException e) {
                Log.w("TAG", "signInResult:failed code=" + e.getStatusCode());
                Log.w("TAG", "signInResult:failed code=" + e.getMessage());
            }
        }
    }
}
