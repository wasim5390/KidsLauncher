package com.uiu.kids.ui.slides.invitation;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.uiu.kids.R;
import com.uiu.kids.model.Invitation;

import java.util.List;


public class InviteListAdapter extends RecyclerView.Adapter<InviteListAdapter.ViewHolder> {

    private Context mContext;
    private InviteListAdapter.Callback mCallback;
    private List<Invitation> mInviteList;
    private LayoutInflater inflater;

    public InviteListAdapter(Context context, List<Invitation> invitations, InviteListAdapter.Callback callback) {
        this.mCallback = callback;
        this.mContext = context;
        this.inflater = LayoutInflater.from(context);
        this.mInviteList = invitations;
    }


    public void setItems(List<Invitation> slideItems) {
        if(slideItems!=null) {
            this.mInviteList.clear();
            this.mInviteList.addAll(slideItems);
            notifyDataSetChanged();
        }

    }


    @NonNull
    @Override
    public InviteListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InviteListAdapter.ViewHolder((InviteListItemView) inflater.inflate(R.layout.invite_list_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull InviteListAdapter.ViewHolder holder, int position) {
        Invitation item = mInviteList.get(position);
        ((InviteListItemView) holder.itemView).setSlideItem(item,mCallback);

    }


    @Override
    public int getItemCount() {
        return mInviteList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(InviteListItemView itemView) {
            super(itemView);
        }
    }

    public interface Callback {
        void onItemClick(Invitation slideItem);
        void onItemLongClick(Invitation slideItem);
    }
}
