package com.uiu.kids.ui.dashboard;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
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
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.uiu.kids.BaseFragment;
import com.uiu.kids.Constant;
import com.uiu.kids.Injection;
import com.uiu.kids.KidsLauncherApp;
import com.uiu.kids.R;
import com.uiu.kids.event.LoginEvent;
import com.uiu.kids.event.notification.NotificationReceiveEvent;
import com.uiu.kids.event.SlideCreateEvent;
import com.uiu.kids.event.SlideEvent;
import com.uiu.kids.location.GeofenceTransitionsIntentService;
import com.uiu.kids.model.Location;
import com.uiu.kids.model.Setting;
import com.uiu.kids.model.Slide;
import com.uiu.kids.model.User;
import com.uiu.kids.ui.slides.reminder.AlarmReceiver;
import com.uiu.kids.util.PermissionUtil;
import com.uiu.kids.util.PreferenceUtil;
import com.uiu.kids.util.Util;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_CANCELED;


public class DashboardFragment extends BaseFragment implements DashboardContract.View,
        GoogleLoginDialog, PermissionUtil.PermissionCallback, View.OnClickListener {

    private static final int RC_SIGN_IN = 0x006;
    private static final int RESULT_ADMIN_ENABLE = 0x1002;
    private static final String TAG = DashboardFragment.class.getSimpleName();
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
    private GeofencingClient mGeofencingClient;
    private PendingIntent mGeofencePendingIntent;
    private List<Geofence> mGeofenceList = new ArrayList<>();

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
        if(user.getId()!=null) {
            PermissionUtil.requestPermissions(getActivity(), new PermissionUtil.PermissionCallback() {
                @Override
                public void onPermissionsGranted(String permission) {

                }

                @Override
                public void onPermissionsGranted() {
                    presenter.loadInvites(user.getId());
                }

                @Override
                public void onPermissionDenied() {

                }
            });

            if(preferenceUtil.getPreference(PREF_NOTIFICATION_TOKEN).isEmpty())
            {
                FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener( getActivity(), instanceIdResult -> {
                    String mToken = instanceIdResult.getToken();
                    preferenceUtil.savePreference(PREF_NOTIFICATION_TOKEN,mToken);
                    presenter.updateFcmToken(user.getId(),mToken);
                });
            }
        }
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
        mGeofencingClient = LocationServices.getGeofencingClient(getActivity());

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
        if(photoUri!=null)
        params.put("image_link", photoUri.toString());
        if(preferenceUtil.getPreference(PREF_NOTIFICATION_TOKEN).isEmpty())
        {
            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener( getActivity(), instanceIdResult -> {
                String mToken = instanceIdResult.getToken();
                preferenceUtil.savePreference(PREF_NOTIFICATION_TOKEN,mToken);
                params.put("fcm_key", mToken);
                extractPhoneNumber(params);
                Log.e("Token",mToken);
            });
        }else{
            params.put("fcm_key", preferenceUtil.getPreference(PREF_NOTIFICATION_TOKEN));
            extractPhoneNumber(params);
        }


    }

    @SuppressLint("MissingPermission")
    private void extractPhoneNumber(HashMap<String,Object> params){
        String mPhoneNumber=null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            List<SubscriptionInfo> subscription = SubscriptionManager.from(getActivity().getApplicationContext()).getActiveSubscriptionInfoList();
            if(subscription==null)
            {
                getMobileNumberFromUser(params,false);
                return;
            }

            for (int i = 0; i < subscription.size(); i++) {
                SubscriptionInfo info = subscription.get(i);

                Log.d(TAG, "number " + info.getNumber());
                Log.d(TAG, "network name : " + info.getCarrierName());
                Log.d(TAG, "country iso " + info.getCountryIso());
                mPhoneNumber = info.getNumber();
                break;
            }
        }else {
            TelephonyManager tMgr = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
            mPhoneNumber = tMgr.getLine1Number();

        }
        if (mPhoneNumber == null || mPhoneNumber.isEmpty()) {
            getMobileNumberFromUser(params,false);
            return;
        }
        params.put("mobile_number", mPhoneNumber);
        presenter.createAccount(params);
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
        try {
            setViewPager(removeSosSlide(slideItems));
            presenter.getKidsDirections(preferenceUtil.getAccount().getId());
            presenter.getSettings();
            progressBar.hide();
            getView().findViewById(R.id.rlBottom).setVisibility(View.VISIBLE);
        }catch (IllegalStateException e){
            Log.e(TAG,e.getMessage());
            if(getActivity()!=null)
            getActivity().finish();
        }
        setNextUpdateTime();
    }

    private void setNextUpdateTime(){
        String last_sync = preferenceUtil.getPreference(Constant.LAST_SYNC_TIME);
        Calendar nextSync = last_sync.isEmpty()?Calendar.getInstance():Util.formatDate(last_sync);
        nextSync.add(Calendar.HOUR_OF_DAY,5);
        Util.setAlarm(nextSync,getActivity());
       // long diff =  Util.getDifferenceInUnit(Util.formatDate(last_sync).getTime(),Calendar.getInstance().getTime(),3);

    }

    private void updateData(){
            presenter.getInvites(preferenceUtil.getAccount().getId());
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
        if(directions!=null && directions.size()>0) {
            PreferenceUtil.getInstance(getActivity()).saveSafePlaces(Constant.KEY_SAFE_PLACES,directions);
            addGeoFences(directions);
        }
    }

    @Override
    public void onSettingsUpdated(Setting setting) {
        if(!setting.getBackground().isEmpty())
            mBaseActivity.applyBg(setting.getBackground());
        if(getActivity()!=null)
            Util.updateSystemSettings(getActivity(),setting);
    }

    @Override
    public void phoneNumberExist(HashMap<String, Object> params) {
        getMobileNumberFromUser(params,true);
    }



    @Override
    public void setPresenter(DashboardContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showNoInternet() {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(AlarmReceiver.DataSyncEvent receiveEvent) {
        updateData();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(SlideCreateEvent receiveEvent) {
        presenter.addSlide(receiveEvent.getSlide());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(NotificationReceiveEvent receiveEvent) {
        if(receiveEvent.getNotificationForSlideType()== Constant.INVITE_CODE && receiveEvent.isSlideUpdate()){
            presenter.loadSlidesFromLocal();
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(SlideEvent receiveEvent) {
        if(receiveEvent.getSlideType()== Constant.SLIDE_INDEX_INVITE){
            //    if(!receiveEvent.isCreateSlide())
            //        presenter.removeSlideByType(Constant.SLIDE_INDEX_INVITE);
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

    private void getMobileNumberFromUser(HashMap<String,Object> params,boolean phoneExist) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getContext());
        View mView = layoutInflaterAndroid.inflate(R.layout.user_input_dialog_box, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(getContext());
        alertDialogBuilderUserInput.setView(mView);
        TextView title = mView.findViewById(R.id.dialogTitle);
        title.setText(!phoneExist?R.string.enter_mobile_number:R.string.enter_other_mobile_number);
        final EditText userInputDialogEditText = mView.findViewById(R.id.userInputDialog);
        userInputDialogEditText.setHint("Mobile number");
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton("OK", (dialogBox, id) -> {
                    String phoneNumber = userInputDialogEditText.getText().toString();
                    if(Util.isValidNumber(phoneNumber)) {
                        params.put("mobile_number",phoneNumber);
                        presenter.createAccount(params);
                    }
                    else
                    {
                        Toast.makeText(getContext(), "Please enter valid mobile number", Toast.LENGTH_SHORT).show();
                        getMobileNumberFromUser(params,phoneExist);
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

    private PendingIntent getGeofencePendingIntent() {
        // Reuse the PendingIntent if we already have it.
        if (mGeofencePendingIntent != null) {
            return mGeofencePendingIntent;
        }
        Intent intent = new Intent(getActivity(), GeofenceTransitionsIntentService.class);
        // intent.putExtra(KEY_GEOFENCE_EXTRA, (Serializable) location);
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when
        // calling addGeofences() and removeGeofences().
        mGeofencePendingIntent = PendingIntent.getService(getActivity(), 0, intent, PendingIntent.
                FLAG_UPDATE_CURRENT);
        return mGeofencePendingIntent;
    }


    @SuppressLint("MissingPermission")
    public void addGeoFences(List<com.uiu.kids.model.Location> locations) {

        mGeofenceList.clear();
        for (com.uiu.kids.model.Location location : locations) {
            Geofence geofence = Util.createGeofence(location.getId(),location.getLatitude(), location.getLongitude());
            mGeofenceList.add(geofence);
        }
        mGeofencingClient.addGeofences(getGeofencingRequest(mGeofenceList), getGeofencePendingIntent())
                .addOnSuccessListener(aVoid -> {

                    // Toast.makeText(getActivity(), "GeoFences Added", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {

                    //   Toast.makeText(instance, "GeoFences Not added Added: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
    private GeofencingRequest getGeofencingRequest(List<Geofence> geofence) {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_DWELL);
        builder.addGeofences(geofence);
        return builder.build();
    }
}
