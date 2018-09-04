package com.wiser.kids.ui.SOS;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.wiser.kids.BaseFragment;
import com.wiser.kids.Constant;
import com.wiser.kids.R;
import com.wiser.kids.event.NotificationReceiveEvent;
import com.wiser.kids.model.User;
import com.wiser.kids.ui.home.contact.ContactActivity;
import com.wiser.kids.ui.home.contact.ContactEntity;
import com.wiser.kids.util.PreferenceUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

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
    @BindView(R.id.sosbtn)
    public TextView btnSOS;


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
        btnSOS.setEnabled(false);
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
        if (sosList.size() > 1) {
            btnSOS.setEnabled(true);
        }
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

                isclickItm=true;
                startCallInten(slideItem.getmPhoneNumber(), REQ_CALL);
            }
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
        if (requestCode == REQ_CALL) {

            if (entityList.size() > position + 1) {
                Log.e("OnActivityresult", String.valueOf(position));
                startCallInten(entityList.get(position).getmPhoneNumber(), 12);
                position++;
            }
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
        } finally {

            isphnCalling = false;
            EndCallListener callListener = new EndCallListener();
            TelephonyManager mTM = (TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE);
            mTM.listen(callListener, PhoneStateListener.LISTEN_CALL_STATE);
        }
    }


    @Override
    public void itemLoadForCall(List<ContactEntity> list) {

        if(!list.isEmpty()) {
            entityList = list;
            startCallInten(list.get(position).getmPhoneNumber(), REQ_CALL);
            position++;
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(NotificationReceiveEvent receiveEvent) {
        if (receiveEvent.getNotificationForSlideType() == Constant.SLIDE_INDEX_SOS) {
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


    public class EndCallListener extends PhoneStateListener {


        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            Log.e("stat    ", String.valueOf(state) + incomingNumber);
            if (TelephonyManager.CALL_STATE_RINGING == state) {
            }
            if (TelephonyManager.CALL_STATE_OFFHOOK == state) {
                Log.e("off hook", String.valueOf(state));

                isphnCalling = true;

            }
            if (TelephonyManager.CALL_STATE_IDLE == state) {

                Log.e("state idle", String.valueOf(state));


                if (isphnCalling) {

                    if (!isclickItm) {

                        if (entityList!=null &&entityList.size() > position) {
                            Log.e("OnActivityresult", String.valueOf(position));
                            startCallInten(entityList.get(position).getmPhoneNumber(), REQ_CALL);
                        }
                        position++;
                    }
                }

                isphnCalling = false;
                Log.e("isphnCalling", String.valueOf(isphnCalling));


            }

        }


    }


}


