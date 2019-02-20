package com.uiu.kids.ui.slides.people;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.uiu.kids.BaseFragment;
import com.uiu.kids.Constant;
import com.uiu.kids.R;
import com.uiu.kids.event.InviteUpdatedEvent;
import com.uiu.kids.event.notification.NotificationReceiveEvent;
import com.uiu.kids.event.SlideCreateEvent;
import com.uiu.kids.model.Slide;
import com.uiu.kids.model.User;
import com.uiu.kids.ui.home.contact.ContactActivity;
import com.uiu.kids.ui.home.contact.ContactEntity;
import com.uiu.kids.ui.home.contact.info.ContactInfoActivity;
import com.uiu.kids.util.PreferenceUtil;
import com.uiu.kids.util.Util;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

public class FavoritePeopleFragment extends BaseFragment implements FavoritePeopleAdapter.Callback,
        FavoritePeopleContract.View
{
    private static final int REQ_CONTACT = 0x101;
    private static final int REQ_CONTACT_INFO = 0x1020;
    public static String TAG ="FavoritePeopleFragment";

    @BindView(R.id.tvFavPeopleTitle)
    TextView title;
    @BindView(R.id.rvFavPeoples)
    RecyclerView recyclerView;
    @BindView(R.id.progressBar)
    ContentLoadingProgressBar progressBar;
    private FavoritePeopleAdapter adapter;
    FavoritePeopleContract.Presenter mPresenter;

    public static FavoritePeopleFragment newInstance() {
        Bundle args = new Bundle();
        FavoritePeopleFragment homeFragment = new FavoritePeopleFragment();
        homeFragment.setArguments(args);
        return homeFragment;
    }

    @Override
    public int getID() {
        return R.layout.fragment_favorite_people;
    }

    @Override
    public void initUI(View view) {
        EventBus.getDefault().register(this);
        setRecyclerView();
        if(mPresenter!=null)
        mPresenter.start();
    }
    public void setRecyclerView(){
        adapter = new FavoritePeopleAdapter(getContext(),this);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2,GridLayoutManager.VERTICAL,false));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Unregister");
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void setUserVisibleHint(boolean isFragmentVisible_) {
        super.setUserVisibleHint(true);
        if (this.isVisible()) {
// we check that the fragment is becoming visible
            if (isFragmentVisible_ ) {
                progressBar.show();
                mPresenter.start();
            }
        }
    }

    @Override
    public void onSlideItemClick(ContactEntity slideItem) {
        new Handler().postDelayed(() -> {
            if(slideItem.getId()==null)
            {
                startActivityForResult(new Intent(getContext(), ContactActivity.class),REQ_CONTACT);
            }else{
                Intent i = new Intent(getContext(), ContactInfoActivity.class);
                i.putExtra(Constant.SELECTED_CONTACT, slideItem);
                startActivityForResult(i,REQ_CONTACT_INFO);
            }
        },1);

    }

    @Override
    public void showMessage(String message) {
        if(getActivity()!=null)
        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void hideProgress() {
        progressBar.hide();
    }

    @Override
    public void showProgress() {
        progressBar.show();
    }

    @Override
    public void slideSerial(int serial,int count) {
        serial++;
        String pageNum = serial+"/"+count;
        title.setText(getString(R.string.favorite_people)+" ("+pageNum+")");
    }

    @Override
    public void onNewSlideCreated(Slide slide) {

    }

    @Override
    public void itemAddedOnNewSlide(Slide newSlide) {
        EventBus.getDefault().postSticky(new SlideCreateEvent(newSlide));
    }

    @Override
    public void onFavoritePeopleLoaded(List<ContactEntity> list) {
        adapter.setSlideItems(list);
        progressBar.hide();
    }

    @Override
    public void onFavoritePeopleSaved() {

    }

    @Override
    public void setPresenter(FavoritePeopleContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showNoInternet() {

    }

    @OnClick(R.id.btnAddNew)
    public void addNew(){
        if(mPresenter.canAddOnSlide())
        startActivityForResult(new Intent(getContext(), ContactActivity.class),REQ_CONTACT);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(NotificationReceiveEvent receiveEvent) {
        if(receiveEvent.getNotificationForSlideType()== Constant.SLIDE_INDEX_FAV_PEOPLE
                && receiveEvent.isSlideUpdate()
                ){
            mPresenter.start();
           /* JSONObject jsonObject = receiveEvent.getNotificationResponse();
            ContactEntity entity =  new Gson().fromJson(jsonObject.toString(),ContactEntity.class);
            if(entity.hasAccess()){
                mPresenter.updateFavoritePeople(entity);
            }*/
           // mPresenter.loadFavoritePeoples();
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(InviteUpdatedEvent receiveEvent) {
        if(adapter!=null)
            mPresenter.loadFavoritePeoples();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==REQ_CONTACT){
            if(resultCode==RESULT_OK){
                User user= PreferenceUtil.getInstance(getActivity()).getAccount();
                ContactEntity entity = (ContactEntity) data.getSerializableExtra(KEY_SELECTED_CONTACT);
                Uri uri = Uri.parse(entity.getPhotoUri());


                AssetFileDescriptor fd = null;
                try {
                    fd = getContext().getContentResolver().openAssetFileDescriptor(uri, "r");
                    InputStream inputStream = fd.createInputStream();
                    BufferedInputStream buf =new BufferedInputStream(inputStream);
                    Bitmap my_btmp = BitmapFactory.decodeStream(buf);
                    entity.setBase64ProfilePic(Util.bitmapToBase64(my_btmp));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                mPresenter.saveFavoritePeople(entity,String.valueOf(user.getId()));
            }
        }else{
            mPresenter.start();
        }
    }

}
