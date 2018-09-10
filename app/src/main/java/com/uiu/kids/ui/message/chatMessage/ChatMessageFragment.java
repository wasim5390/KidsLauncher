package com.uiu.kids.ui.message.chatMessage;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.devlomi.record_view.OnRecordListener;
import com.devlomi.record_view.RecordButton;
import com.devlomi.record_view.RecordView;
import com.squareup.picasso.Picasso;
import com.uiu.kids.BaseFragment;
import com.uiu.kids.R;
import com.uiu.kids.ui.home.contact.ContactEntity;


import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;


public class ChatMessageFragment extends BaseFragment implements ChatMessageContract.View, ChatMessageAdapterList.Callback {


    @BindView(R.id.header_img)
    public CircleImageView headerImg;

    @BindView(R.id.chat_Name)
    public TextView HeaderName;

    @BindView(R.id.rvChat)
    public RecyclerView recyclerView;


    @BindView(R.id.record_view)
    RecordView recordView;

    @BindView(R.id.audio_record_button)
    public RecordButton recordButton;

    @BindView(R.id.message_video)
    public ImageButton btnVideo;

    private int PICK_VIDEO_REQUEST = 0x786;
    public ChatMessageContract.Presenter presenter;
    public ContactEntity item;
    public static MediaPlayer mp;
    public ChatMessageAdapterList adapter;

    @Override
    public int getID() {
        return R.layout.fragment_chat_message;
    }

    public static ChatMessageFragment newInstance(ContactEntity item) {
        Bundle args = new Bundle();
        ChatMessageFragment instance = new ChatMessageFragment();
        args.putSerializable("item", item);
        instance.setArguments(args);
        return instance;
    }

    @Override
    public void initUI(View view) {
        ButterKnife.bind(getActivity());
        recordButton.setRecordView(recordView);
        addAudioListner();
        mp=new MediaPlayer();
        this.item = ((ContactEntity) getArguments().getSerializable("item"));
        presenter.start();
        setAdapter();
        HeaderName.setText(item.getName());
        Picasso.with(getContext()).load(item.getPhotoUri()).placeholder(item.getName() != null ? RES_AVATAR : RES_ADD_NEW).into(headerImg);
        presenter.getAllMessage(item.getId());

    }

    private void addAudioListner() {
        recordView.setOnRecordListener(new OnRecordListener() {
            @Override
            public void onStart() {
                btnVideo.setVisibility(View.GONE);
                presenter.startRecording();
                Log.d("RecordView", "onStart");
            }

            @Override
            public void onCancel() {
                //On Swipe To Cancel
                presenter.stopRecording();
                presenter.deleteAudioFile();
                btnVideo.setVisibility(View.VISIBLE);
                Log.d("RecordView", "onCancel");

            }

            @Override
            public void onFinish(long recordTime) {
                //Stop Recording..
                btnVideo.setVisibility(View.VISIBLE);
                presenter.stopRecording();
                presenter.shareAudioMedia();
                Log.d("RecordView", "onFinish");

                Log.d("RecordTime", String.valueOf(recordTime));
            }

            @Override
            public void onLessThanSecond() {
                btnVideo.setVisibility(View.VISIBLE);
                presenter.stopRecording();
                presenter.deleteAudioFile();
                presenter.shareAudioMedia();
                //When the record time is less than One Second
                Log.d("RecordView", "onLessThanSecond");
            }
        });
    }

    private void setAdapter() {

        adapter = new ChatMessageAdapterList(getContext(), presenter.getUserId(), this,mp);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void loadMessageList(List<ChatMessageEntity> list) {
        adapter.setSlideItems(list);
    }


    @Override
    public void setPresenter(ChatMessageContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showNoInternet() {

    }

    @OnClick(R.id.message_video)
    public void btnVideoClicked() {
        loadCamera();
    }

    private void loadCamera() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    10);
        } else {
            Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            takeVideoIntent.putExtra("android.intent.extra.durationLimit", 10);
            takeVideoIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
            takeVideoIntent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, 5 * 1048 * 1048);
            if (takeVideoIntent.resolveActivity(getContext().getPackageManager()) != null) {
                startActivityForResult(takeVideoIntent, PICK_VIDEO_REQUEST);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PICK_VIDEO_REQUEST) {
            if (data != null) {
                Uri video_uri = data.getData();
                Toast.makeText(getContext(), String.valueOf(video_uri), Toast.LENGTH_SHORT).show();
                if (video_uri != null) {
                    presenter.videoInFile(getPath(video_uri));
                }
            }
        }
    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContext().getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String s = cursor.getString(column_index);
        cursor.close();
        Log.e("Video path", s);
        return s;
    }

    public boolean deleteFile(final File file) {
        final String where = MediaStore.MediaColumns.DATA + "=?";
        final String[] selectionArgs = new String[]{
                file.getAbsolutePath()
        };
        final ContentResolver contentResolver = getContext().getContentResolver();
        final Uri filesUri = MediaStore.Files.getContentUri("external");

        contentResolver.delete(filesUri, where, selectionArgs);

        if (file.exists()) {

            contentResolver.delete(filesUri, where, selectionArgs);
        }
        return !file.exists();
    }

    @Override
    public void showMessage(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onMediaFileShare(String filePath, int type) {
        File file = new File(filePath);
        presenter.shareFileToContact(file, type);
    }

    @Override
    public void onSlideItemClick(int position) {
        adapter.resetLastItem(position);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mp.release();
    }

    @Override
    public void onPause()
    {
        super.onPause();

    }
}
