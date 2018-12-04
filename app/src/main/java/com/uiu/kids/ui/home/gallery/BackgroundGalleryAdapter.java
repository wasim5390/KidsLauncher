package com.uiu.kids.ui.home.gallery;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.uiu.kids.R;
import com.uiu.kids.util.Util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.logging.Handler;

public class BackgroundGalleryAdapter  extends RecyclerView.Adapter<BackgroundGalleryAdapter.ViewHolder>{


    private List<BackgroundGalleryActivity.ImageModel> items;
    private Context context;
    private OnItemClickListener clickListener;
    private int width=150;

    public BackgroundGalleryAdapter(Context context, List<BackgroundGalleryActivity.ImageModel> items, OnItemClickListener clickListener) {
        this.items = items;
        this.context = context;
        this.clickListener = clickListener;
        width = Util.getScreenWidth()/3;
    }

    @Override
    public BackgroundGalleryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_gallery_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final BackgroundGalleryAdapter.ViewHolder holder, int position) {

        holder.view.setOnClickListener(v -> clickListener.onItemClick(items.get(position).getName()));
        new android.os.Handler().post(() -> {

            Bitmap bitmap = getBitmapFromAsset(context,"bg_images/"+items.get(position).getName());
            float aspectRatio = bitmap.getWidth() / (float) bitmap.getHeight();
            int height = Math.round(width / aspectRatio);
            Bitmap resized = Bitmap.createScaledBitmap(bitmap,width,height,false);
            bitmap.recycle();
            holder.image.setImageBitmap(resized);
        });



    }

    private Bitmap getBitmapFromAsset(Context context, String strName) {
        AssetManager assetManager = context.getAssets();
        InputStream istr = null;
        try {
            istr = assetManager.open(strName);
            return BitmapFactory.decodeStream(istr);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public BackgroundGalleryActivity.ImageModel getItem(int position) {
        return items.get(position);
    }

    public void updateItems(List<BackgroundGalleryActivity.ImageModel> newItems) {
        items.clear();
        items.addAll(newItems);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        View view;
        public ViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            image = view.findViewById(R.id.item_img);
        }
    }

    public interface OnItemClickListener{
        void onItemClick(String imageName);
    }
}