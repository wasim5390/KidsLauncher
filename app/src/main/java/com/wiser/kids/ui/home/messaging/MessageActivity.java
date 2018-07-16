package com.wiser.kids.ui.home.messaging;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.wiser.kids.BaseActivity;
import com.wiser.kids.Injection;
import com.wiser.kids.R;
import com.wiser.kids.ui.home.dialer.callhistory.CallHistoryFragment;
import com.wiser.kids.ui.home.dialer.callhistory.CallHistoryPresenter;
import com.wiser.kids.util.PreferenceUtil;

public class MessageActivity extends BaseActivity {

    private MessagePresenter messagePresenter;
    private MessageFragment messageFragment;

    @Override
    public int getID() {
        return R.layout.activity_message;
    }

    @Override
    public void created(Bundle savedInstanceState) {


        loadMessageFragment();
    }

    private void loadMessageFragment() {
        messageFragment = messageFragment !=null? messageFragment : MessageFragment.newInstance();
        messagePresenter = messagePresenter !=null? messagePresenter : new MessagePresenter(messageFragment, PreferenceUtil.getInstance(this), Injection.provideRepository(this));
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, messageFragment);
        fragmentTransaction.commit();
    }

}
