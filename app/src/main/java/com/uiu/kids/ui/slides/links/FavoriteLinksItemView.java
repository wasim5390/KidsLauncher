package com.uiu.kids.ui.slides.links;

import android.app.Activity;
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


import java.util.logging.Handler;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import de.hdodenhof.circleimageview.CircleImageView;
import io.github.ponnamkarthik.richlinkpreview.MetaData;
import io.github.ponnamkarthik.richlinkpreview.ResponseListener;
import io.github.ponnamkarthik.richlinkpreview.RichLinkView;
import io.github.ponnamkarthik.richlinkpreview.RichPreview;
import io.github.ponnamkarthik.richlinkpreview.ViewListener;

public class FavoriteLinksItemView extends ConstraintLayout implements Constant {

    @BindView(R.id.iv_iitem)
    CircleImageView slideItemImage;

    @BindView(R.id.tv_llable)
    TextView itemLable;

    @BindView(R.id.richLinkCard)
    ConstraintLayout richConstraintCard;

    @BindView(R.id.rich_link_image)
    ImageView richLinkImage;

    @BindView(R.id.rich_link_title)
    TextView richLinkTitle;

    @BindView(R.id.rich_link_desp)
    TextView richLinkDesc;

    @BindView(R.id.rich_link_url)
    TextView richLinkUrl;


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
            if (item.getId()!=null) {
                itemLable.setVisibility(GONE);
                richConstraintCard.setVisibility(VISIBLE);
                slideItemImage.setVisibility(GONE);
                richConstraintCard.setEnabled(slideItem.hasAccess());
                ivTimer.setVisibility(slideItem.hasAccess()?GONE:VISIBLE);

                if( slideItem.getIcon_url().isEmpty()) {
                    Picasso.get()
                            .load(R.drawable.placeholder_sqr)
                            .into(richLinkImage);
                } else {

                    Picasso.get()
                            .load(item.getIcon_url()).placeholder(R.drawable.placeholder_sqr)
                            .into(richLinkImage);
                }

                if(slideItem.getTitle()==null || slideItem.getTitle().isEmpty()) {
                    richLinkTitle.setVisibility(GONE);
                } else {
                    richLinkTitle.setVisibility(VISIBLE);
                    richLinkTitle.setText(slideItem.getTitle());
                }
                if(slideItem.getLink()==null) {
                    richLinkUrl.setText(slideItem.getLink());
                } else {
                    richLinkUrl.setVisibility(VISIBLE);
                    richLinkUrl.setText(slideItem.getLink());
                }
                if(slideItem.getDesc()==null || slideItem.getDesc().isEmpty()) {
                    richLinkDesc.setVisibility(GONE);
                } else {
                    richLinkDesc.setVisibility(VISIBLE);
                    richLinkDesc.setText(item.getDesc());
                }

            } else {

                ivTimer.setVisibility(GONE);
                richConstraintCard.setVisibility(GONE);
                itemLable.setText("Add New");
                slideItemImage.setVisibility(VISIBLE);
                slideItemImage.setImageResource(R.mipmap.ic_add_icon);
                slideItemImage.setEnabled(true);
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

    @OnClick(R.id.richLinkCard)
    public void onLinkClick(){
        callback.onSlideItemClick(slideItem);
    }


}

