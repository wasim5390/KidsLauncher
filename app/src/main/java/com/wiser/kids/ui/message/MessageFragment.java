package com.wiser.kids.ui.message;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.wiser.kids.BaseFragment;
import com.wiser.kids.Injection;
import com.wiser.kids.R;
import com.wiser.kids.ui.home.contact.ContactEntity;
import com.wiser.kids.ui.message.chatMessage.ChatMessageActivity;
import com.wiser.kids.ui.message.chatMessage.ChatMessageFragment;
import com.wiser.kids.ui.message.chatMessage.ChatMessagePresenter;
import com.wiser.kids.util.PreferenceUtil;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MessageFragment extends BaseFragment implements MessageContract.View, MessageAdapterList.Callback {
    public MessageContract.Presenter presenter;

    @BindView(R.id.rvMessage)
    public RecyclerView recyclerView;
    public MessageAdapterList adapter;

    public ChatMessageFragment chatMessageFragment;
    public ChatMessagePresenter chatMessagePresenter;

    public static MessageFragment newInstance() {
        Bundle args = new Bundle();
        MessageFragment instance = new MessageFragment();
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
        this.presenter = presenter;
    }

    @Override
    public void showNoInternet() {

    }

    @Override
    public void onSlideItemClick(ContactEntity slideItem, boolean isSelected) {


        Intent intent=new Intent(getActivity(), ChatMessageActivity.class);
        intent.putExtra("item",slideItem);
        startActivityForResult(intent,0);

    }

    @Override
    public void loadPeople(List<ContactEntity> list) {
        adapter.setSlideItems(list);
    }

    @Override
    public void showMessage(String msg) {

        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFileShared() {
        getActivity().finish();
    }


}