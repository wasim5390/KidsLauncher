package com.wiser.kids.ui.message.MessageAudioRecord;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.wiser.kids.BaseFragment;
import com.wiser.kids.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MessageAudioRecordFragment extends BaseFragment implements MessageAudioRecordContract.View {

    @BindView(R.id.recording)
    public ImageView recording;

    @BindView(R.id.stop_recording)
    public ImageView stopRecording;

    public MessageAudioRecordContract.Presenter presenter;


    public static MessageAudioRecordFragment newInstance() {
        Bundle args = new Bundle();
        MessageAudioRecordFragment instance = new MessageAudioRecordFragment();
        instance.setArguments(args);
        return instance;

    }


    @Override
    public int getID() {
        return R.layout.fragment_message_audio_record;
    }

    @Override
    public void initUI(View view) {
        ButterKnife.bind(getActivity());
        presenter.start();
    }

    @Override
    public void setPresenter(MessageAudioRecordContract.Presenter presenter) {

        this.presenter = presenter;
    }

    @Override
    public void showNoInternet() {

    }

    @OnClick(R.id.recording)
    public void onRecording() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    10);
        } else {
            presenter.startRecording();

        }
    }

    @OnClick(R.id.stop_recording)
    public void onStopRecording() {
        presenter.stopRecording();
    }

    @Override
    public void showMessage(String msg) {

        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();

    }
}
