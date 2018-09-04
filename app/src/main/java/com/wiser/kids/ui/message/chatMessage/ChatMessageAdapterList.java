package com.wiser.kids.ui.message.chatMessage;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wiser.kids.R;
import com.wiser.kids.ui.home.contact.ContactEntity;
import com.wiser.kids.ui.message.MessageAdapterList;
import com.wiser.kids.ui.message.MessageItemView;

import java.util.ArrayList;
import java.util.List;

public class ChatMessageAdapterList extends RecyclerView.Adapter<ChatMessageAdapterList.ViewHolder> {

    private Context mContext;
    private ChatMessageAdapterList.Callback mCallback;
    private List<ChatMessageEntity> mSlideItems;
    private LayoutInflater inflater;
    public String userId;
    public final int ITEM_TYPE_SENT=0;
    public final int ITEM_TYPE_RECEIVED=1;


    public ChatMessageAdapterList(Context context,String userid ,ChatMessageAdapterList.Callback callback) {
        this.mCallback = callback;
        this.mContext = context;
        this.userId=userid;
        this.inflater = LayoutInflater.from(context);
        this.mSlideItems = new ArrayList<>();
    }

    public void setSlideItems(List<ChatMessageEntity> slideItems) {
        this.mSlideItems.clear();
        this.mSlideItems.addAll(slideItems);
        notifyDataSetChanged();

    }

    @Override
    public int getItemViewType(int position) {
        if (mSlideItems.get(position).senderId.equalsIgnoreCase(userId)) {
            return ITEM_TYPE_SENT;
        } else {
            return ITEM_TYPE_RECEIVED;
        }
    }


    @NonNull
    @Override
    public ChatMessageAdapterList.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ChatMessageAdapterList.ViewHolder view=null;
        if(viewType==ITEM_TYPE_SENT) {
            view = new ChatMessageAdapterList.ViewHolder((ChatMessageItemView) inflater.inflate(R.layout.send_msg_row, parent, false));
        }
        else if(viewType==ITEM_TYPE_RECEIVED)
        {
            view= new ChatMessageAdapterList.ViewHolder((ChatMessageItemView) inflater.inflate(R.layout.recieve_msg_row, parent, false));
        }
            return view;
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
