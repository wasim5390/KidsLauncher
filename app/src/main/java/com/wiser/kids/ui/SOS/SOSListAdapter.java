package com.wiser.kids.ui.SOS;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.wiser.kids.R;
import com.wiser.kids.ui.favorite.people.FavoritePeopleAdapter;
import com.wiser.kids.ui.favorite.people.FavoritePeopleItemView;
import com.wiser.kids.ui.home.contact.ContactEntity;

import java.util.ArrayList;
import java.util.List;

public class SOSListAdapter extends RecyclerView.Adapter<SOSListAdapter.ViewHolder> {

    private Context mContext;
    private SOSListAdapter.Callback mCallback;
    private List<ContactEntity> mSlideItems;
    private LayoutInflater inflater;


    public SOSListAdapter(Context context, SOSListAdapter.Callback callback) {
        this.mCallback = callback;
        this.mContext = context;
        this.inflater = LayoutInflater.from(mContext);
        this.mSlideItems = new ArrayList<>();
        }




    public void setSlideItems(List<ContactEntity> slideItems) {
        this.mSlideItems.clear();
        this.mSlideItems.addAll(slideItems);
        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public SOSListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SOSListAdapter.ViewHolder((SOSItemView) inflater.inflate(R.layout.sos_list_item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SOSListAdapter.ViewHolder holder, int position) {

        ContactEntity item = mSlideItems.get(position);
        ((SOSItemView) holder.itemView).setSlideItem(item, mCallback);

    }




    @Override
    public int getItemCount() {
        return mSlideItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(SOSItemView itemView) {
            super(itemView);
        }
    }

    public interface Callback {
        void onSlideItemClick(ContactEntity slideItem);
    }
}
