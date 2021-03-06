package com.uiu.kids.ui.slides.links;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.uiu.kids.R;
import com.uiu.kids.model.LinksEntity;


import java.util.List;

public class FavoriteLinksAdapter extends RecyclerView.Adapter<FavoriteLinksAdapter.ViewHolder> {

    private Context mContext;
    private FavoriteLinksAdapter.Callback mCallback;
    private List<LinksEntity> mSlideItems;
    private LayoutInflater inflater;

    public FavoriteLinksAdapter(Context context, List<LinksEntity> favItems, FavoriteLinksAdapter.Callback callback) {
        this.mCallback = callback;
        this.mContext = context;
        this.inflater = LayoutInflater.from(mContext);
        this.mSlideItems = favItems;
    }


    public void setSlideItems(List<LinksEntity> slideItems) {
        if (slideItems != null) {
            this.mSlideItems.clear();
            this.mSlideItems.addAll(slideItems);
            notifyDataSetChanged();
        }

    }

    @NonNull
    @Override
    public FavoriteLinksAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        FavoriteLinksItemView view=(FavoriteLinksItemView) inflater.inflate(R.layout.link_slide_item_view, parent, false);
        return new FavoriteLinksAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteLinksAdapter.ViewHolder holder, int position) {

        LinksEntity item = mSlideItems.get(position);
        ((FavoriteLinksItemView) holder.itemView).setSlideItem(item,mCallback);
    }




    @Override
    public int getItemCount() {
        return mSlideItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(FavoriteLinksItemView itemView) {
            super(itemView);
        }
    }

    public interface Callback {
        void onSlideItemClick(LinksEntity slideItem);
    }

}