package com.uiu.kids.ui.home.gallery;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.uiu.kids.BaseActivity;
import com.uiu.kids.Constant;
import com.uiu.kids.R;
import com.uiu.kids.util.PreferenceUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BackgroundGalleryActivity extends BaseActivity implements BackgroundGalleryAdapter.OnItemClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.gallery_list)
    RecyclerView mRecyclerView;
    private BackgroundGalleryAdapter mAdapter;
    private List<ImageModel> imageModelList;

    @Override
    public int getID() {
        return R.layout.activity_gallery;
    }

    @Override
    public void created(Bundle savedInstanceState) {
        ButterKnife.bind(this);

        setToolBar(toolbar, "Gallery", true);

        imageModelList = new ArrayList<>();
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mRecyclerView.setHasFixedSize(true); // Helps improve performance
        mAdapter = new BackgroundGalleryAdapter(this,new ArrayList<>(),this);
        mRecyclerView.setAdapter(mAdapter);

        String[] images;
        try {
            images = getAssets().list("bg_images");
            ArrayList<String> listImages = new ArrayList<>(Arrays.asList(images));
            for(String image:listImages){

               imageModelList.add(new ImageModel(image));
            }
            mAdapter.updateItems(imageModelList);
        } catch (IOException e) {
            e.printStackTrace();
        }



    }


    @Override
    public void onItemClick(String imageName) {
        PreferenceUtil.getInstance(this).savePreference(Constant.KEY_SELECTED_BG,imageName);

        Intent result = new Intent();
        result.putExtra(Constant.KEY_SELECTED_BG,imageName);
        setResult(RESULT_OK,result);
        finish();
    }

    @OnClick(R.id.header_btn_left)
    public void onBackClick(){
        onBackPressed();
    }




    public class ImageModel {

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getName() {

            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        String name, path;

        public ImageModel(String name) {
            this.name = name;
        }

        public ImageModel() {
        }
        // Getters & Setters here

    }
}
