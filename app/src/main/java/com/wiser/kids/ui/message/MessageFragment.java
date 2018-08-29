package com.wiser.kids.ui.message;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.wiser.kids.BaseFragment;
import com.wiser.kids.R;
import com.wiser.kids.ui.home.contact.ContactEntity;
import com.wiser.kids.ui.home.helper.HelperAdapterList;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MessageFragment extends BaseFragment implements MessageContract.View,MessageAdapterList.Callback
{
    public MessageContract.Presenter presenter;

    @BindView(R.id.rvMessage)
    public RecyclerView recyclerView;
    public MessageAdapterList adapter;
    public File mFile;
    public int type;



    public static MessageFragment newInstance(int type,String filePath){
        Bundle args=new Bundle();
        args.putString("path",filePath);
        args.putInt("type",type);
        MessageFragment instance=new MessageFragment();
        instance.setArguments(args);
        return instance;
    }

    @Override
    public int getID() {
        return R.layout.fragment_message;
    }

    @Override
    public void initUI(View view) {
        ButterKnife.bind(getActivity());
        mFile=new File((String)getArguments().getString("path"));
        type=((int)getArguments().getInt("type"));
        Log.e("type ", String.valueOf(type));
        Log.e("file", String.valueOf(mFile));
        presenter.start();
        setAdapter();
    }

    private void setAdapter() {
        adapter = new MessageAdapterList(getContext(), this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void setPresenter(MessageContract.Presenter presenter) {

        this.presenter=presenter;
    }

    @Override
    public void showNoInternet() {

    }

    @Override
    public void onSlideItemClick(ContactEntity slideItem ,boolean isSelected) {

        slideItem.setSelectedForSharing(isSelected);
    }

    @Override
    public void loadPeople(List<ContactEntity> list) {
        adapter.setSlideItems(list);
    }

    @Override
    public void showMessage(String msg) {

        Toast.makeText(getContext(),msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFileShared() {
        getActivity().finish();
    }


    @OnClick(R.id.msg_btnDone)
    public void btnDoeClicked()
    {
        List<ContactEntity> listSelectedContact= adapter.getSelectedContact();
        presenter.shareFileToContact(listSelectedContact,mFile,type);
    }
}