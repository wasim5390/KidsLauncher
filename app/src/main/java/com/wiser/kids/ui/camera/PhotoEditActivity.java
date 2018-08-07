package com.wiser.kids.ui.camera;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.transition.ChangeBounds;
import android.support.transition.TransitionManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wiser.kids.BaseActivity;
import com.wiser.kids.R;
import com.wiser.kids.ui.camera.filters.FilterListener;
import com.wiser.kids.ui.camera.filters.FilterViewAdapter;
import com.wiser.kids.ui.camera.tools.EditingToolsAdapter;
import com.wiser.kids.ui.camera.tools.ToolType;
import com.wiser.kids.util.PermissionUtil;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import ja.burhanrashid52.photoeditor.OnPhotoEditorListener;
import ja.burhanrashid52.photoeditor.PhotoEditor;
import ja.burhanrashid52.photoeditor.PhotoEditorView;
import ja.burhanrashid52.photoeditor.ViewType;
import ja.burhanrashid52.photoeditor.PhotoFilter;

public class PhotoEditActivity extends BaseActivity  {

    private static final String TAG = PhotoEditActivity.class.getSimpleName();

    PhotoEditFragment photoEditFragment;
    @Override
    public int getID() {
        makeFullScreen();
        return R.layout.activity_photo_edit;
    }

    @Override
    public void created(Bundle savedInstanceState) {
        photoEditFragment = photoEditFragment != null ? photoEditFragment : PhotoEditFragment.newInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, photoEditFragment);
        fragmentTransaction.commit();

        //Set Image Dynamically
        // mPhotoEditorView.getSource().setImageResource(R.drawable.color_palette);
    }


}
