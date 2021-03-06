package com.uiu.kids.ui.share;

import android.content.Context;
import android.os.Handler;
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

public class ShareToUserItemView extends ConstraintLayout implements Constant {

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

    private ShareToAdapterList.Callback callback;
    private ContactEntity slideItem;

    public ShareToUserItemView(Context context) {
        super(context);
    }

    public ShareToUserItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ShareToUserItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this, this);
        animScale = AnimationUtils.loadAnimation(getContext(), R.anim.anim_focus);

    }

    public void setSlideItem(ContactEntity item, ShareToAdapterList.Callback callback) {
        this.callback = callback;
        this.slideItem = item;
        if (item != null) {
            String name = item.getName()!=null?item.getName():"";
            String email =item.getEmail()!=null?item.getEmail():"";
            itemName.setText(name.trim().isEmpty()?email:name);
            Picasso.get().load(slideItem.getProfilePic()).placeholder(item.getName() != null ? RES_AVATAR : RES_ADD_NEW).into(slideItemImage);
        }
    }


    @OnClick(R.id.message_item_layout)
    public void onSlideItemClick() {


        layout.startAnimation(animScale);
        animScale.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                checkBox.setVisibility(VISIBLE);
                new Handler().postDelayed(() -> callback.onSlideItemClick(slideItem, false),100);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
