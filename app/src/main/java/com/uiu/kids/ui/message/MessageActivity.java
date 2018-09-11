package com.uiu.kids.ui.message;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.uiu.kids.BaseActivity;
import com.uiu.kids.Injection;
import com.uiu.kids.R;
import com.uiu.kids.util.PreferenceUtil;


import butterknife.ButterKnife;

public class MessageActivity extends BaseActivity {

    public MessageFragment messageFragment;
    public MessagePresenter messagePresenter;

    @Override
    public int getID() {
        return R.layout.activity_message2;
    }

    @Override
    public void created(Bundle savedInstanceState) {

        ButterKnife.bind(this);
       loadMessageFragment();

    }

    private void loadMessageFragment() {
        messageFragment = messageFragment != null ? messageFragment : MessageFragment.newInstance();
        messagePresenter = messagePresenter != null ? messagePresenter : new MessagePresenter(messageFragment, PreferenceUtil.getInstance(this), Injection.provideRepository(this));
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.chatframeLayout, messageFragment,"messageFragment");
        fragmentTransaction.commit();
    }
}
