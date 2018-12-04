package com.uiu.kids.ui.dashboard;


import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.telephony.PhoneNumberUtils;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.tasks.Task;
import com.uiu.kids.BaseActivity;
import com.uiu.kids.BaseFragment;
import com.uiu.kids.Constant;
import com.uiu.kids.Injection;
import com.uiu.kids.R;
import com.uiu.kids.event.LoginEvent;
import com.uiu.kids.event.NotificationReceiveEvent;
import com.uiu.kids.event.SlideCreateEvent;
import com.uiu.kids.event.SlideEvent;
import com.uiu.kids.location.BackgroundGeoFenceService;
import com.uiu.kids.model.Location;
import com.uiu.kids.model.Setting;
import com.uiu.kids.model.Slide;
import com.uiu.kids.model.User;
import com.uiu.kids.model.response.InvitationResponse;
import com.uiu.kids.source.DataSource;
import com.uiu.kids.source.Repository;
import com.uiu.kids.ui.SleepActivity;
import com.uiu.kids.util.PermissionUtil;
import com.uiu.kids.util.PreferenceUtil;
import com.uiu.kids.util.Util;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_CANCELED;


public class DashboardFragment extends BaseFragment implements DashboardContract.View,
        GoogleLoginDialog, PermissionUtil.PermissionCallback, View.OnClickListener {

    private static final int RC_SIGN_IN = 0x006;
    private static final int RESULT_ADMIN_ENABLE = 0x1002;
    protected GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInAccount account;
    DashboardContract.Presenter presenter;

    @BindView(R.id.dashboard_pager)
    ViewPager fragmentPager;
    PagerAdapter pagerAdapter;

    @BindView(R.id.home_left_btn)
    public ImageView hLefttBtn;

    @BindView(R.id.home_right_btn)
    public ImageView hRightBtn;

    @BindView(R.id.home_btn)
    public ImageView homBtn;

    @BindView(R.id.home_press_btn)
    public TextView swipeText;

    @BindView(R.id.progressBar)
    ContentLoadingProgressBar progressBar;

    public String loadedFrom="Normal";

    private PreferenceUtil preferenceUtil;


    @Override
    public int getID() {
        return R.layout.fragment_dashboard;
    }

    @Override
    public void initUI(View view) {
        ButterKnife.bind(this,view);
        if(getArguments().containsKey("ActionFrom"))
            loadedFrom = getArguments().getString("ActionFrom");
        EventBus.getDefault().register(this);
        if(presenter==null)
            setPresenter(new DashboardPresenter(this, PreferenceUtil.getInstance(getActivity()), Injection.provideRepository(getActivity())));

        preferenceUtil= PreferenceUtil.getInstance(getContext());
        User user = preferenceUtil.getAccount();
        if(user.getId()!=null)
            presenter.getInvites(user.getId());
        else{
            googleSignInClient();
            GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getActivity());
            if (account == null) {
                showGoogleLoginDialog(this);
            } else {
                getProfileInformation(account);
            }
        }
        addListener();

    }


    @Override
    public void onResume() {
        super.onResume();

    }

    private void addListener() {
        hLefttBtn.setOnClickListener(this);
        hRightBtn.setOnClickListener(this);
        homBtn.setOnClickListener(this);
    }

    public static DashboardFragment newInstance(String loadedFrom) {
        Bundle args = new Bundle();
        args.putString("ActionFrom",loadedFrom);
        DashboardFragment dashboardFragment = new DashboardFragment();
        dashboardFragment.setArguments(args);
        return dashboardFragment;
    }


    private void setViewPager(List<Slide> slides) {

        if (pagerAdapter != null) {
            pagerAdapter.setSlides(slides);
            if(loadedFrom.equals("Bobble"))
                moveToSlide(SLIDE_INDEX_HOME);
            else
                moveToSlide(SLIDE_INDEX_HOME);

        }
        else {
            pagerAdapter = new PagerAdapter(getChildFragmentManager(),slides,Injection.provideRepository(getContext()),preferenceUtil);
            fragmentPager.setAdapter(pagerAdapter);

            fragmentPager.setOffscreenPageLimit(2);
            pagerAdapter.notifyDataSetChanged();
            fragmentPager.setPageMargin(32);
            fragmentPager.invalidate();

            if(loadedFrom.equals("Bobble"))
                moveToSlide(SLIDE_INDEX_HOME);
            else
                moveToSlide(SLIDE_INDEX_HOME);
        }

        fragmentPager.addOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
              /*  User user = PreferenceUtil.getInstance(getContext()).getAccount();
                if(user.getPrimaryHelper()==null)
                if (positionOffset > 0.5) {
                    fragmentPager.setCurrentItem(0, true);
                }*/
            }

            @Override
            public void onPageSelected(int position) {

                homBtn.setVisibility(pagerAdapter.getSlideIndex(SLIDE_INDEX_HOME)!=position?View.VISIBLE:View.GONE);
                swipeText.setVisibility(pagerAdapter.getSlideIndex(SLIDE_INDEX_HOME)==position?View.VISIBLE:View.INVISIBLE);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }
    private void googleSignInClient() {
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

    private void getProfileInformation(GoogleSignInAccount acct) {
        if (acct != null) {
            progressBar.show();
            account = acct;
            Log.i("UserIDCheck", "--->" + acct.getId());
            PermissionUtil.requestPermissions(getActivity(), this);
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
        HashMap<String, Object> params = new HashMap<>();
        params.put("email", account.getEmail());
        params.put("password", account.getEmail());
        params.put("first_name", account.getGivenName());
        params.put("last_name", account.getFamilyName());
        params.put("user_type", "3"); // 3 means kids.
        params.put("image_link", (photoUri != null && photoUri.toString().isEmpty()) ? photoUri.toString() : null);
        params.put("fcm_key", preferenceUtil.getPreference(PREF_NOTIFICATION_TOKEN));

        TelephonyManager tMgr = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
        @SuppressLint("MissingPermission") String mPhoneNumber = tMgr.getLine1Number();
        //   if(mPhoneNumber==null || mPhoneNumber.isEmpty()) {
        //       getMobileNumberFromUser(params);
        //   }
        //   else{
        params.put("phone_number", mPhoneNumber);
        presenter.createAccount(params);
        //    }


    }

    @Override
    public void onPermissionDenied() {
        progressBar.hide();
        Toast.makeText(mBaseActivity, "All permissions required!", Toast.LENGTH_SHORT).show();
        mBaseActivity.openSettings();
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);

        super.onDestroy();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            if(resultCode==RESULT_CANCELED)
                getActivity().finish();
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
    public void onLoginSuccessful(User user) {
        EventBus.getDefault().postSticky(new LoginEvent(user));
    }

    @Override
    public void onLoginFailed(String message) {
        Toast.makeText(mBaseActivity, message, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onSlidesLoaded(List<Slide> slideItems) {

        setViewPager(removeSosSlide(slideItems));
        presenter.getKidsDirections(preferenceUtil.getAccount().getId());
        progressBar.hide();
        getView().findViewById(R.id.rlBottom).setVisibility(View.VISIBLE);
    }

    @Override
    public void onSlidesUpdated(List<Slide> slides) {

        pagerAdapter.setSlides(removeSosSlide(slides));
        fragmentPager.setSaveFromParentEnabled(false);
        fragmentPager.invalidate();
    }


    /**
     * With reference 10/9/2018 SOS slide is not needed in Kid App
     * @param slides
     * @return
     */
    public List<Slide> removeSosSlide(List<Slide> slides){
        List<Slide> slideList = new ArrayList<>(slides);
        for(Slide slide:slides){
            if(slide.getType()==SLIDE_INDEX_SOS || slide.getType()==SLIDE_INDEX_DIRECTIONS)
                slideList.remove(slide);
        }
        return slideList;
    }
    @Override
    public void onDirectionsLoaded(List<Location> directions) {
        if(directions.size()>0) {
            BackgroundGeoFenceService.getInstance().addGeoFences(directions);
        }
    }

    @Override
    public void onSettingsUpdated(Setting setting) {

    }


    @Override
    public void setPresenter(DashboardContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showNoInternet() {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(SlideCreateEvent receiveEvent) {
        presenter.addSlide(receiveEvent.getSlide());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(NotificationReceiveEvent receiveEvent) {
        if(receiveEvent.getNotificationForSlideType()== Constant.INVITE_CODE){
        presenter.loadSlidesFromLocal();
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(SlideEvent receiveEvent) {
        if(receiveEvent.getSlideType()== Constant.SLIDE_INDEX_INVITE){
            if(!receiveEvent.isCreateSlide())
            presenter.removeSlideByType(Constant.SLIDE_INDEX_INVITE);
        }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.home_right_btn:
                // if(!PreferenceUtil.getInstance(getActivity()).getAccount().getInvitations().isEmpty())
                fragmentPager.arrowScroll(ViewPager.FOCUS_RIGHT);
                break;

            case R.id.home_left_btn:
                //  if(!PreferenceUtil.getInstance(getActivity()).getAccount().getInvitations().isEmpty())
                fragmentPager.arrowScroll(ViewPager.FOCUS_LEFT);


                break;
            case R.id.home_btn:
                //fragmentPager.setCurrentItem(preferenceUtil.getAccount().getPrimaryHelper()==null?1:0);
                moveToSlide(Constant.SLIDE_INDEX_HOME);
                break;
        }
    }

    private void getMobileNumberFromUser(HashMap<String,Object> params) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getContext());
        View mView = layoutInflaterAndroid.inflate(R.layout.user_input_dialog_box, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(getContext());
        alertDialogBuilderUserInput.setView(mView);
        TextView title = mView.findViewById(R.id.dialogTitle);
        title.setText("Please provide your registered mobile number!");
        final EditText userInputDialogEditText = mView.findViewById(R.id.userInputDialog);
        userInputDialogEditText.setHint("+123-456-7890");
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton("OK", (dialogBox, id) -> {
                    String phoneNumber = userInputDialogEditText.getText().toString();
                    if(PhoneNumberUtils.isGlobalPhoneNumber(phoneNumber)) {
                        params.put("phone_number",phoneNumber);
                        presenter.createAccount(params);
                    }
                    else
                    {
                        Toast.makeText(getContext(), "Please enter valid mobile number", Toast.LENGTH_SHORT).show();

                    }

                });

        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.setCancelable(false);
        alertDialogAndroid.show();
    }
    @Override
    public void hideProgress() {
        try {
            mBaseActivity.hideProgress();
        }catch (Exception e){

        }
    }

    public void moveToSlide(int slideType) {

        int slideIndexToMove =pagerAdapter.getSlideIndex(slideType);
        fragmentPager.setCurrentItem(slideIndexToMove,true);
    }

}
