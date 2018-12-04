package com.uiu.kids.ui.slides.people;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.uiu.kids.Constant;
import com.uiu.kids.R;
import com.uiu.kids.ui.home.contact.ContactEntity;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class FavoritePeopleItemView extends ConstraintLayout implements Constant {

    @BindView(R.id.iv_item)
    CircleImageView slideItemImage;

    @BindView(R.id.tv_lable)
    TextView itemLable;

    @BindView(R.id.ivEggTimer)
    ImageView ivTimer;

    Animation animScale;

    private FavoritePeopleAdapter.Callback callback;
    private ContactEntity slideItem;

    public FavoritePeopleItemView(Context context) {
        super(context);
    }

    public FavoritePeopleItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FavoritePeopleItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this,this);
        animScale = AnimationUtils.loadAnimation(getContext(),R.anim.anim_scale);

    }

    public void setSlideItem(ContactEntity item, FavoritePeopleAdapter.Callback callback){
        this.callback = callback;
        this.slideItem = item;
        if(item !=null) {
            if(item.getId()!=null) {
                slideItemImage.setEnabled(slideItem.hasAccess());
                ivTimer.setVisibility(slideItem.hasAccess()?GONE:VISIBLE);
                itemLable.setText(item.getName());
                Picasso.get().load(slideItem.getProfilePic()).placeholder(item.getName() != null ? RES_AVATAR : RES_ADD_NEW).into(slideItemImage);

            }
            else
            {

                ivTimer.setVisibility(GONE);
                itemLable.setText("Add New");
                slideItemImage.setImageResource(R.mipmap.ic_add_icon);
                slideItemImage.setEnabled(true);
            }

        }
    }

    @OnClick(R.id.iv_item)
    public void onSlideItemClick(){
        slideItemImage.startAnimation(animScale);
        animScale.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                callback.onSlideItemClick(slideItem);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }
}
