package com.wiser.kids.ui.dashboard;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
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
import com.wiser.kids.BaseFragment;
import com.wiser.kids.Injection;
import com.wiser.kids.R;
import com.wiser.kids.event.GoogleLoginEvent;
//import com.wiser.kids.location.BackgroundGeoFenceService;
import com.wiser.kids.location.BackgroundGeoFenceService;
import com.wiser.kids.model.Location;
import com.wiser.kids.model.SlideItem;
import com.wiser.kids.model.User;
import com.wiser.kids.util.PermissionUtil;
import com.wiser.kids.util.PreferenceUtil;
import com.wiser.kids.util.Util;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;


public class DashboardFragment extends BaseFragment implements DashboardContract.View,
        GoogleLoginDialog, PermissionUtil.PermissionCallback, View.OnClickListener {

    private static final int RC_SIGN_IN = 0x006;
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


    @Override
    public int getID() {
        return R.layout.fragment_dashboard;
    }

    @Override
    public void initUI(View view) {
        if(presenter==null)
            setPresenter(new DashboardPresenter(this, PreferenceUtil.getInstance(getActivity()), Injection.provideRepository(getActivity())));
        User user = PreferenceUtil.getInstance(getActivity()).getAccount();
        if(user.getId()!=null)
            presenter.getUserSlides(user.getId());
        else{
            googleSignInClient();
            GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getActivity());
            if (account == null) {
                showGoogleLoginDialog(this);
            } else {
                getProfileInformation(account);
            }
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


    private void setViewPager(List<Fragment> slides) {
        if (pagerAdapter != null)
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

        fragmentPager.addOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
                if(PreferenceUtil.getInstance(getContext()).getAccount().getPrimaryHelper()==null)
                if (positionOffset > 0.5) {
                    fragmentPager.setCurrentItem(0, true);
                }
            }

            @Override
            public void onPageSelected(int position) {
                if(PreferenceUtil.getInstance(getContext()).getAccount().getPrimaryHelper()==null)
                    if (position > 0) {
                        fragmentPager.setCurrentItem(0, true);
                    }
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
            account = acct;
            Log.i("UserIDCheck", "--->" + acct.getId());
            PermissionUtil.requestPermissions(getActivity(), this);
            new Handler().postDelayed(() -> EventBus.getDefault().postSticky(new GoogleLoginEvent(acct)), 1500);

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
        params.put("fcm_key", PreferenceUtil.getInstance(getContext()).getPreference(PREF_NOTIFICATION_TOKEN));

        TelephonyManager tMgr = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
        @SuppressLint("MissingPermission") String mPhoneNumber = tMgr.getLine1Number();
        if(mPhoneNumber==null || mPhoneNumber.isEmpty()) {
            getMobileNumberFromUser(params);
        }
        else{
            params.put("phone_number", mPhoneNumber);
            presenter.createAccount(params);
        }


    }

    @Override
    public void onPermissionDenied() {
        Toast.makeText(mBaseActivity, "All permissions required!", Toast.LENGTH_SHORT).show();
        mBaseActivity.openSettings();
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
        presenter.getKidsDirections(PreferenceUtil.getInstance(getActivity()).getAccount().getId());
    }

    @Override
    public void onSlidesLoaded(List<SlideItem> slideItems) {

        presenter.convertSlidesToFragment(slideItems);
    }

    @Override
    public void onDirectionsLoaded(List<Location> directions) {
        if(directions.size()>0) {
            List<Geofence> geofenceList = new ArrayList<>();
            for (Location location : directions) {
                geofenceList.add(Util.createGeofence(location.getLatitude(), location.getLongitude()));
            }
            BackgroundGeoFenceService.getInstance().addGeoFences(geofenceList);
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
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.home_right_btn:
                if(!PreferenceUtil.getInstance(getActivity()).getAccount().getHelpers().isEmpty())
                fragmentPager.arrowScroll(ViewPager.FOCUS_RIGHT);
                break;

            case R.id.home_left_btn:
                if(!PreferenceUtil.getInstance(getActivity()).getAccount().getHelpers().isEmpty())
                    fragmentPager.arrowScroll(ViewPager.FOCUS_LEFT);
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
}
