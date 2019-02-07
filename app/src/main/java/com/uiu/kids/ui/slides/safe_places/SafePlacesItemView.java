package com.uiu.kids.ui.slides.safe_places;

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
import com.uiu.kids.model.Location;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class SafePlacesItemView extends ConstraintLayout implements Constant {

    @BindView(R.id.iv_item)
    CircleImageView slideItemImage;

    @BindView(R.id.tv_lable)
    TextView itemLable;
    @BindView(R.id.ivEggTimer)
    ImageView ivTimer;

    Animation animScale;
    private boolean isInEditMode;
    private SafePlacesAdapter.Callback callback;
    private Location slideItem;

    public SafePlacesItemView(Context context) {
        super(context);
    }

    public SafePlacesItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SafePlacesItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this,this);
        animScale = AnimationUtils.loadAnimation(getContext(),R.anim.anim_scale);

    }

    public void setSlideItem(Location item, boolean isInEditMode, SafePlacesAdapter.Callback callback){
        this.callback = callback;
        this.slideItem = item;
        this.isInEditMode = isInEditMode;

        if(item !=null) {
            if(item.getId()!=null) {
                slideItemImage.setEnabled(true);
                itemLable.setText(item.getTitle());
               // if(slideItem.getImage()==null)
               // slideItemImage.setImageResource(R.drawable.directions_icon);
               // else
               //     Picasso.get().load(slideItem.getImage()).error(R.drawable.directions_icon).fit().into(slideItemImage);


            }

        }
    }


    @OnClick(R.id.iv_item)
    public void onSlideItemClick(){
        if(slideItem.getId()!=null && !isInEditMode)
            return;
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
