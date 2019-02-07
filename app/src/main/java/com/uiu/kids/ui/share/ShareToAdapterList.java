package com.uiu.kids.ui.share;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.uiu.kids.R;
import com.uiu.kids.ui.home.contact.ContactEntity;


import java.util.ArrayList;
import java.util.List;

public class ShareToAdapterList extends RecyclerView.Adapter<ShareToAdapterList.ViewHolder> {

    private Context mContext;
    private ShareToAdapterList.Callback mCallback;
    private List<ContactEntity> mSlideItems;
    private LayoutInflater inflater;

    public ShareToAdapterList(Context context, ShareToAdapterList.Callback callback) {
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
    public ShareToAdapterList.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ShareToAdapterList.ViewHolder((ShareToUserItemView) inflater.inflate(R.layout.message_list_item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ShareToAdapterList.ViewHolder holder, int position) {
        ContactEntity item = mSlideItems.get(position);
        ((ShareToUserItemView) holder.itemView).setSlideItem(item, mCallback);

    }


    @Override
    public int getItemCount() {
        return mSlideItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(ShareToUserItemView itemView) {
            super(itemView);
        }
    }

    public interface Callback {
        void onSlideItemClick(ContactEntity slideItem, boolean isSelected);
    }
}