package com.wiser.kids.ui.home.apps;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wiser.kids.R;

import java.util.ArrayList;
import java.util.List;

public class AppsListAdapter extends RecyclerView.Adapter<AppsListAdapter.ViewHolder> {

    private Context context;
    private List<AppsEntity> appList =new ArrayList<AppsEntity>();
    public  AppsListAdapter.onAppItemClick appItemClick;
    public  List<PackageInfo> packageInfoList=new ArrayList<>();

   public AppsListAdapter(List<AppsEntity> list, List<PackageInfo> packageInfoList, Context context, AppsListAdapter.onAppItemClick appItemClick)
   {
       this.context=context;
       this.appList=list;
       this.appItemClick=appItemClick;
       this.packageInfoList=packageInfoList;

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
       PackageInfo pack=packageInfoList.get(position);

       holder.appName.setText(pack.applicationInfo.loadLabel(context.getPackageManager()).toString());
       holder.appIcon.setImageDrawable(pack.applicationInfo.loadIcon(context.getPackageManager()));

     //  Log.e("pakag neme",pack.packageName);

       holder.view.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               appItemClick.onAppSelected(pack);
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
        void onAppSelected(PackageInfo packageInfo);

    }

}
