package com.uiu.kids.ui.slides.links;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.uiu.kids.Constant;
import com.uiu.kids.R;
import com.uiu.kids.model.LinksEntity;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class FavoriteLinksItemView extends ConstraintLayout implements Constant {

    @BindView(R.id.iv_iitem)
    CircleImageView slideItemImage;

    @BindView(R.id.tv_llable)
    TextView itemLable;

    @BindView(R.id.ivEggTimer)
    ImageView ivTimer;

    Animation animScale;

    private FavoriteLinksAdapter.Callback callback;
    private LinksEntity slideItem;

    public FavoriteLinksItemView(Context context) {
        super(context);
    }

    public FavoriteLinksItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FavoriteLinksItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this, this);
        animScale = AnimationUtils.loadAnimation(getContext(), R.anim.anim_scale);

    }

    public void setSlideItem(LinksEntity item, FavoriteLinksAdapter.Callback callback) {
        this.callback = callback;
        this.slideItem = item;
        if (item != null) {
            if (!item.getFlagEmptylist()) {
                slideItemImage.setEnabled(slideItem.hasAccess());
                ivTimer.setVisibility(slideItem.hasAccess()?GONE:VISIBLE);
                Picasso.with(getContext()).load(slideItem.icon_url).placeholder(R.mipmap.website).into(slideItemImage);
                itemLable.setVisibility(GONE);
                itemLable.setText(slideItem.getLinkName());
                Log.e("link", slideItem.getLinkName());
            } else {
                ivTimer.setVisibility(GONE);
                itemLable.setText("Add New");
                slideItemImage.setImageResource(R.mipmap.ic_add_icon);
            }
        }

    }

    @OnClick(R.id.iv_iitem)
    public void onSlideItemClick() {

        slideItemImage.startAnimation(animScale);
        animScale.setAnimationListener(new Animation.AnimationListener() {
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

