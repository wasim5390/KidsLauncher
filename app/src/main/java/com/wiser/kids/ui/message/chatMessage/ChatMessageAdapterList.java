package com.wiser.kids.ui.message.chatMessage;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.wiser.kids.R;
import com.wiser.kids.ui.home.contact.ContactEntity;
import com.wiser.kids.ui.message.MessageAdapterList;
import com.wiser.kids.ui.message.MessageItemView;

import java.util.ArrayList;
import java.util.List;

public class ChatMessageAdapterList extends RecyclerView.Adapter<ChatMessageAdapterList.ViewHolder> {

    private Context mContext;
    private MessageAdapterList.Callback mCallback;
    private List<ChatMessageEntity> mSlideItems;
    private LayoutInflater inflater;

    public ChatMessageAdapterList(Context context, MessageAdapterList.Callback callback,String userid,String conversationistid) {
        this.mCallback = callback;
        this.mContext = context;
        this.inflater = LayoutInflater.from(context);
        this.mSlideItems = new ArrayList<>();
    }


    public void setSlideItems(List<ChatMessageEntity> slideItems) {
        this.mSlideItems.clear();
        this.mSlideItems.addAll(slideItems);
        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public ChatMessageAdapterList.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChatMessageAdapterList.ViewHolder((ChatMessageItemView) inflater.inflate(R.layout.message_list_item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ChatMessageAdapterList.ViewHolder holder, int position) {
        ChatMessageEntity item = mSlideItems.get(position);
        ((ChatMessageItemView) holder.itemView).setSlideItem(mContext,item, mCallback);
        }


    @Override
    public int getItemCount() {
        return mSlideItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(ChatMessageItemView itemView) {
            super(itemView);
        }
    }

    public interface Callback {
        void onSlideItemClick(ChatMessageEntity slideItem,boolean isSelected);
    }
}
