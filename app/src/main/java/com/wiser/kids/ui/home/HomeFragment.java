package com.wiser.kids.ui.home;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.squareup.picasso.Picasso;
import com.wiser.kids.BaseFragment;
import com.wiser.kids.Constant;
import com.wiser.kids.Injection;
import com.wiser.kids.R;
import com.wiser.kids.event.GoogleLoginEvent;
import com.wiser.kids.ui.camera.editor.PhotoEditorActivity;
import com.wiser.kids.ui.home.apps.AppsActivity;
import com.wiser.kids.ui.home.contact.ContactActivity;
import com.wiser.kids.ui.home.dialer.DialerActivity;
import com.wiser.kids.ui.home.helper.HelperFragment;
import com.wiser.kids.ui.home.helper.HelperPresenter;
import com.wiser.kids.ui.message.MessageActivity;
import com.wiser.kids.util.PermissionUtil;
import com.wiser.kids.util.PreferenceUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class HomeFragment extends BaseFragment implements HomeContract.View, Constant, HomeSlideAdapter.Callback,
        PermissionUtil.PermissionCallback {

    private static final int REQ_CONTACT = 0x003;
    private static final int REQ_DIALER = 0x004;
    private static final int REQ_APPS = 0x005;
    private static final int REQ_CAMERA = 0x006;
    private static final int REQ_MESSAGING=0x007;
    public static String TAG = "HomeFragment";


    @BindView(R.id.rvHomeItems)
    RecyclerView recyclerView;
    @BindView(R.id.single_contact_avatar)
    ImageView mProfileImg;

    private HomeSlideAdapter adapterHomeSlide;
    private HomeContract.Presenter presenter;

    private HelperFragment helperFragment;
    private HelperPresenter helperPresenter;


    @Override
    public int getID() {
        return R.layout.fragment_home;
    }

    @Override
    public void initUI(View view) {
        EventBus.getDefault().register(this);
        setRecyclerView();
        if(presenter!=null)
        presenter.getSlideItems();
    }

    public static HomeFragment newInstance() {
        Bundle args = new Bundle();
        HomeFragment homeFragment = new HomeFragment();
        homeFragment.setArguments(args);
        return homeFragment;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Unregister");
        EventBus.getDefault().unregister(this);
    }

    public void setRecyclerView() {
        adapterHomeSlide = new HomeSlideAdapter(getContext(), this);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapterHomeSlide);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(GoogleLoginEvent account) {
        GoogleSignInAccount googleAccount = account.getAccount();
        try {
            Picasso.with(getContext()).load(googleAccount.getPhotoUrl()).fit().placeholder(R.mipmap.avatar_male2).error(R.mipmap.avatar_male2).into(mProfileImg);
        } catch (Exception e) {
            Picasso.with(getContext()).load(R.mipmap.avatar_male2).fit().into(mProfileImg);
        }
    }


    @Override
    public void showMessage() {

    }

    @Override
    public void slideItemsLoaded(List<String> list) {
        adapterHomeSlide.setSlideItems(list);
    }

    @Override
    public void setPresenter(HomeContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showNoInternet() {

    }

    @Override
    public void onSlideItemClick(String slideItem) {
        switch (slideItem) {
            case CONTACTS:
                if (PermissionUtil.isPermissionGranted(mBaseActivity, Manifest.permission.WRITE_CONTACTS)) {
                    gotoContact();
                } else
                    PermissionUtil.requestPermission(mBaseActivity, Manifest.permission.WRITE_CONTACTS, this);
                break;
            case DIALER:
                if (PermissionUtil.isPermissionGranted(mBaseActivity, Manifest.permission.CALL_PHONE)) {
                    gotoDialer();
                } else
                    PermissionUtil.requestPermission(mBaseActivity, Manifest.permission.CALL_PHONE, this);
                break;
            case APPLICATIONS:
                gotoApplication();
                break;
            case CAMERA:
                gotoCamera();
                break;
            case C_ME:
                goToMessage();
                break;

            /*case MESSAGING:
                goToMessage();
                break;*/

        }

    }

    private void goToMessage() {
        new Handler().postDelayed(() -> {
           startActivityForResult(new Intent(getContext(), MessageActivity.class), REQ_MESSAGING);

        }, 230);
    }


    @Override
    public void onPermissionsGranted(String permission) {
        switch (permission) {
            case Manifest.permission.WRITE_CONTACTS:
                gotoContact();
                break;
            case Manifest.permission.WRITE_CALL_LOG:
                break;
        }
    }

    @Override
    public void onPermissionsGranted() {

    }

    @Override
    public void onPermissionDenied() {

    }

    private void gotoContact() {
        new Handler().postDelayed(() -> {
            startActivityForResult(new Intent(getContext(), ContactActivity.class), REQ_CONTACT);

        }, 230);
    }

    private void gotoDialer() {
        new Handler().postDelayed(() -> {
            startActivityForResult(new Intent(getContext(), DialerActivity.class), REQ_DIALER);

        }, 230);
    }


    private void gotoApplication() {
        new Handler().postDelayed(() -> {
            startActivityForResult(new Intent(getContext(), AppsActivity.class), REQ_APPS);

        }, 230);

    }

    private void gotoCamera() {
        new Handler().postDelayed(() -> {
            startActivityForResult(new Intent(getContext(), PhotoEditorActivity.class), REQ_CAMERA);

        }, 230);

    }

    @OnClick(R.id.btnConfig)
    public void goToHelper() {
        helperFragment = null;
        helperPresenter = null;
        loadHelperFragment();

    }

    private void loadHelperFragment() {
        helperFragment = helperFragment != null ? helperFragment : helperFragment.newInstance();
        helperPresenter = helperPresenter != null ? helperPresenter : new HelperPresenter(helperFragment, PreferenceUtil.getInstance(getContext()), Injection.provideRepository(getContext()));
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
        fragmentTransaction.replace(R.id.frameLayoutHome, helperFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

}
