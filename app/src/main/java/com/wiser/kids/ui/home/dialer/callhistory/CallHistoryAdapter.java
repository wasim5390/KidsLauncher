package com.wiser.kids.ui.home.dialer.callhistory;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.ViewGroup;

public class CallHistoryAdapter extends RecyclerView.Adapter<ViewHolder> {

    @Override
    public int getItemViewType(int position) {
        // Just as an example, return 0 or 2 depending on position
        // Note that unlike in ListView adapters, types don't have to be contiguous
        return position % 2 * 2;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        switch (viewType) {
            case 0:
                return new ViewHolderListItem(parent);
            case 2:
                return new ViewHolderHeader(parent);

        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case 0:
                ViewHolderListItem viewHolder0 = (ViewHolderListItem) holder;

                break;

            case 2:
                ViewHolderHeader viewHolder2 = (ViewHolderHeader) holder;

                break;
        }
    }


    @Override
    public int getItemCount() {
        return 0;
    }



    class ViewHolderListItem extends ViewHolder {

        public ViewHolderListItem(View itemView) {
            super(itemView);
        }

    }
        class ViewHolderHeader extends ViewHolder
    {

        public ViewHolderHeader(View itemView) {
            super(itemView);
        }
    }

}

