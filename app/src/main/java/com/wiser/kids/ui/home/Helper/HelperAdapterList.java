package com.wiser.kids.ui.home.Helper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.wiser.kids.R;

import java.util.List;

public class HelperAdapterList extends RecyclerView.Adapter<HelperAdapterList.ViewHolder> {

    private Context mContext;
    private HelperAdapterList.Callback mCallback;
    private List<HelperEntity> mSlideItems;
    private LayoutInflater inflater;

    public HelperAdapterList(Context context,List<HelperEntity> favItems, HelperAdapterList.Callback callback) {
        this.mCallback = callback;
        this.mContext = context;
        this.inflater = LayoutInflater.from(context);
        this.mSlideItems = favItems;
    }


    public void setSlideItems(List<HelperEntity> slideItems) {
        if(slideItems!=null) {
            this.mSlideItems.clear();
            this.mSlideItems.addAll(slideItems);
            notifyDataSetChanged();
        }

    }

    @NonNull
    @Override
    public HelperAdapterList.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HelperAdapterList.ViewHolder((HelperItemView) inflater.inflate(R.layout.helper_list_item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HelperAdapterList.ViewHolder holder, int position) {
        HelperEntity item = mSlideItems.get(position);
        ((HelperItemView) holder.itemView).setSlideItem(item,mCallback);

    }


    @Override
    public int getItemCount() {
        return mSlideItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(HelperItemView itemView) {
            super(itemView);
        }
    }

    public interface Callback {
        void onSlideItemClick(HelperEntity slideItem,boolean isChecked);
    }
}
