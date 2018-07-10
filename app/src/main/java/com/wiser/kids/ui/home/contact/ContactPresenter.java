package com.wiser.kids.ui.home.contact;

import com.wiser.kids.source.Repository;
import com.wiser.kids.util.PreferenceUtil;

public class ContactPresenter implements ContactContract.Presenter {

    private ContactContract.View view;
    private PreferenceUtil preferenceUtil;
    private Repository repository;

    public ContactPresenter(ContactContract.View view, PreferenceUtil util, Repository repository) {
        this.view = view;
        this.preferenceUtil = util;
        this.repository = repository;
        this.view.setPresenter(this);
    }

    @Override
    public void loadContacts() {

    }

    @Override
    public void start() {

    }
}
