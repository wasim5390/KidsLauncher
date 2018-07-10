package com.wiser.kids.ui.home;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.wiser.kids.Constant;
import com.wiser.kids.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeItemView extends ConstraintLayout implements Constant{
    @BindView(R.id.iv_item)
    ImageView slideItemImage;
    @BindView(R.id.tv_lable)
    TextView itemLable;
    Animation animScale;

    private HomeSlideAdapter.Callback callback;
    private String slideItem="";

    public HomeItemView(Context context) {
        super(context);
    }

    public HomeItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HomeItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this,this);
        animScale = AnimationUtils.loadAnimation(getContext(),R.anim.anim_scale);

    }

    public void setSlideItem(String item, HomeSlideAdapter.Callback callback){
        this.callback = callback;
        this.slideItem = item;
        itemLable.setText(item);
        switch (slideItem){
            case CONTACTS:
                slideItemImage.setImageResource(RES_CONTACTS);
                break;
            case DIALER:
                slideItemImage.setImageResource(RES_DIALER);
                break;
            case APPLICATIONS:
                slideItemImage.setImageResource(RES_APPS);
                break;
            case CAMERA:
                slideItemImage.setImageResource(RES_CAMERA);
                break;
            case MESSAGING:
                // @TODO have to update this
                slideItemImage.setImageResource(RES_ADD_NEW);
                break;
        }
    }

    @OnClick(R.id.iv_item)
    public void onSlideItemClick(){
        slideItemImage.startAnimation(animScale);
        callback.onSlideItemClick(slideItem);
    }
}
