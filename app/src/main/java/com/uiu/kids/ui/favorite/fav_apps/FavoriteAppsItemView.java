package com.uiu.kids.ui.favorite.fav_apps;

import android.content.Context;
import android.graphics.drawable.Drawable;
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
import com.uiu.kids.ui.home.apps.AppsEntity;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FavoriteAppsItemView extends ConstraintLayout implements Constant {

    @BindView(R.id.iv_iitem)
    ImageView slideItemImage;

    @BindView(R.id.tv_llable)
    TextView itemLable;

    Animation animScale;

    private FavoriteAppsAdapter.Callback callback;
    private AppsEntity slideItem;

    public FavoriteAppsItemView(Context context) {
        super(context);
    }

    public FavoriteAppsItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FavoriteAppsItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this,this);
        animScale = AnimationUtils.loadAnimation(getContext(),R.anim.anim_scale);

    }

    public void setSlideItem(AppsEntity item, Drawable icon, FavoriteAppsAdapter.Callback callback){
        this.callback = callback;
        this.slideItem = item;
        if(item!=null)
        {
            if(item.getName()!=null) {
                slideItemImage.setEnabled(slideItem.hasAccess());
                this.setAlpha(slideItem.hasAccess()?1:0.65f);
              //  slideItemImage.(icon);
                Picasso.with(getContext()).load(slideItem.getAppIcon()).into(slideItemImage);
                itemLable.setText(item.getName());

            }
            else
            {
                itemLable.setText("Add New");
                slideItemImage.setImageResource(R.mipmap.ic_add_icon);
            }
        }

    }

    @OnClick(R.id.iv_iitem)
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
