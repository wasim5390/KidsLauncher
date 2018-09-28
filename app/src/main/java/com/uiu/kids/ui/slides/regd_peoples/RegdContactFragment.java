package com.uiu.kids.ui.slides.regd_peoples;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.Toast;

import com.uiu.kids.BaseFragment;
import com.uiu.kids.Constant;
import com.uiu.kids.R;
import com.uiu.kids.ui.home.contact.ContactEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static android.view.View.VISIBLE;

public class RegdContactFragment extends BaseFragment implements Constant,RegdContactContract.View,
        RegdContactListAdapter.OnItemClickListener,SearchView.OnQueryTextListener {

    private static final int REQ_CONTACT_INFO = 0x005;
    private RegdContactContract.Presenter presenter;
    private List<ContactEntity> mContactsList;

    @BindView(R.id.contact_list)
    RecyclerView mRecyclerView;

    @BindView(R.id.searchViewContact)
    SearchView mContactSearchView;


    private RegdContactListAdapter adapter;

    public static RegdContactFragment newInstance() {
        Bundle args = new Bundle();
        RegdContactFragment homeFragment = new RegdContactFragment();
        homeFragment.setArguments(args);
        return homeFragment;
    }


    @Override
    public int getID() {
        return R.layout.fragment_contact;
    }

    @Override
    public void initUI(View view) {
        mContactsList = new ArrayList<>();
        setAdapter();
        presenter.start();
        mContactSearchView.setOnQueryTextListener(this);
        mContactSearchView.setOnClickListener(v -> {
            if (mContactSearchView.isIconified()) {
                mContactSearchView.setIconified(false);
            }
        });
        try {
            View v = mContactSearchView.findViewById(android.support.v7.appcompat.R.id.search_plate);
            v.setBackgroundColor(ResourcesCompat.getColor(getResources(), android.R.color.transparent, null));
            updateSearchView(mContactSearchView);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }



    private void setAdapter(){
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new RegdContactListAdapter(getContext(),new ArrayList<>(), this);
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onContactsLoaded(List<ContactEntity> list) {

        mContactsList.clear();
        mContactsList.addAll(list);
        adapter.updateItems(list);

        view.findViewById(R.id.tvSearchTip).setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onEmptySearchResult() {
        view.findViewById(R.id.tvSearchTip).setVisibility(VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
    }


    @Override
    public void setPresenter(RegdContactContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showNoInternet() {

    }



    @Override
    public void onItemClick(int position) {
        ContactEntity contact = adapter.getItem(position);
        contact.setHasAccess(false);
        Intent i = getActivity().getIntent();
        i.putExtra(Constant.KEY_SELECTED_CONTACT, contact);
        getActivity().setResult(Activity.RESULT_OK, i);
        getActivity().finish();
       /* Intent i = new Intent(getContext(), ContactInfoActivity.class);
        i.putExtra(Constant.SELECTED_CONTACT, contact);
        startActivityForResult(i,REQ_CONTACT_INFO);*/
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        presenter.searchContacts(newText);
        return true;
    }
}
