package com.uiu.kids.ui.slides.apps;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.uiu.kids.R;
import com.uiu.kids.ui.home.apps.AppsEntity;


import java.util.List;

public class FavoriteAppsAdapter extends RecyclerView.Adapter<FavoriteAppsAdapter.ViewHolder> {

    private Context mContext;
    private FavoriteAppsAdapter.Callback mCallback;
    private List<AppsEntity> mSlideItems;
    private LayoutInflater inflater;

    public FavoriteAppsAdapter(Context context, List<AppsEntity> favItems, FavoriteAppsAdapter.Callback callback) {
        this.mCallback = callback;
        this.mContext = context;
        this.inflater = LayoutInflater.from(context);
        this.mSlideItems = favItems;
    }


    public void setSlideItems(List<AppsEntity> slideItems) {
        if(slideItems!=null) {
            this.mSlideItems.clear();
            this.mSlideItems.addAll(slideItems);
            notifyDataSetChanged();
        }

    }

    @NonNull
    @Override
    public FavoriteAppsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FavoriteAppsAdapter.ViewHolder((FavoriteAppsItemView) inflater.inflate(R.layout.app_slide_item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteAppsAdapter.ViewHolder holder, int position) {
        AppsEntity item = mSlideItems.get(position);
        ((FavoriteAppsItemView) holder.itemView).setSlideItem(item,item.getIcon(mContext.getApplicationContext()),mCallback);

    }


    @Override
    public int getItemCount() {
        return mSlideItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(FavoriteAppsItemView itemView) {
            super(itemView);
        }
    }

    public interface Callback {
        void onSlideItemClick(AppsEntity slideItem);
    }
}