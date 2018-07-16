package com.wiser.kids.ui.home.dialer.callhistory;


import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.CallLog;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.wiser.kids.R;
import com.wiser.kids.util.CallLogManager;
import com.wiser.kids.util.CallRecord;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.provider.MediaStore.Images.Media.getBitmap;

public class CallHistoryAdapter extends RecyclerView.Adapter<ViewHolder> {

    public Context context;
    public CallLogManager callLogManager;
    public List<CallRecord> list;
    public CallHistoryAdapter.onHistoryItemClick onHistoryItemClick;

    public CallHistoryAdapter(Context context, List<CallRecord> callList, CallHistoryAdapter.onHistoryItemClick onHistoryItemClick) {

        this.context=context;
        this.list=callList;
        this.onHistoryItemClick=onHistoryItemClick;
    }



    @Override
    public int getItemViewType(int position) {
        return list.get(position).type;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        View view;
        if(CallRecord.TYPE_SEPERATOR==viewType) {
           view = LayoutInflater.from(context).inflate(R.layout.call_history_list_header, parent, false);
           holder = new HeaderViewHolder(view);
       }
       else {
           view = LayoutInflater.from(context).inflate(R.layout.call_history_list_item, parent, false);
           holder = new ItemViewHolder(view);
       }

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        CallRecord callItem=list.get(position);
        if (holder instanceof HeaderViewHolder)
        {
            ((HeaderViewHolder) holder).header.setText(callItem.displayName);

        }
        else
        {
            ((ItemViewHolder)holder).name.setText(callItem.displayName);
            ((ItemViewHolder)holder).number.setText(callItem.phoneNumber);
            ((ItemViewHolder)holder).time.setText(callItem.timeString);
            int directionStr;
            if (callItem.direction == CallLog.Calls.INCOMING_TYPE) {
                ((ItemViewHolder)holder).directionIcon.setImageResource(R.mipmap.incoming_call_icon);
                directionStr = R.string.incoming_call;
            }
            else if (callItem.direction == CallLog.Calls.OUTGOING_TYPE){
                ((ItemViewHolder)holder).directionIcon.setImageResource(R.mipmap.ougoing_call_icon);
                directionStr = R.string.outgoing_call;
            }
            else {
                ((ItemViewHolder)holder).directionIcon.setImageResource(R.mipmap.missed_call_icon);
                directionStr = R.string.missed_call;
            }

            ((ItemViewHolder)holder).direction.setText(directionStr);

            if (callItem.type == CallRecord.TYPE_KNOWN_CONTACT) {
                if(callItem.photoId!=0) {
                    ((ItemViewHolder)holder).number.setVisibility(View.VISIBLE);
//
//                    ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                    params.setMargins(0,0,0,0);
//                    ((ItemViewHolder)holder).direction.setLayoutParams(params);
                    callLogManager=new CallLogManager(context);
                    callLogManager.loadPhoto(((ItemViewHolder) holder).callerImg, callItem.photoId);
                }
            }
            else {
                ((ItemViewHolder)holder).number.setText("");
//               ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                params.setMargins(200,100,100,100);
//                ((ItemViewHolder)holder).direction.setLayoutParams(params);
                ((ItemViewHolder)holder).callerImg.setImageResource(R.drawable.avatar_male2);
            }

            ((ItemViewHolder)holder).time.setText(callItem.timeString);


        }

        ((ItemViewHolder)holder).itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onHistoryItemClick.onContactSelected(callItem);
            }
        });


    }
    public Bitmap imageLoad(Uri photoId)
    {
        Bitmap bitmap=null;
        try {
            bitmap = getBitmap(context.getContentResolver(),photoId);

        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Exception error Uri",   e.toString());
        }

        return bitmap;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }



    class HeaderViewHolder extends RecyclerView.ViewHolder
    {
        public TextView header;

        public HeaderViewHolder(View itemView)
        {
            super(itemView);
            header=(TextView) itemView.findViewById(R.id.call_history_header_text);
        }

    }

        class ItemViewHolder extends RecyclerView.ViewHolder {
        public TextView name,number,direction,time;
        public ImageView directionIcon;
        public CircleImageView callerImg;
        public View view;
        public ItemViewHolder(View itemView)
        {
            super(itemView);
            this.view=itemView;
            name=(TextView)itemView.findViewById(R.id.call_row_display_name);
            number=(TextView)itemView.findViewById(R.id.call_row_phone_number);
            direction=(TextView)itemView.findViewById(R.id.call_row_direction);
            time=(TextView)itemView.findViewById(R.id.call_row_time);
            callerImg=(CircleImageView) itemView.findViewById(R.id.call_row_avatar);
            directionIcon=(ImageView) itemView.findViewById(R.id.call_row_type_icon);
        }
    }

   public interface onHistoryItemClick
   {
       void onContactSelected(CallRecord callRecord);

   }

}

