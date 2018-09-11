package com.uiu.kids.ui.message;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.uiu.kids.Constant;
import com.uiu.kids.R;
import com.uiu.kids.ui.home.contact.ContactEntity;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class MessageItemView extends ConstraintLayout implements Constant {

    @BindView(R.id.profile_img)
    CircleImageView slideItemImage;

    @BindView(R.id.name)
    TextView itemName;

    @BindView(R.id.phn_no)
    TextView tvPhn;

    @BindView(R.id.select_contact)
    CheckBox checkBox;

    @BindView(R.id.message_item_layout)
    ConstraintLayout layout;

    Animation animScale;

    private MessageAdapterList.Callback callback;
    private ContactEntity slideItem;

    public MessageItemView(Context context) {
        super(context);
    }

    public MessageItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MessageItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this, this);
        animScale = AnimationUtils.loadAnimation(getContext(), R.anim.anim_scale);

    }

    public void setSlideItem(ContactEntity item, MessageAdapterList.Callback callback) {
        this.callback = callback;
        this.slideItem = item;
        if (item != null) {
            itemName.setText(item.getName());
            tvPhn.setText(item.getPhoneNumber().get(0).toString());
            Picasso.with(getContext()).load(slideItem.getPhotoUri()).placeholder(item.getName() != null ? RES_AVATAR : RES_ADD_NEW).into(slideItemImage);
        }
    }


    @OnClick(R.id.profile_img)
    public void onSlideItemClick() {


        slideItemImage.startAnimation(animScale);
        animScale.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                callback.onSlideItemClick(slideItem, false);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
