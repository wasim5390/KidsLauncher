package com.wiser.kids.ui.favorite.people;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.wiser.kids.R;
import com.wiser.kids.ui.home.contact.ContactEntity;

import java.util.ArrayList;
import java.util.List;

public class FavoritePeopleAdapter extends RecyclerView.Adapter<FavoritePeopleAdapter.ViewHolder> {

    private Context mContext;
    private FavoritePeopleAdapter.Callback mCallback;
    private List<ContactEntity> mSlideItems;
    private LayoutInflater inflater;

    public FavoritePeopleAdapter(Context context, FavoritePeopleAdapter.Callback callback) {
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

    @NonNull
    @Override
    public FavoritePeopleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FavoritePeopleAdapter.ViewHolder((FavoritePeopleItemView) inflater.inflate(R.layout.fav_people_item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritePeopleAdapter.ViewHolder holder, int position) {
        ContactEntity item = mSlideItems.get(position);
        ((FavoritePeopleItemView) holder.itemView).setSlideItem(item, mCallback);

    }


    @Override
    public int getItemCount() {
        return mSlideItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(FavoritePeopleItemView itemView) {
            super(itemView);
        }
    }

    public interface Callback {
        void onSlideItemClick(ContactEntity slideItem);
    }
}