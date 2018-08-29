package com.wiser.kids.ui.message.MessageAudioRecord;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wiser.kids.BaseFragment;
import com.wiser.kids.Injection;
import com.wiser.kids.R;
import com.wiser.kids.model.SlideItem;
import com.wiser.kids.ui.message.MessageFragment;
import com.wiser.kids.ui.message.MessagePresenter;
import com.wiser.kids.util.PreferenceUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MessageAudioRecordFragment extends BaseFragment implements MessageAudioRecordContract.View {

    @BindView(R.id.recording)
    public ImageView recording;

    @BindView(R.id.stop_recording)
    public ImageView stopRecording;

    @BindView(R.id.next_recording)
    public ImageView next;

    @BindView(R.id.chronomet)
    public Chronometer chronomat;


    public MessageAudioRecordContract.Presenter presenter;
    private long pauseOffset;
    public MessageFragment messageFragment;
    public MessagePresenter messagePresenter;


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
        setListner();
    }
    public void setListner()
    {
        chronomat.setFormat("%s");
        chronomat.setBase(SystemClock.elapsedRealtime());
        chronomat.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                if ((SystemClock.elapsedRealtime() - chronometer.getBase()) >= 10000) {
                    //chronometer.setBase(SystemClock.elapsedRealtime()-pauseOffset);
                    Toast.makeText(getContext(), "Bing!", Toast.LENGTH_SHORT).show();
                }
            }
        });

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
            chronomat.setBase(SystemClock.elapsedRealtime()-pauseOffset);
            chronomat.start();
            stopRecording.setVisibility(View.VISIBLE);
            recording.setVisibility(View.GONE);
            presenter.startRecording();
        }
    }

    @OnClick(R.id.stop_recording)
    public void onStopRecording()
    {
        chronomat.stop();
        pauseOffset = SystemClock.elapsedRealtime() - chronomat.getBase();
        stopRecording.setVisibility(View.GONE);
        next.setVisibility(View.VISIBLE);
        presenter.stopRecording();
    }

    @OnClick(R.id.next_recording)
    public void goToFavPeople()
    {
        next.setVisibility(View.GONE);
        chronomat.setVisibility(View.GONE);

        messageFragment=null;
        messagePresenter=null;
        goToMessageFragment();

    }

    private void goToMessageFragment() {
        messageFragment = messageFragment != null ? messageFragment : MessageFragment.newInstance(3,presenter.getFilePath());
        messagePresenter = messagePresenter != null ? messagePresenter : new MessagePresenter(messageFragment, PreferenceUtil.getInstance(getActivity()), Injection.provideRepository(getActivity()));
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.msgframeLayout, messageFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void showMessage(String msg) {
        Toast.makeText(getContext(),msg,Toast.LENGTH_SHORT).show();
    }
}
