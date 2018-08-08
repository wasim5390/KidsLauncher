package com.wiser.kids.ui.camera.editor;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.wiser.kids.R;
import com.wiser.kids.ui.home.contact.ContactEntity;

import java.util.ArrayList;
import java.util.List;

public class PhotoEditorFavContactsAdapter extends RecyclerView.Adapter<PhotoEditorFavContactsAdapter.ViewHolder> {

    private Context mContext;
    private PhotoEditorFavContactsAdapter.Callback mCallback;
    private List<ContactEntity> mSlideItems;
    private LayoutInflater inflater;

    public PhotoEditorFavContactsAdapter(Context context, PhotoEditorFavContactsAdapter.Callback callback) {
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
    public PhotoEditorFavContactsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PhotoEditorFavContactsAdapter.ViewHolder((PhotoEditorContactItemView) inflater.inflate(R.layout.photo_editor_contact, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoEditorFavContactsAdapter.ViewHolder holder, int position) {
        ContactEntity item = mSlideItems.get(position);
        ((PhotoEditorContactItemView) holder.itemView).setSlideItem(item, mCallback);

    }


    @Override
    public int getItemCount() {
        return mSlideItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(PhotoEditorContactItemView itemView) {
            super(itemView);
        }
    }

    public interface Callback {
        void onSlideItemClick(ContactEntity slideItem);
    }
}