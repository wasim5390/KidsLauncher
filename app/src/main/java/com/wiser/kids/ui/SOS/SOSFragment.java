package com.wiser.kids.ui.SOS;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.i18n.phonenumbers.Phonenumber;
import com.wiser.kids.BaseFragment;
import com.wiser.kids.Constant;
import com.wiser.kids.R;
import com.wiser.kids.event.NotificationReceiveEvent;
import com.wiser.kids.model.User;
import com.wiser.kids.ui.favorite.people.FavoritePeopleAdapter;
import com.wiser.kids.ui.home.contact.ContactActivity;
import com.wiser.kids.ui.home.contact.ContactEntity;
import com.wiser.kids.ui.home.contact.info.ContactInfoActivity;
import com.wiser.kids.util.PreferenceUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.List;

import static android.app.Activity.RESULT_OK;

public class SOSFragment extends BaseFragment implements SOSContract.View, SOSListAdapter.Callback, View.OnLongClickListener {

    private static final String TAG = "SOSFragment";
    public SOSContract.Presenter presenter;
    public SOSListAdapter adapter;
    public RecyclerView recyclerView;
    public int REQ_CONTACT = 999;
    public TextView SOS_btn;
    private int REQ_CALL = 888;


    public static SOSFragment newInstance() {
        Bundle args = new Bundle();
        SOSFragment instance = new SOSFragment();
        instance.setArguments(args);
        return instance;
    }

    @Override
    public int getID() {
        return R.layout.fragment_so;
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void initUI(View view) {
//        Log.e("Emergency number", String.valueOf(PhoneNumberUtils.isLocalEmergencyNumber(getContext(),"112")));
//        Log.e("Emergency number", String.valueOf(PhoneNumberUtils.isLocalEmergencyNumber(getContext(),"911")));
//        Log.e("Emergency number", String.valueOf(PhoneNumberUtils.isLocalEmergencyNumber(getContext(),"999")));
//        Log.e("Emergency number", String.valueOf(PhoneNumberUtils.isLocalEmergencyNumber(getContext(),"1122")));
//        Log.e("Emergency number", String.valueOf(PhoneNumberUtils.isLocalEmergencyNumber(getContext(),"15")));
        EventBus.getDefault().register(this);
        init(view);
        setAdapter();
        addListner();
        presenter.start();

    }

    private void addListner() {
        SOS_btn.setOnLongClickListener(this);
    }

    private void init(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.rvSos);
        SOS_btn = (TextView) view.findViewById(R.id.sosbtn);
    }

    private void setAdapter() {
        adapter = new SOSListAdapter(getContext(), this);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void setPresenter(SOSContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showNoInternet() {

    }

    @Override
    public void onSOSListLoaded(List<ContactEntity> sosList) {
        adapter.setSlideItems(sosList);

    }

    @Override
    public void showMessage(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSlideItemClick(ContactEntity slideItem) {
        new Handler().postDelayed(() -> {
            if (slideItem.getName() == null) {
                startActivityForResult(new Intent(getContext(), ContactActivity.class), REQ_CONTACT);

            } else {

                startCallInten(slideItem.getmPhoneNumber());
            }
        }, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_CONTACT) {
            if (resultCode == RESULT_OK) {
                User user = PreferenceUtil.getInstance(getActivity()).getAccount();
                presenter.saveFavoriteSOS((ContactEntity) data.getSerializableExtra(KEY_SELECTED_CONTACT), String.valueOf(user.getId()));

            }
        }
        if (requestCode == REQ_CALL) {

                //Toast.makeText(getContext(), "aa"+String.valueOf(data.toString()), Toast.LENGTH_SHORT).show();

        }

    }

    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()) {
            case R.id.sosbtn:
                presenter.getItemForCall();
                break;


        }

        return true;
    }

    @Override
    public void startCallInten(String number) {

        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + number));

        if (ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startActivityForResult(callIntent, REQ_CALL);
    }

    @Override
    public void itemLoadForCall(List<ContactEntity> list) {
        startCallInten(list.get(2).getmPhoneNumber());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(NotificationReceiveEvent receiveEvent) {
        if (receiveEvent.getNotificationForSlideType() == Constant.SLIDE_INDEX_FAV_PEOPLE) {
            JSONObject jsonObject = receiveEvent.getNotificationResponse();
            ContactEntity entity = new Gson().fromJson(jsonObject.toString(), ContactEntity.class);
            if (entity.hasAccess()) {
                presenter.updateSOSItem(entity);
            }
        }

    }

    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Unregister");
        EventBus.getDefault().unregister(this);
    }


}

