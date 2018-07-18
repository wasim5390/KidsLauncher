package com.wiser.kids.ui.home.apps;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wiser.kids.R;
import com.wiser.kids.util.CallRecord;

import java.util.ArrayList;
import java.util.List;

public class AppsListAdapter extends RecyclerView.Adapter<AppsListAdapter.ViewHolder> {

    private Context context;
    private List<AppsEntity> appList =new ArrayList<AppsEntity>();
    public  AppsListAdapter.onAppItemClick appItemClick;

   public AppsListAdapter(List<AppsEntity> list,Context context,AppsListAdapter.onAppItemClick appItemClick)
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
       holder.appIcon.setImageDrawable(appsEntity.getIcon());

       holder.view.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               appItemClick.onAppSelected(appsEntity);
           }
       });


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
