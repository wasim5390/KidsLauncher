package com.uiu.kids.ui.slides.reminder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;


import com.uiu.kids.R;

import java.util.List;

public class ReminderAdapterList extends RecyclerView.Adapter<ReminderAdapterList.ViewHolder> {

    private Context mContext;
    private ReminderAdapterList.Callback mCallback;
    private List<ReminderEntity> mSlideItems;
    private LayoutInflater inflater;

    public ReminderAdapterList(Context context, List<ReminderEntity> favItems, ReminderAdapterList.Callback callback) {
        this.mCallback = callback;
        this.mContext = context;
        this.inflater = LayoutInflater.from(mContext);
        this.mSlideItems = favItems;
    }


    public void setSlideItems(List<ReminderEntity> slideItems) {
        if (slideItems != null) {
            this.mSlideItems.clear();
            this.mSlideItems.addAll(slideItems);
            notifyDataSetChanged();
        }

    }

    @NonNull
    @Override
    public ReminderAdapterList.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ReminderItemView view=(ReminderItemView) inflater.inflate(R.layout.reminder_item_view, parent, false);
        return new ReminderAdapterList.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReminderAdapterList.ViewHolder holder, int position) {

        ReminderEntity item = mSlideItems.get(position);
        ((ReminderItemView) holder.itemView).setSlideItem(item,mCallback);
        ((ReminderItemView) holder.itemView).adjustView(getItemCount());
    }




    @Override
    public int getItemCount() {
        return mSlideItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(ReminderItemView itemView) {
            super(itemView);
        }
    }

    public interface Callback {
        void onSlideItemClick(ReminderEntity slideItem);
    }

}

