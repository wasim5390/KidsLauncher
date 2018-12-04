package com.uiu.kids.ui.message.chatMessage;

import android.content.IntentFilter;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.uiu.kids.BaseActivity;
import com.uiu.kids.Injection;
import com.uiu.kids.R;
import com.uiu.kids.ui.home.contact.ContactEntity;
import com.uiu.kids.util.PreferenceUtil;


public class ChatMessageActivity extends BaseActivity {

    public ChatMessageFragment chatMessageFragment;
    public ChatMessagePresenter chatMessagePresenter;
    public ContactEntity slideItem;

    @Override
    public int getID() {
        return R.layout.activity_chat_message;
    }

    @Override
    public void created(Bundle savedInstanceState) {


        slideItem=((ContactEntity) getIntent().getSerializableExtra("item"));
        Log.e("slide name",slideItem.getName());
        loadChatMessageFragment();
    }

    private void loadChatMessageFragment() {
        chatMessageFragment = chatMessageFragment != null ? chatMessageFragment : ChatMessageFragment.newInstance(slideItem);
        chatMessagePresenter = chatMessagePresenter != null ? chatMessagePresenter : new ChatMessagePresenter(chatMessageFragment, PreferenceUtil.getInstance(this), Injection.provideRepository(this));
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.msgframeLayout, chatMessageFragment);
        fragmentTransaction.commit();
    }
    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(mGpsSwitchStateReceiver, new IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION));
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(mGpsSwitchStateReceiver);
    }
    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


}
