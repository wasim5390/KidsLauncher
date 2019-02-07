package com.uiu.kids.ui.slides.sos;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.uiu.kids.BaseFragment;
import com.uiu.kids.Constant;
import com.uiu.kids.R;
import com.uiu.kids.event.notification.NotificationReceiveEvent;
import com.uiu.kids.model.User;
import com.uiu.kids.ui.SosManager;
import com.uiu.kids.ui.home.contact.ContactEntity;
import com.uiu.kids.util.PreferenceUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.OnLongClick;

import static android.app.Activity.RESULT_OK;

public class SOSFragment extends BaseFragment implements SOSContract.View, SOSListAdapter.Callback, View.OnLongClickListener {

    private static final String TAG = "SOSFragment";
    private SOSContract.Presenter presenter;
    private SOSListAdapter adapter;
    private int REQ_CALL = 0x888;
    private int REQ_CONTACT = 0x999;
    private int position = 0;
    private int listSize = 0;
    public boolean isclickItm=false;
    public boolean isphnCalling = false;
    private List<ContactEntity> entityList;

    @BindView(R.id.rvSos)
    public RecyclerView recyclerView;
    @BindView(R.id.tvEmpty)
    TextView tvEmptyList;
    @BindView(R.id.tvMsg)
    TextView tvMsg;
    @BindView(R.id.sosbtn)
    public ImageView btnSOS;
    SosManager manager;

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


    @Override
    public void initUI(View view) {

        EventBus.getDefault().register(this);
        setAdapter();
        presenter.start();

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
        tvEmptyList.setVisibility(sosList.size()>0?View.GONE:View.VISIBLE);
        btnSOS.setVisibility(sosList.isEmpty()?View.GONE:View.VISIBLE);
        tvMsg.setVisibility(sosList.isEmpty()?View.GONE:View.VISIBLE);

        adapter.setSlideItems(sosList);

    }

    @Override
    public void showMessage(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }
    @Override
    public void slideSerial(int serial, int count) {
        serial++;
        String pageNum = serial+"/"+count;
        ((TextView)getView().findViewById(R.id.tvSOSTitle)).setText(getString(R.string.sos)+" ("+pageNum+")");
    }
    @Override
    public void onSlideItemClick(ContactEntity slideItem) {
        new Handler().postDelayed(() -> {

                isclickItm=true;
                startCallInten(slideItem.getMobileNumber(), REQ_CALL);
        }, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("onEnter", String.valueOf(requestCode));
        if (requestCode == REQ_CONTACT) {
            if (resultCode == RESULT_OK) {
                User user = PreferenceUtil.getInstance(getActivity()).getAccount();
                presenter.saveFavoriteSOS((ContactEntity) data.getSerializableExtra(KEY_SELECTED_CONTACT), String.valueOf(user.getId()));
            }
        }
        if (requestCode == 900) {
            // Call Action belongs to SosManager
            manager.callNext();

        }
    }

    @OnLongClick(R.id.sosbtn)
    public boolean onLongClick(View v) {
        isclickItm=false;
        position = 0;
        presenter.getItemForCall();
        return true;
    }

    @Override
    public void startCallInten(String number, int callRequest) {

        try {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + number));

            if (ActivityCompat.checkSelfPermission(getContext(),
                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            startActivity(callIntent);

        } catch (ActivityNotFoundException activityException) {
            Log.e("dialing-example", "Call failed", activityException);
        }
    }


    @Override
    public void itemLoadForCall(List<ContactEntity> list) {

        if(!list.isEmpty()) {
             manager = new SosManager(list, getContext(), () -> {
               //  Toast.makeText(mBaseActivity, "All Called", Toast.LENGTH_SHORT).show();
             });
            manager.start();
           // entityList = list;
           // startCallInten(list.get(position).getmPhoneNumber(), REQ_CALL);
           // position++;
        }
        else
        {
            showMessage("You have not any SOS number yet");
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(NotificationReceiveEvent receiveEvent) {
        if (receiveEvent.getNotificationForSlideType() == Constant.SLIDE_INDEX_SOS) {
          /*  JSONObject jsonObject = receiveEvent.getNotificationResponse();
            ContactEntity entity = new Gson().fromJson(jsonObject.toString(), ContactEntity.class);
            if (entity.hasAccess()) {
                presenter.updateSOSItem(entity);
            }*/
          presenter.loadSOSList();
        }

    }

    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Unregister");
        EventBus.getDefault().unregister(this);


    }



}


