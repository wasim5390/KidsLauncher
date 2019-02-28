package com.uiu.kids.ui.camera.editor;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.transition.ChangeBounds;
import android.support.transition.TransitionManager;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import com.uiu.kids.BaseFragment;
import com.uiu.kids.Constant;
import com.uiu.kids.R;
import com.uiu.kids.ui.camera.EmojiBSFragment;
import com.uiu.kids.ui.camera.StickerBSFragment;
import com.uiu.kids.ui.camera.filters.FilterListener;
import com.uiu.kids.ui.camera.filters.FilterViewAdapter;
import com.uiu.kids.ui.camera.tools.EditingToolsAdapter;
import com.uiu.kids.ui.camera.tools.ToolType;
import com.uiu.kids.ui.home.contact.ContactEntity;
import com.uiu.kids.ui.share.ShareActivity;
import com.uiu.kids.util.PermissionUtil;
import com.uiu.kids.util.Util;

import java.io.File;
import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import id.zelory.compressor.Compressor;
import ja.burhanrashid52.photoeditor.OnPhotoEditorListener;
import ja.burhanrashid52.photoeditor.PhotoEditor;
import ja.burhanrashid52.photoeditor.PhotoEditorView;
import ja.burhanrashid52.photoeditor.PhotoFilter;
import ja.burhanrashid52.photoeditor.ViewType;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class PhotoEditorFragment extends BaseFragment implements PhotoEditorContract.View ,OnPhotoEditorListener,
        PermissionUtil.PermissionCallback,
        View.OnClickListener,
        EmojiBSFragment.EmojiListener,
        StickerBSFragment.StickerListener, EditingToolsAdapter.OnItemSelected, FilterListener {

    private static final String TAG = PhotoEditorFragment.class.getSimpleName();
    private static final int CAMERA_REQUEST = 0x52;
    private static final int PICK_REQUEST = 0x53;
    private static final int SHARE_REQUEST = 0x54;

    @BindView(R.id.photoEditorView)
    public PhotoEditorView mPhotoEditorView;
    @BindView(R.id.txtCurrentTool)
    public TextView mTxtCurrentTool;
    @BindView(R.id.rvTools)
    public RecyclerView mRvTools;
    @BindView(R.id.rvFilterView)
    public RecyclerView mRvFilters;
    @BindView(R.id.rvContacts)
    public RecyclerView mRvContacts;
    @BindView(R.id.rootView)
    ConstraintLayout mRootView;

    private PhotoEditor mPhotoEditor;
    private EmojiBSFragment mEmojiBSFragment;
    private StickerBSFragment mStickerBSFragment;

    private ConstraintSet mConstraintSet = new ConstraintSet();
    private EditingToolsAdapter mEditingToolsAdapter ;
    private FilterViewAdapter mFilterViewAdapter;
    private PhotoEditorFavContactsAdapter mContactAdapter;

    private boolean mIsFilterVisible;
    String mCurrentPhotoPath;

    private boolean favContactLoaded=false;

    @BindView(R.id.progressBar)
    ContentLoadingProgressBar progressBar;

    private PhotoEditorContract.Presenter presenter;


    public static PhotoEditorFragment newInstance(String imagePath) {
        Bundle args = new Bundle();
        args.putString("FilePath",imagePath);
        PhotoEditorFragment photoEditorFragment = new PhotoEditorFragment();
        photoEditorFragment.setArguments(args);
        return photoEditorFragment;
    }


    @Override
    public int getID() {
        return R.layout.fragment_edit_image;
    }

    @Override
    public void initUI(View view) {
        mCurrentPhotoPath = getArguments().getString("FilePath");
        hideProgress();
        setAdapters();
      //  presenter.loadFavPeoples();
        mEmojiBSFragment = new EmojiBSFragment();
        mStickerBSFragment = new StickerBSFragment();
        mStickerBSFragment.setStickerListener(this);
        mEmojiBSFragment.setEmojiListener(this);

        LinearLayoutManager llmTools = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mRvTools.setLayoutManager(llmTools);
        mRvTools.setAdapter(mEditingToolsAdapter);


        LinearLayoutManager llmFilters = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mRvFilters.setLayoutManager(llmFilters);
        mRvFilters.setAdapter(mFilterViewAdapter);

      //  mRvContacts.setAdapter(mContactAdapter);

        mPhotoEditor = new PhotoEditor.Builder(getContext(), mPhotoEditorView)
                .setPinchTextScalable(true) // set flag to make text scalable when pinch
                .build(); // build photo editor sdk

        mPhotoEditor.setOnPhotoEditorListener(this);
        // dispatchTakePictureIntent();

        mPhotoEditor.clearAllViews();
        try {
            File file = new File(mCurrentPhotoPath);
            // Bitmap bitmap = MediaStore.Images.Media
            //         .getBitmap(getActivity().getContentResolver(), Uri.fromFile(file));
            Bitmap bitmap = new Compressor(getContext()).compressToBitmap(file);
            if (bitmap != null) {
                // int orientation = getOrientation(file.getPath());
                /// bitmap = rotateBitmap(bitmap,orientation);
                mPhotoEditorView.getSource().setImageBitmap(bitmap);

            }
        }catch (IOException e){
            onBackPressed();
            e.printStackTrace();
        }

    }

    private void setAdapters(){
        mEditingToolsAdapter = new EditingToolsAdapter(this);
        mFilterViewAdapter = new FilterViewAdapter(this);
       /* mContactAdapter = new PhotoEditorFavContactsAdapter(getContext(), slideItem -> {
            mContactAdapter.notifyDataSetChanged();
        });*/
    }


    @Override
    public void showProgress() {
        progressBar.show();
    }

    @Override
    public void hideProgress() {
        progressBar.hide();
    }

    @Override
    public void onEditTextChangeListener(final View rootView, String text, int colorCode) {
    }

    @Override
    public void onAddViewListener(ViewType viewType, int numberOfAddedViews) {
        Log.d(TAG, "onAddViewListener() called with: viewType = [" + viewType + "], numberOfAddedViews = [" + numberOfAddedViews + "]");
    }

    @Override
    public void onRemoveViewListener(int numberOfAddedViews) {
        Log.d(TAG, "onRemoveViewListener() called with: numberOfAddedViews = [" + numberOfAddedViews + "]");
    }

    @Override
    public void onStartViewChangeListener(ViewType viewType) {
        Log.d(TAG, "onStartViewChangeListener() called with: viewType = [" + viewType + "]");
    }

    @Override
    public void onStopViewChangeListener(ViewType viewType) {
        Log.d(TAG, "onStopViewChangeListener() called with: viewType = [" + viewType + "]");
    }

    @OnClick({ R.id.imgShare, R.id.btnSend, R.id.imgClose})
    public void onClick(View view) {
        switch (view.getId()) {


            case R.id.imgShare:
                saveImage();
              //  View contactView = getView().findViewById(R.id.viewShareContact);
              //  contactView.setVisibility(contactView.getVisibility()==View.VISIBLE?View.GONE:View.VISIBLE);
                break;

            case R.id.btnSend:
                saveImage();
                break;

            case R.id.imgClose:
                onBackPressed();
                break;


        }
    }

    @SuppressLint("MissingPermission")
    private void saveImage() {

        if (PermissionUtil.isPermissionGranted(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            showProgress();
            File parent  = new File(Environment.getExternalStorageDirectory().toString()+"/Kids Launcher");
            parent.mkdir();
            File file = new File(parent,
                    File.separator + ""
                            + System.currentTimeMillis() + ".png");
            try {
                file.createNewFile();
                mPhotoEditor.saveAsFile(file.getAbsolutePath(), new PhotoEditor.OnSaveListener() {
                    @Override
                    public void onSuccess(@NonNull String imagePath) {
                     //   hideProgress();
                      //  Toast.makeText(getContext(), "Image Saved Successfully", Toast.LENGTH_SHORT).show();
                        File image = new File(imagePath);
                        mPhotoEditorView.getSource().setImageURI(Uri.fromFile(image));



                        Util.saveOnGallery(getActivity(),image);



                        Intent intent = new Intent();
                        intent.setClass(getActivity(), ShareActivity.class);
                        intent.putExtra(Constant.RECORDED_FILE_PATH,imagePath);
                        intent.putExtra(Constant.RECORDED_FILE_TYPE,MEDIA_IMAGE);
                        startActivityForResult(intent,SHARE_REQUEST);
                        removeOriginalFile(mCurrentPhotoPath);
                      //  presenter.sharePicToFav(contacts,MEDIA_IMAGE,image);
                    }

                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        hideProgress();
                        Toast.makeText(getContext(), "Failed to save Image", Toast.LENGTH_SHORT).show();
                        removeOriginalFile(mCurrentPhotoPath);
                    }
                });
            } catch (IOException e) {
                removeOriginalFile(mCurrentPhotoPath);
                e.printStackTrace();
                hideProgress();
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        else
            PermissionUtil.requestPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE, this);
    }

    private void removeOriginalFile(String path){
        File file = new File(path);
        Log.i("OriginalFile:",file.getAbsolutePath());
        boolean deletedOriginal = file.getAbsoluteFile().delete();
        Log.i("OriginalFileDeleted:",String.valueOf(deletedOriginal));
    }

    @Override
    public void onEmojiClick(String emojiUnicode) {
        mPhotoEditor.addEmoji(emojiUnicode);
        mTxtCurrentTool.setText(R.string.label_emoji);

    }

    @Override
    public void onStickerClick(Bitmap bitmap) {
        mPhotoEditor.addImage(bitmap);
        mTxtCurrentTool.setText(R.string.label_sticker);
    }
    @Override
    public void onFilterSelected(PhotoFilter photoFilter) {
        mPhotoEditor.setFilterEffect(photoFilter);
    }

    @Override
    public void onToolSelected(ToolType toolType) {
        switch (toolType) {

            case FILTER:
                mTxtCurrentTool.setText(R.string.label_filter);
                showFilter(true);
                break;
            case EMOJI:
                mEmojiBSFragment.show(getChildFragmentManager(), mEmojiBSFragment.getTag());
                break;
            case STICKER:
                mStickerBSFragment.show(getChildFragmentManager(), mStickerBSFragment.getTag());
                break;
            case GALLERY:

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_REQUEST);
                break;
        }
    }

    public void onBackPressed() {
        if (mIsFilterVisible) {
            showFilter(false);
            mTxtCurrentTool.setText(R.string.app_name);
        } else if (!mPhotoEditor.isCacheEmpty()) {
            showSaveDialog();
        } else {
            getActivity().onBackPressed();
        }
    }

    void showFilter(boolean isVisible) {
        mIsFilterVisible = isVisible;
        mConstraintSet.clone(mRootView);

        if (isVisible) {
            mConstraintSet.clear(mRvFilters.getId(), ConstraintSet.START);
            mConstraintSet.connect(mRvFilters.getId(), ConstraintSet.START,
                    ConstraintSet.PARENT_ID, ConstraintSet.START);
            mConstraintSet.connect(mRvFilters.getId(), ConstraintSet.END,
                    ConstraintSet.PARENT_ID, ConstraintSet.END);
        } else {
            mConstraintSet.connect(mRvFilters.getId(), ConstraintSet.START,
                    ConstraintSet.PARENT_ID, ConstraintSet.END);
            mConstraintSet.clear(mRvFilters.getId(), ConstraintSet.END);
        }

        ChangeBounds changeBounds = new ChangeBounds();
        changeBounds.setDuration(350);
        changeBounds.setInterpolator(new AnticipateOvershootInterpolator(1.0f));
        TransitionManager.beginDelayedTransition(mRootView, changeBounds);

        mConstraintSet.applyTo(mRootView);
    }



    public void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                // Save a file: path for use with ACTION_VIEW intents
                mCurrentPhotoPath = photoFile.getAbsolutePath();
                Uri photoURI = FileProvider.getUriForFile(getActivity(),
                        "com.uiu.kids.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST);
            }
        }
    }

    private void showSaveDialog() {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            builder = new AlertDialog.Builder(getActivity(),R.style.DialogTheme);
        else
            builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Are you sure to exit without saving \nimage ?");
        builder.setPositiveButton("Save", (dialog, which) -> saveImage());
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builder.setNeutralButton("Discard", (dialog, which) -> getActivity().finish());
        builder.create().show();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CAMERA_REQUEST:
                    mPhotoEditor.clearAllViews();
                    try {
                        File file = new File(mCurrentPhotoPath);
                        // Bitmap bitmap = MediaStore.Images.Media
                        //         .getBitmap(getActivity().getContentResolver(), Uri.fromFile(file));
                        Bitmap bitmap = new Compressor(getContext()).compressToBitmap(file);
                        if (bitmap != null) {
                            // int orientation = getOrientation(file.getPath());
                            /// bitmap = rotateBitmap(bitmap,orientation);
                            mPhotoEditorView.getSource().setImageBitmap(bitmap);

                        }
                    }catch (IOException e){
                        onBackPressed();
                        e.printStackTrace();
                    }
                    break;
                case PICK_REQUEST:
                    try {
                        mPhotoEditor.clearAllViews();
                        Uri uri = data.getData();
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                        mPhotoEditorView.getSource().setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case SHARE_REQUEST:
                    getActivity().setResult(RESULT_OK);
                    getActivity().finish();
                    break;
            }
        }else{
            if(requestCode!=PICK_REQUEST) {
                getActivity().setResult(RESULT_CANCELED);
                getActivity().finish();
            }
        }
    }


    @Override
    public void onPermissionsGranted(String permission) {
        saveImage();
    }

    @Override
    public void onPermissionsGranted() {

    }

    @Override
    public void onPermissionDenied() {
        Toast.makeText(mBaseActivity, "Need permission to save image", Toast.LENGTH_SHORT).show();
        mBaseActivity.openSettings();
    }

    @Override
    public void showMessage(String message) {
        favContactLoaded=true;
        Toast.makeText(mBaseActivity, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPicShared() {

    }

    @Override
    public void onFavPeopleLoaded(List<ContactEntity> contactEntities) {
      //  favContactLoaded = true;
      //  mContactAdapter.setSlideItems(contactEntities);
    }

    @Override
    public void setPresenter(PhotoEditorContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showNoInternet() {

    }
}
