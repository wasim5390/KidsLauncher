package com.wiser.kids.ui.favorite.fav_apps;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.wiser.kids.Constant;
import com.wiser.kids.R;
import com.wiser.kids.ui.home.apps.AppsEntity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class FavoriteAppsItemView extends ConstraintLayout implements Constant {

    @BindView(R.id.iv_iitem)
    CircleImageView slideItemImage;

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

    public void setSlideItem(AppsEntity item,Drawable icon, FavoriteAppsAdapter.Callback callback){
        this.callback = callback;
        this.slideItem = item;
        if(item!=null)
        {
            if(!item.getFlagEmptyList()) {
                    slideItemImage.setEnabled(slideItem.hasAccess());
                    this.setAlpha(slideItem.hasAccess()?1:0.65f);
                slideItemImage.setImageDrawable(icon);
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
                slideItem.setHasAccess(false);
                callback.onSlideItemClick(slideItem);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


    }


}