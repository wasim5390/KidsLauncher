package com.uiu.kids.ui.invitation;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.squareup.picasso.Picasso;
import com.uiu.kids.BaseActivity;
import com.uiu.kids.Constant;
import com.uiu.kids.R;
import com.uiu.kids.model.Invitation;
import com.uiu.kids.model.User;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import butterknife.internal.Utils;

public class InviteListItemView extends LinearLayout implements Constant {


    @BindView(R.id.invite_screen_profile_photo)
    ImageView ivProfileImage;

    @BindView(R.id.tvHelperName)
    TextView email;

    @BindView(R.id.tvHelperStatus)
    TextView status;

    Invitation mItem;
    InviteListAdapter.Callback callback;

    public InviteListItemView(Context context) {
        super(context);
    }

    public InviteListItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public InviteListItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this, this);

    }

    public void setSlideItem(Invitation item, InviteListAdapter.Callback callback) {
        this.callback = callback;
        this.mItem = item;
        if (mItem != null) {
            User sender = mItem.getSender();
            email.setText(mItem.getSender().getEmail());
            status.setText(getStatus(mItem.getStatus()));
            if (sender.getImageLink()==null || sender.getImageLink().isEmpty()) {
                ivProfileImage.setImageResource(R.drawable.avatar_male2);
            } else {
                Picasso.with(getContext()).load(sender.getImageLink()).placeholder(R.drawable.avatar_male2).into(ivProfileImage);
            }
            if(mItem.getStatus()==INVITE.CONNECTED)
                BaseActivity.primaryParentId=mItem.getSender().getId();
        }

    }

    private String getStatus(int statusCode){
        String status="";
        switch (statusCode){
            case INVITE.CONNECTED:
                status= "CONNECTED";
                break;
            case INVITE.INVITED:
                status= "INVITED";
                break;
            case INVITE.REJECTED:
                status= "REJECTED";
                break;
        }
        return status;
    }

    @OnClick(R.id.view)
    public void onKidClick()
    {
        callback.onItemClick(mItem);

    }

    @OnLongClick(R.id.view)
    public boolean onKidLongClick(){
        callback.onItemLongClick(mItem);
        return true;
    }
}