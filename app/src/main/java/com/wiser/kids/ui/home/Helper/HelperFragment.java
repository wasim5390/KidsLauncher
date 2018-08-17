package com.wiser.kids.ui.home.Helper;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.wiser.kids.BaseFragment;
import com.wiser.kids.R;
import com.wiser.kids.ui.reminder.ReminderAdapterList;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class HelperFragment extends BaseFragment implements HelperContract.view,HelperAdapterList.Callback {

    public HelperContract.Presenter presenter;
    public HelperAdapterList adapter;

    @BindView(R.id.rvhelper)
    RecyclerView recyclerView;





    public static HelperFragment newInstance()
    {
        Bundle args=new Bundle();
        HelperFragment instance=new HelperFragment();
        instance.setArguments(args);
        return instance;
    }


    @Override
    public int getID() {
        return R.layout.fragment_helper;
    }

    @Override
    public void initUI(View view) {
        ButterKnife.bind(getActivity());
        presenter.start();
        setAdapter();
    }

    private void setAdapter() {
        adapter = new HelperAdapterList(getContext(), new ArrayList<>(), this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }


    @Override
    public void setPresenter(HelperContract.Presenter presenter) {
        this.presenter=presenter;
    }

    @Override
    public void showNoInternet() {

    }

    @Override
    public void onSlideItemClick(HelperEntity slideItem) {

    }

    @Override
    public void loadHelperList(List<HelperEntity> list) {
        adapter.setSlideItems(list);
    }

    @Override
    public void showMessage(String msg) {
        Toast.makeText(getContext(),msg,Toast.LENGTH_SHORT).show();
    }
}
