package com.wiser.kids.ui.message.MessageVideoRecording;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import com.wiser.kids.BaseFragment;
import com.wiser.kids.Injection;
import com.wiser.kids.R;
import com.wiser.kids.ui.message.MessageFragment;
import com.wiser.kids.ui.message.MessagePresenter;
import com.wiser.kids.util.PreferenceUtil;
import com.wiser.kids.util.Util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MessageVideoRecordingFragment extends BaseFragment implements MessageVideoRecordingContract.View {

    public MessageVideoRecordingContract.Presenter presenter;
    private int PICK_VIDEO_REQUEST = 0x786;
    public MessagePresenter messagePresenter;
    public MessageFragment messageFragment;


    public static MessageVideoRecordingFragment newInstance() {
        Bundle args = new Bundle();
        MessageVideoRecordingFragment instance = new MessageVideoRecordingFragment();
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
        this.presenter = presenter;
    }

    @Override
    public void showNoInternet() {

    }

    @Override
    public void loadCamera() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    10);
        }
        else {
            Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            takeVideoIntent.putExtra("android.intent.extra.durationLimit", 15);
            takeVideoIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
            takeVideoIntent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, 5 * 1048 * 1048);
            if (takeVideoIntent.resolveActivity(getContext().getPackageManager()) != null) {
                startActivityForResult(takeVideoIntent, PICK_VIDEO_REQUEST);
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PICK_VIDEO_REQUEST) {
            Uri video_uri = data.getData();
            Toast.makeText(getContext(), String.valueOf(video_uri), Toast.LENGTH_SHORT).show();
            if (video_uri != null) {
                presenter.videoInFile(getPath(video_uri));
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
    public void onMediaFileShare(String filePath) {
        messagePresenter=null;
        messagePresenter=null;
        goToMessageFragment(filePath);


    }
    private void goToMessageFragment(String filePath) {
//        messageFragment = messageFragment != null ? messageFragment : MessageFragment.newInstance(MEDIA_VIDEO,filePath);
//        messagePresenter = messagePresenter != null ? messagePresenter : new MessagePresenter(messageFragment, PreferenceUtil.getInstance(getActivity()), Injection.provideRepository(getActivity()));
//        FragmentManager fragmentManager = getChildFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.msgframeLayout, messageFragment,null);
//        fragmentTransaction.commit();
    }


}
