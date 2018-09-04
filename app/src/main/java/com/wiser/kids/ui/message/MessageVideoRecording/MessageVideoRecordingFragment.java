package com.wiser.kids.ui.message.MessageVideoRecording;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import com.wiser.kids.BaseFragment;
import com.wiser.kids.R;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MessageVideoRecordingFragment extends BaseFragment implements MessageVideoRecordingContract.View {

    public MessageVideoRecordingContract.Presenter presenter;
    private int PICK_VIDEO_REQUEST=0x786;

    @BindView(R.id.videoView)
    VideoView videoView;

    @BindView(R.id.videoNext)
    ImageView next;

    public static MessageVideoRecordingFragment newInstance()
    {
        Bundle args=new Bundle();
        MessageVideoRecordingFragment instance=new MessageVideoRecordingFragment();
        instance.setArguments(args);
        return instance;
    }

    @Override
    public int getID() {
        return R.layout.fragment_message_video_recording;
    }

    @Override
    public void initUI(View view) {

        ButterKnife.bind(getActivity());
        loadCamera();
    }

    @Override
    public void setPresenter(MessageVideoRecordingContract.Presenter presenter) {
        this.presenter=presenter;
    }

    @Override
    public void showNoInternet() {

    }

    @Override
    public void loadCamera() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        takeVideoIntent.putExtra("android.intent.extra.durationLimit", 10);
        takeVideoIntent.putExtra("EXTRA_VIDEO_QUALITY", 0);
        if (takeVideoIntent.resolveActivity(getContext().getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, PICK_VIDEO_REQUEST);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode==PICK_VIDEO_REQUEST)
        {
           Uri video_uri=data.getData();
            Toast.makeText(getContext(),String.valueOf(video_uri),Toast.LENGTH_SHORT).show();
           videoView.setVideoURI(video_uri);
            File srcFile = new File(getPath(video_uri));
            presenter.videoInFile(srcFile);
            srcFile.delete();
        }
    }
    public String getPath(Uri uri)
    {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContext().getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int column_index =             cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String s=cursor.getString(column_index);
        cursor.close();
        return s;
    }

    @OnClick(R.id.videoView)
    public void next()
    {

    }


}
