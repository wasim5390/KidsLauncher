package com.uiu.kids.ui.message;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.uiu.kids.R;
import com.uiu.kids.ui.home.contact.ContactEntity;


import java.util.ArrayList;
import java.util.List;

public class MessageAdapterList extends RecyclerView.Adapter<MessageAdapterList.ViewHolder> {

    private Context mContext;
    private MessageAdapterList.Callback mCallback;
    private List<ContactEntity> mSlideItems;
    private LayoutInflater inflater;

    public MessageAdapterList(Context context, MessageAdapterList.Callback callback) {
        this.mCallback = callback;
        this.mContext = context;
        this.inflater = LayoutInflater.from(context);
        this.mSlideItems = new ArrayList<>();
    }


    public void setSlideItems(List<ContactEntity> slideItems) {
        this.mSlideItems.clear();
        this.mSlideItems.addAll(slideItems);
        notifyDataSetChanged();

    }

    public List<ContactEntity> getSelectedContact(){
        List<ContactEntity> selectedContact = new ArrayList<>();
        for(ContactEntity entity: mSlideItems){
            if(entity.isSelectedForSharing())
                selectedContact.add(entity);
        }
        return selectedContact;
    }

    @NonNull
    @Override
    public MessageAdapterList.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MessageAdapterList.ViewHolder((MessageItemView) inflater.inflate(R.layout.message_list_item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapterList.ViewHolder holder, int position) {
        ContactEntity item = mSlideItems.get(position);
        ((MessageItemView) holder.itemView).setSlideItem(item, mCallback);

    }


    @Override
    public int getItemCount() {
        return mSlideItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(MessageItemView itemView) {
            super(itemView);
        }
    }

    public interface Callback {
        void onSlideItemClick(ContactEntity slideItem, boolean isSelected);
    }
}