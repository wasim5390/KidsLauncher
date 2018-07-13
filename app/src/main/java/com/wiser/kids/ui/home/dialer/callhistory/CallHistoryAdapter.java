package com.wiser.kids.ui.home.dialer.callhistory;


import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.CallLog;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wiser.kids.R;
import com.wiser.kids.util.CallRecord;

import java.io.IOException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CallHistoryAdapter extends RecyclerView.Adapter<ViewHolder> {

    private Context context;
    public ArrayList<CallRecord> list;

    public CallHistoryAdapter(Context context, ArrayList<CallRecord> callList) {

        this.context=context;
        this.list=callList;
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

               // ((ItemViewHolder)holder).callerImg.setImageBitmap(imageLoad(callItem.photoId));
                ((ItemViewHolder)holder).callerImg.setImageResource(R.drawable.avatar_male2);

            }
            else {
                ((ItemViewHolder)holder).callerImg.setImageResource(R.drawable.avatar_male2);
            }

            ((ItemViewHolder)holder).time.setText(callItem.timeString);


        }


    }
    public Bitmap imageLoad(long photoId)
    {
        Bitmap bitmap=null;
        Uri uri=Uri.parse(String.valueOf(photoId));
        try {
            bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(),uri);

        } catch (IOException e) {
            e.printStackTrace();
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
        public ItemViewHolder(View itemView)
        {
            super(itemView);
            name=(TextView)itemView.findViewById(R.id.call_row_display_name);
            number=(TextView)itemView.findViewById(R.id.call_row_phone_number);
            direction=(TextView)itemView.findViewById(R.id.call_row_direction);
            time=(TextView)itemView.findViewById(R.id.call_row_time);
            callerImg=(CircleImageView) itemView.findViewById(R.id.call_row_avatar);
            directionIcon=(ImageView) itemView.findViewById(R.id.call_row_type_icon);
        }
    }

}

