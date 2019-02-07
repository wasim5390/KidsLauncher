package com.uiu.kids.ui.share;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.uiu.kids.BaseFragment;
import com.uiu.kids.Constant;
import com.uiu.kids.R;
import com.uiu.kids.ui.home.contact.ContactEntity;


import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShareFragment extends BaseFragment implements ShareContract.View, ShareToAdapterList.Callback {
    public ShareContract.Presenter presenter;

    @BindView(R.id.rvMessage)
    public RecyclerView recyclerView;
    public ShareToAdapterList adapter;


     String FILE_PATH =null;
     int FILE_TYPE =4;

    public static ShareFragment newInstance(String filePath, int fileType) {
        Bundle args = new Bundle();
        ShareFragment instance = new ShareFragment();
        args.putString(Constant.RECORDED_FILE_PATH,filePath);
        args.putInt(Constant.RECORDED_FILE_TYPE,fileType);
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
         FILE_PATH = getArguments().getString(Constant.RECORDED_FILE_PATH);
         FILE_TYPE = getArguments().getInt(Constant.RECORDED_FILE_TYPE);
        presenter.start();
        setAdapter();
    }

    private void setAdapter() {
        adapter = new ShareToAdapterList(getContext(), this);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void setPresenter(ShareContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showNoInternet() {

    }

    @Override
    public void onSlideItemClick(ContactEntity slideItem, boolean isSelected) {


       // Intent intent=new Intent(getActivity(), ChatMessageActivity.class);
       // intent.putExtra("item",slideItem);
       // startActivityForResult(intent,0);
        if(FILE_PATH==null)
            return;
        UploadFileService.uploadMedia(getActivity().getApplicationContext(),FILE_PATH,FILE_TYPE,slideItem.getId());
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }

    @Override
    public void loadPeople(List<ContactEntity> list) {
        adapter.setSlideItems(list);
    }

    @Override
    public void showMessage(String msg) {

        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }



}