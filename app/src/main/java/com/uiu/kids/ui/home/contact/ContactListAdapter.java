package com.uiu.kids.ui.home.contact;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.uiu.kids.R;


import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ViewHolder>{


    private List<ContactEntity> items;
    private Context context;
    private OnItemClickListener clickListener;

    public ContactListAdapter(Context context, List<ContactEntity> items, OnItemClickListener clickListener) {
        this.items = items;
        this.context = context;
        this.clickListener = clickListener;
    }

    @Override
    public ContactListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.contact_list_item_view, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ContactListAdapter.ViewHolder holder, int position) {
        holder.tvContactTitle.setText(items.get(position).getName());
        holder.view.setOnClickListener(v -> clickListener.onItemClick(holder.getAdapterPosition()));
        Picasso.get().load(items.get(position).getPhotoUri())
                .error(R.mipmap.avatar_male2).placeholder(R.mipmap.avatar_male2)
                .into(holder.profileImage);

    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public ContactEntity getItem(int position) {
        return items.get(position);
    }

    public void updateItems(List<ContactEntity> newItems) {
        items.clear();
        items.addAll(newItems);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView profileImage;
        TextView tvContactTitle;
        View view;
        public ViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            profileImage = view.findViewById(R.id.profile_image);
            tvContactTitle = view.findViewById(R.id.contact_name);
        }
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }
}