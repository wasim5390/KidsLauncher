package com.uiu.kids.ui.home.contact;

import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.uiu.kids.BaseActivity;
import com.uiu.kids.Injection;
import com.uiu.kids.R;
import com.uiu.kids.ui.home.contact.add.AddNewContactActivity;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ContactActivity extends BaseActivity {
    ContactFragment contactFragment;
    ContactPresenter contactPresenter;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    public int getID() {
        return R.layout.activity_contact;
    }

    @Override
    public void created(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        setToolBar(toolbar,"",true);
       // toolbar.findViewById(R.id.header_btn_right).setVisibility(View.GONE);
        loadContactFragment();
    }

    private void loadContactFragment() {
        contactFragment = contactFragment !=null? contactFragment : ContactFragment.newInstance();
        contactPresenter = contactPresenter !=null? contactPresenter : new ContactPresenter(contactFragment, ContactLoader.getInstance(this), Injection.provideRepository(this));
       // ContactsFragment fragment = new ContactsFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, contactFragment);
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

    @OnClick(R.id.header_btn_left)
    public void onBackClick(){
        onBackPressed();
    }

    @OnClick(R.id.header_btn_right)
    public void onAddNewContact(){
        Intent intent = new Intent();
        intent.setClass(this, AddNewContactActivity.class);
        startActivityForResult(intent,0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==RESULT_OK && data!=null){
            ContactEntity entity = (ContactEntity)data.getSerializableExtra(KEY_SELECTED_CONTACT);
            setResult(RESULT_OK,data);
            finish();
        }

    }
}
