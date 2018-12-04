package com.uiu.kids.ui.slides.links;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.uiu.kids.BaseFragment;
import com.uiu.kids.Constant;
import com.uiu.kids.R;
import com.uiu.kids.event.NotificationReceiveEvent;
import com.uiu.kids.event.SlideCreateEvent;
import com.uiu.kids.model.LinksEntity;
import com.uiu.kids.model.Slide;
import com.uiu.kids.util.Util;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class FavoriteLinksFragment extends BaseFragment implements FavoriteLinksContract.View,FavoriteLinksAdapter.Callback{

    private static final String TAG = "FavoriteLinksFragment";

    public FavoriteLinksContract.Presenter presenter;
    @BindView(R.id.rvFavLinks)
    public RecyclerView rvFavoriteLinks;
    public FavoriteLinksAdapter adapter;

    public static FavoriteLinksFragment newInstance()
    {
        Bundle args=new Bundle();
        FavoriteLinksFragment instance=new FavoriteLinksFragment();
        instance.setArguments(args);
        return instance;
    }

    @Override
    public int getID() {
        return R.layout.fragment_favorite_links;
    }

    @Override
    public void initUI(View view) {
        EventBus.getDefault().register(this);
        setAdapter();
        if(presenter!=null)
        presenter.start();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Unregister");
        EventBus.getDefault().unregister(this);
    }


    private void setAdapter() {
        adapter = new FavoriteLinksAdapter(getContext(),new ArrayList<>(),this);
        rvFavoriteLinks.setLayoutManager(new LinearLayoutManager(getContext()));
        rvFavoriteLinks.setHasFixedSize(true);
        rvFavoriteLinks.setAdapter(adapter);

    }

/*    @Override
    public void setUserVisibleHint(boolean isFragmentVisible_) {
        super.setUserVisibleHint(true);
        if (this.isVisible()) {
// we check that the fragment is becoming visible
            if (isFragmentVisible_ ) {
                presenter.loadFavLinks();
            }
        }
    }*/

    @Override
    public void setPresenter(FavoriteLinksContract.Presenter presenter) {
    this.presenter=presenter;
    }

    @Override
    public void showNoInternet() {
        Toast.makeText(mBaseActivity, "No internet connection!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFavoriteLinksLoaded(List<LinksEntity> linksEntities) {
        adapter.setSlideItems(linksEntities);
    }

    @Override
    public void showProgressbar() {
        showProgress();
    }

    @Override
    public void hideProgressbar() {
      hideProgress();
    }

    @Override
    public void showMassage(String msg) {
        Toast.makeText(getContext(),msg,Toast.LENGTH_SHORT).show();
    }
    @Override
    public void slideSerial(int serial,int count) {
        serial++;
        String pageNum = serial+"/"+count;
        ((TextView)getView().findViewById(R.id.tvFavLinkTitle)).setText(getString(R.string.favorite_links)+" ("+pageNum+")");
    }

    @Override
    public void itemAddedOnNewSlide(Slide newSlide) {
        EventBus.getDefault().postSticky(new SlideCreateEvent(newSlide));
    }

    @Override
    public void onFavoriteLinkDataLoaded(String originalLink, LinksEntity entity) {
        if(entity==null)
        {
            entity = new LinksEntity(originalLink,"","","");
        }
        presenter.addFavLinkOnSlide(entity);
    }

    @Override
    public void onNewSlideCreated(Slide slideItem) {

    }

    @Override
    public void onSlideItemClick(LinksEntity slideItem) {

        new Handler().postDelayed(() -> {

            if (slideItem.getLink() == null) {

                dialogWindowForLink();

            } else {

                Util.startFromLink(slideItem.getLink(),getActivity());

            }
        }, 1);
    }


    private void dialogWindowForLink() {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getContext());
        View mView = layoutInflaterAndroid.inflate(R.layout.user_input_dialog_box, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(getContext());
        alertDialogBuilderUserInput.setView(mView);
        final EditText userInputDialogEditText = (EditText) mView.findViewById(R.id.userInputDialog);
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton("Send", (dialogBox, id) -> {

                    if(Patterns.WEB_URL.matcher(userInputDialogEditText.getText().toString()).matches()) {
                        presenter.getFavLinkData(userInputDialogEditText.getText().toString());
                        }
                    else
                    {
                        Toast.makeText(getContext(), "url doesn't match", Toast.LENGTH_SHORT).show();
                    }

                })
                .setNegativeButton("Cancel",
                        (dialogBox, id) -> dialogBox.cancel());

        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.show();
    }
    @OnClick(R.id.btnAddNew)
    public void addNew(){
        if(presenter.canAddOnSlide())
            dialogWindowForLink();
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(NotificationReceiveEvent receiveEvent) {
        if(receiveEvent.getNotificationForSlideType()== Constant.SLIDE_INDEX_FAV_LINKS
                && receiveEvent.isSlideUpdate()
                ){
           /* JSONObject jsonObject = receiveEvent.getNotificationResponse();
            LinksEntity entity =  new Gson().fromJson(jsonObject.toString(),LinksEntity.class);
            if(entity.hasAccess()){
                presenter.updateFavLink(entity);
            }*/
           presenter.start();
        }

    }
}
