package com.wiser.kids.ui.message.chatMessage;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.wiser.kids.BaseActivity;
import com.wiser.kids.Injection;
import com.wiser.kids.R;
import com.wiser.kids.ui.home.contact.ContactEntity;
import com.wiser.kids.util.PreferenceUtil;

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
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


}
