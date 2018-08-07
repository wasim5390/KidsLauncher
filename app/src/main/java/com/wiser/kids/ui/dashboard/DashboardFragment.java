package com.wiser.kids.ui.dashboard;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;

import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;


import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.wiser.kids.BaseFragment;
import com.wiser.kids.R;
import com.wiser.kids.event.GoogleLoginEvent;
import com.wiser.kids.model.SlideItem;
import com.wiser.kids.ui.home.HomeFragment;
import com.wiser.kids.ui.home.HomePresenter;
import com.wiser.kids.util.PermissionUtil;
import com.wiser.kids.util.PreferenceUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;


public class DashboardFragment extends BaseFragment implements DashboardContract.View,
        GoogleLoginDialog,PermissionUtil.PermissionCallback, View.OnClickListener {

    private static final int RC_SIGN_IN = 0x006;
    protected GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInAccount account;
    DashboardContract.Presenter presenter;


    @BindView(R.id.dashboard_pager)
    ViewPager fragmentPager;
    PagerAdapter  pagerAdapter;

    @BindView(R.id.home_left_btn)
    public ImageView hLefttBtn;

    @BindView(R.id.home_right_btn)
    public ImageView hRightBtn;


    @Override
    public int getID() {
        return R.layout.fragment_dashboard;
    }

    @Override
    public void initUI(View view) {
        googleSignInClient();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getActivity());
        if (account == null) {
            showGoogleLoginDialog(this);
        }else{
            getProfileInformation(account);
        }
        addListner();

    }

    private void addListner() {
        hLefttBtn.setOnClickListener(this);
        hRightBtn.setOnClickListener(this);
    }

    public static DashboardFragment newInstance() {
        Bundle args = new Bundle();
        DashboardFragment dashboardFragment = new DashboardFragment();
        dashboardFragment.setArguments(args);
        return dashboardFragment;
    }



    private void setViewPager(List<Fragment> slides){
        if(pagerAdapter!=null)
            pagerAdapter.setSlides(slides);
        else {
            pagerAdapter = new PagerAdapter(getChildFragmentManager(), slides);
            fragmentPager.setAdapter(pagerAdapter);
            fragmentPager.setOffscreenPageLimit(pagerAdapter.getCount() - 1);
            fragmentPager.setCurrentItem(0);
            pagerAdapter.notifyDataSetChanged();
            fragmentPager.setPageMargin(32);
            fragmentPager.invalidate();
        }
    }


    private void googleSignInClient(){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
    }
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void getProfileInformation(GoogleSignInAccount acct){
        if (acct != null) {
            account = acct;
            Log.i("UserIDCheck","--->"+acct.getId());
            PermissionUtil.requestPermissions(getActivity(),this);
            new Handler().postDelayed(() -> EventBus.getDefault().postSticky(new GoogleLoginEvent(acct)),1500);

        }
    }


    @Override
    public void onGoogleLoginClicked() {
        signIn();
    }

    @Override
    public void onPermissionsGranted(String permission) {

    }

    @Override
    public void onPermissionsGranted() {
        Uri photoUri = account.getPhotoUrl();
        HashMap<String,Object> params = new HashMap<>();
        params.put("email",account.getEmail());
        params.put("password",account.getEmail());
        params.put("first_name",account.getGivenName());
        params.put("last_name",account.getFamilyName());
        params.put("user_type","3"); // 3 means kids.
        params.put("image_link",(photoUri!=null && photoUri.toString().isEmpty())?photoUri.toString():null);
        params.put("fcm_key", PreferenceUtil.getInstance(getContext()).getPreference(PREF_NOTIFICATION_TOKEN));

        presenter.createAccount(params);

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

    @Override
    public void showMessage(String message) {
        Toast.makeText(mBaseActivity, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoginSuccessful() {

    }

    @Override
    public void onLoginFailed(String message) {
        Toast.makeText(mBaseActivity, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSlidesCreated(List<Fragment> fragments) {
        setViewPager(fragments);

    }

    @Override
    public void onSlidesLoaded(List<SlideItem> slideItems) {
     //   if(slideItems==null || slideItems.isEmpty())
     //       presenter.createSlides(slideItems);
     //   else
            presenter.convertSlidesToFragment(slideItems);
    }


    @Override
    public void setPresenter(DashboardContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showNoInternet() {

    }


    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.home_right_btn:
                fragmentPager.arrowScroll(ViewPager.FOCUS_RIGHT);
                Log.e("current item", String.valueOf(fragmentPager.getOffscreenPageLimit()));
                //arrowVisibility(fragmentPager.getCurrentItem());
                break;

            case R.id.home_left_btn:
                fragmentPager.arrowScroll(ViewPager.FOCUS_LEFT);
                //arrowVisibility(fragmentPager.getCurrentItem());
                break;
        }
    }

    private void arrowVisibility(int currentItem) {
        if (fragmentPager.getOffscreenPageLimit()==currentItem)
        {
            hRightBtn.setVisibility(View.GONE);
        }
        else if(currentItem==0)
        {
            hLefttBtn.setVisibility(View.GONE);
        }
        else
        {
            hLefttBtn.setVisibility(View.VISIBLE);
            hRightBtn.setVisibility(View.VISIBLE);

        }

    }
}
