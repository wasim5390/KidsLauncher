package com.wiser.kids.ui.home.contact.info;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.wiser.kids.BaseActivity;
import com.wiser.kids.Constant;
import com.wiser.kids.Injection;
import com.wiser.kids.R;
import com.wiser.kids.ui.home.contact.ContactEntity;

import butterknife.OnClick;


public class ContactInfoActivity extends BaseActivity implements Constant {

    private ContactInfoPresenter mPresenter;
    private ContactEntity selectedContact;
    private ContactInfoFragment mFragment;
    @Override
    public int getID() {
        return R.layout.activity_contact_info;
    }

    @Override
    public void created(Bundle savedInstanceState) {
        if(getIntent()!=null & getIntent().hasExtra(SELECTED_CONTACT))
       selectedContact =(ContactEntity) getIntent().getSerializableExtra(Constant.SELECTED_CONTACT);
       loadContactInfoFragment(selectedContact);
    }


    private void loadContactInfoFragment(ContactEntity contactEntity) {
        mFragment = mFragment!=null?mFragment: ContactInfoFragment.newInstance();
        mPresenter =mPresenter!=null?mPresenter: new ContactInfoPresenter(mFragment,contactEntity, Injection.provideRepository(this));
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, mFragment);
        fragmentTransaction.commit();
    }

    @OnClick(R.id.header_btn_left)
    public void onBackClick(){
        onBackPressed();
    }
}
