package com.wiser.kids.ui.home.contact.info;

import android.os.Bundle;
import android.view.View;

import com.wiser.kids.BaseFragment;
import com.wiser.kids.R;

public class ContactInfoFragment extends BaseFragment implements ContactInfoContract.View{

    private ContactInfoContract.Presenter presenter ;

    public static ContactInfoFragment newInstance() {
        Bundle args = new Bundle();
        ContactInfoFragment homeFragment = new ContactInfoFragment();
        homeFragment.setArguments(args);
        return homeFragment;
    }


    @Override
    public int getID() {
        return R.layout.fragment_contact_info;
    }

    @Override
    public void initUI(View view) {

    }

    @Override
    public void showMessage() {

    }

    @Override
    public void setPresenter(ContactInfoContract.Presenter presenter) {
    this.presenter = presenter;
    }

    @Override
    public void showNoInternet() {

    }
}
