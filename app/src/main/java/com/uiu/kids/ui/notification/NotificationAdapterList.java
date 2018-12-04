package com.uiu.kids.ui.notification;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.uiu.kids.R;
import com.uiu.kids.model.Data;
import com.uiu.kids.model.NotificationsItem;

import java.util.ArrayList;
import java.util.List;

public class NotificationAdapterList extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private NotificationAdapterList.Callback mCallback;
    private List<NotificationsItem> mSlideItems;
    private LayoutInflater inflater;

    public NotificationAdapterList(Context context, NotificationAdapterList.Callback callback) {
        this.mCallback = callback;
        this.mContext = context;
        this.inflater = LayoutInflater.from(context);
        this.mSlideItems = new ArrayList<>();
    }


    public void setSlideItems(List<NotificationsItem> slideItems) {
        this.mSlideItems.clear();
        this.mSlideItems.addAll(slideItems);
        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = (NotificationItemView) inflater.inflate(R.layout.notification_list_item_view, parent, false);

        switch (viewType) {
            case 0:
                return new ViewHolderApproval(view);
            case 2:
                return new ViewHolderReminder(view);

        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        NotificationsItem item = mSlideItems.get(position);
        switch (holder.getItemViewType()){
            case 0:
                ((NotificationItemView) holder.itemView).setSlideItem(item, mCallback);
                break;
            case 1:
                break;
        }




    }

    @Override
    public int getItemViewType(int position) {
        // Just as an example, return 0 or 2 depending on position
        // Note that unlike in ListView adapters, types don't have to be contiguous
        return position % 2 * 2;
    }

    @Override
    public int getItemCount() {
        return mSlideItems.size();
    }


    public class ViewHolderApproval extends RecyclerView.ViewHolder {

        public ViewHolderApproval(View itemView) {
            super(itemView);
        }
    }

    public class ViewHolderReminder extends RecyclerView.ViewHolder {

        public ViewHolderReminder(View itemView) {
            super(itemView);
        }
    }


    public interface Callback {
        void onSlideItemClick(NotificationsItem slideItem);
    }
}
