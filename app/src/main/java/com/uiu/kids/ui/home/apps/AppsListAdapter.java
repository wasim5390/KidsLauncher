package com.uiu.kids.ui.home.apps;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.uiu.kids.R;

import java.util.List;

public class AppsListAdapter extends RecyclerView.Adapter<AppsListAdapter.ViewHolder> {

    private Context context;
    private List<AppsEntity> appList;
    public  AppsListAdapter.onAppItemClick appItemClick;

    public AppsListAdapter(List<AppsEntity> list, Context context, AppsListAdapter.onAppItemClick appItemClick)
    {
        this.context=context;
        this.appList=list;
        this.appItemClick=appItemClick;

    }


    @NonNull
    @Override
    public AppsListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.app_list_item_view,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppsListAdapter.ViewHolder holder, int position) {

        AppsEntity appsEntity=appList.get(position);

        holder.appName.setText(appsEntity.getName());

        holder.appIcon.setImageDrawable(appsEntity.getIcon(context));


        holder.view.setOnClickListener(v -> appItemClick.onAppSelected(appsEntity));


    }
    public void setAppList(List<AppsEntity> appList){
        this.appList.clear();
        this.appList.addAll(appList);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return appList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView appName;
        public ImageView appIcon;
        public View view;

        public ViewHolder(View itemView) {
            super(itemView);
            view=itemView;
            appIcon=(ImageView) itemView.findViewById(R.id.apps_list_img);
            appName=(TextView) itemView.findViewById(R.id.app_name);
        }
    }

    public interface onAppItemClick
    {
        void onAppSelected(AppsEntity appsEntity);

    }

}