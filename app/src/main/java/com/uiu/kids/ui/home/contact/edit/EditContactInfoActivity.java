package com.uiu.kids.ui.home.contact.edit;

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


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class EditContactInfoActivity extends BaseActivity implements Constant {

    private EditContactInfoPresenter mPresenter;
    private ContactEntity selectedContact;
    private EditContactInfoFragment mFragment;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.header_btn_right)
    ImageView btnRight;

    @Override
    public int getID() {
        return R.layout.activity_edit_contact;
    }

    @Override
    public void created(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        setToolBar(toolbar,"",true);
        btnRight.setVisibility(View.GONE);
        if(getIntent()!=null & getIntent().hasExtra(SELECTED_CONTACT))
       selectedContact =(ContactEntity) getIntent().getSerializableExtra(Constant.SELECTED_CONTACT);
       loadEditContactFragment(selectedContact);
    }


    private void loadEditContactFragment(ContactEntity contactEntity) {
        mFragment = mFragment!=null?mFragment: EditContactInfoFragment.newInstance(contactEntity);
        mPresenter =mPresenter!=null?mPresenter: new EditContactInfoPresenter(mFragment,contactEntity, Injection.provideRepository(this));
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
