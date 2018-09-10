package com.uiu.kids.ui.home;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;


import com.uiu.kids.R;

import java.util.ArrayList;
import java.util.List;

public class HomeSlideAdapter extends RecyclerView.Adapter<HomeSlideAdapter.ViewHolder> {

    private Context mContext;
    private Callback mCallback;
    private List<String> mSlideItems;
    private LayoutInflater inflater;

    public HomeSlideAdapter(Context context,Callback callback) {
        this.mCallback = callback;
        this.mContext = context;
        this.inflater = LayoutInflater.from(context);
        this.mSlideItems = new ArrayList<>();
    }


    public void setSlideItems(List<String> slideItems)
    {
        this.mSlideItems.clear();
        this.mSlideItems.addAll(slideItems);
        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public HomeSlideAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HomeSlideAdapter.ViewHolder((HomeItemView)inflater.inflate(R.layout.home_slide_item_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String item = mSlideItems.get(position);
        ((HomeItemView)holder.itemView).setSlideItem(item,mCallback);

    }


    @Override
    public int getItemCount() {
        return mSlideItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(HomeItemView itemView) {
            super(itemView);
        }
    }

    public interface Callback{
         void onSlideItemClick(String slideItem);
    }


}
