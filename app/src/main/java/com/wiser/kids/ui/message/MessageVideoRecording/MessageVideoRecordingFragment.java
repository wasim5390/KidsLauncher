package com.wiser.kids.ui.message.MessageVideoRecording;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.wiser.kids.BaseFragment;
import com.wiser.kids.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class MessageVideoRecordingFragment extends BaseFragment implements MessageVideoRecordingContract.View {

    public MessageVideoRecordingContract.Presenter presenter;
    private int PICK_VIDEO_REQUEST=0x786;


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
            File oldFile = new File(getPath(video_uri));

            String filepath = Environment.getExternalStorageDirectory().getPath()+"/KidsLauncher/Video";
            File desFile = new File(filepath);

            if(!desFile.exists()){
                desFile.mkdirs();


            }

            try {
                desFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            copyFileOrDirectory(oldFile.getAbsolutePath(),desFile.getAbsolutePath());

        }
    }
    public static void copyFileOrDirectory(String srcDir, String dstDir) {

        try {
            File src = new File(srcDir);
            File dst = new File(dstDir, src.getName());

            if (src.isDirectory()) {

                String files[] = src.list();
                int filesLength = files.length;
                for (int i = 0; i < filesLength; i++) {
                    String src1 = (new File(src, files[i]).getPath());
                    String dst1 = dst.getPath();
                    copyFileOrDirectory(src1, dst1);

                }
            } else {
                copyFile(src, dst);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void copyFile(File sourceFile, File destFile) throws IOException {
        if (!destFile.getParentFile().exists())
            destFile.getParentFile().mkdirs();

        if (!destFile.exists()) {
            destFile.createNewFile();
        }

        FileChannel source = null;
        FileChannel destination = null;

        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());
        } finally {
            if (source != null) {
                source.close();
            }
            if (destination != null) {
                destination.close();
            }
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
}
