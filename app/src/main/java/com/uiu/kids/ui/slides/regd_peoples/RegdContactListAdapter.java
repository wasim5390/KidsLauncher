package com.uiu.kids.ui.slides.regd_peoples;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.uiu.kids.R;
import com.uiu.kids.ui.home.contact.ContactEntity;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegdContactListAdapter extends RecyclerView.Adapter<RegdContactListAdapter.ViewHolder>{


    private List<ContactEntity> items;
    private Context context;
    private OnItemClickListener clickListener;

    public RegdContactListAdapter(Context context, List<ContactEntity> items, OnItemClickListener clickListener) {
        this.items = items;
        this.context = context;
        this.clickListener = clickListener;
    }

    @Override
    public RegdContactListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.contact_list_item_view, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RegdContactListAdapter.ViewHolder holder, int position) {
        holder.tvContactTitle.setText(items.get(position).getName());
        holder.tvContactEmail.setVisibility(items.get(position).getEmail()!=null?View.VISIBLE:View.GONE);
        holder.tvContactEmail.setText(items.get(position).getEmail());
        holder.view.setOnClickListener(v -> clickListener.onItemClick(holder.getAdapterPosition()));
        Picasso.with(context).load(items.get(position).getPhotoUri())
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
        TextView tvContactTitle,tvContactEmail;
        View view;
        public ViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            profileImage = view.findViewById(R.id.profile_image);
            tvContactTitle = view.findViewById(R.id.contact_name);
            tvContactEmail = view.findViewById(R.id.contact_email);
        }
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }
}