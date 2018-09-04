package com.wiser.kids.ui.favorite.links;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.wiser.kids.BaseFragment;
import com.wiser.kids.Constant;
import com.wiser.kids.R;
import com.wiser.kids.event.NotificationReceiveEvent;
import com.wiser.kids.model.LinksEntity;
import com.wiser.kids.util.Util;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FavoriteLinksFragment extends BaseFragment implements FavoriteLinksContract.View,FavoriteLinksAdapter.Callback{

    private static final String TAG = "FavoriteLinksFragment";

    public FavoriteLinksContract.Presenter presenter;
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
        init(view);
        setAdapter();
        presenter.start();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Unregister");
        EventBus.getDefault().unregister(this);
    }
    private void init(View view) {
        rvFavoriteLinks=(RecyclerView) view.findViewById(R.id.rvFavLinks);
    }

    private void setAdapter() {
        adapter = new FavoriteLinksAdapter(getContext(),new ArrayList<>(),this);
        rvFavoriteLinks.setLayoutManager(new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false));
        rvFavoriteLinks.setHasFixedSize(true);
        rvFavoriteLinks.setAdapter(adapter);

    }

    @Override
    public void setPresenter(FavoriteLinksContract.Presenter presenter) {
    this.presenter=presenter;
    }

    @Override
    public void showNoInternet() {

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
    public void onSlideItemClick(LinksEntity slideItem) {

        new Handler().postDelayed(() -> {

            if (slideItem.getLinkName() == null) {

                dialogWindowForLink();

            } else {

                Util.startFromLink(slideItem.link,getContext());

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
                .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {

                        if(Patterns.WEB_URL.matcher(userInputDialogEditText.getText().toString()).matches()) {
                            presenter.getFavLinkData(userInputDialogEditText.getText().toString());
                            }
                        else
                        {
                            Toast.makeText(getContext(), "url doesn't match", Toast.LENGTH_SHORT).show();

                        }

                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {

                                dialogBox.cancel();

                            }
                        });

        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(NotificationReceiveEvent receiveEvent) {
        if(receiveEvent.getNotificationForSlideType()== Constant.SLIDE_INDEX_FAV_LINKS){
            JSONObject jsonObject = receiveEvent.getNotificationResponse();
            LinksEntity entity =  new Gson().fromJson(jsonObject.toString(),LinksEntity.class);
            if(entity.hasAccess()){
                presenter.updateFavLink(entity);
            }
        }

    }
}
