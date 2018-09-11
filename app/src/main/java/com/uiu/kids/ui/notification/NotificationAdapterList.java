package com.uiu.kids.ui.notification;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;


import com.uiu.kids.R;

import java.util.ArrayList;
import java.util.List;

public class NotificationAdapterList extends RecyclerView.Adapter<NotificationAdapterList.ViewHolder> {

    private Context mContext;
    private NotificationAdapterList.Callback mCallback;
    private List<NotificationEntity> mSlideItems;
    private LayoutInflater inflater;

    public NotificationAdapterList(Context context, NotificationAdapterList.Callback callback) {
        this.mCallback = callback;
        this.mContext = context;
        this.inflater = LayoutInflater.from(context);
        this.mSlideItems = new ArrayList<>();
    }


    public void setSlideItems(List<NotificationEntity> slideItems) {
        this.mSlideItems.clear();
        this.mSlideItems.addAll(slideItems);
        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public NotificationAdapterList.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NotificationAdapterList.ViewHolder((NotificationItemView) inflater.inflate(R.layout.notification_list_item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapterList.ViewHolder holder, int position) {
        NotificationEntity item = mSlideItems.get(position);
        ((NotificationItemView) holder.itemView).setSlideItem(item, mCallback);

    }


    @Override
    public int getItemCount() {
        return mSlideItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(NotificationItemView itemView) {
            super(itemView);
        }
    }

    public interface Callback {
        void onSlideItemClick(NotificationEntity slideItem);
    }
}
