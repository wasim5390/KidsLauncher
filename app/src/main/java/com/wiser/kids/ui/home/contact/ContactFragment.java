package com.wiser.kids.ui.home.contact;

import android.os.Bundle;
import android.view.View;

import com.wiser.kids.BaseFragment;
import com.wiser.kids.Constant;
import com.wiser.kids.R;

import java.util.ArrayList;
import java.util.List;

public class ContactFragment extends BaseFragment implements Constant,ContactContract.View {

    private ContactContract.Presenter presenter;
    private final ArrayList<ContactEntity> mContactsList = new ArrayList<>();

    public static ContactFragment newInstance() {
        Bundle args = new Bundle();
        ContactFragment homeFragment = new ContactFragment();
        homeFragment.setArguments(args);
        return homeFragment;
    }


    @Override
    public int getID() {
        return R.layout.fragment_contact;
    }

    @Override
    public void initUI(View view) {
        presenter.loadContacts();
    }

    @Override
    public void showMessage() {

    }

    @Override
    public void onContactsLoaded(List<String> list) {

    }

    @Override
    public void setPresenter(ContactContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showNoInternet() {

    }


}
