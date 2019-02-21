package com.uiu.kids.ui.home.contact.info;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.uiu.kids.BaseActivity;
import com.uiu.kids.Constant;
import com.uiu.kids.Injection;
import com.uiu.kids.R;
import com.uiu.kids.ui.home.contact.ContactEntity;
import com.uiu.kids.ui.home.contact.edit.EditContactInfoActivity;
import com.uiu.kids.util.PreferenceUtil;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ContactInfoActivity extends BaseActivity implements Constant {

    private static final int REQ_EDIT = 0x011;
    private ContactInfoPresenter mPresenter;
    private ContactEntity selectedContact;
    private ContactInfoFragment mFragment;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.header_btn_right)
    ImageView btnRight;

    @Override
    public int getID() {
        return R.layout.activity_contact_info;
    }

    @Override
    public void created(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        setToolBar(toolbar,"",true);
        btnRight.setVisibility(View.GONE);
        if(getIntent()!=null & getIntent().hasExtra(SELECTED_CONTACT))
       selectedContact =(ContactEntity) getIntent().getSerializableExtra(Constant.SELECTED_CONTACT);
       loadContactInfoFragment(selectedContact);
    }


    private void loadContactInfoFragment(ContactEntity contactEntity) {
        mFragment = mFragment!=null?mFragment: ContactInfoFragment.newInstance();
        mPresenter =mPresenter!=null?mPresenter: new ContactInfoPresenter(mFragment,contactEntity, PreferenceUtil.getInstance(this), Injection.provideRepository(this));
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, mFragment);
        fragmentTransaction.commit();
    }

    @OnClick(R.id.header_btn_left)
    public void onBackClick(){
        onBackPressed();
    }

    @OnClick(R.id.header_btn_right)
    public void onEditClick(){
        Intent intent = new Intent();
        intent.putExtra(Constant.SELECTED_CONTACT,selectedContact);
        intent.setClass(this,EditContactInfoActivity.class);
        startActivityForResult(intent,REQ_EDIT);
    }
}
