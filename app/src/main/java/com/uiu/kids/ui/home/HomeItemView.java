package com.uiu.kids.ui.home;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.uiu.kids.Constant;
import com.uiu.kids.R;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class HomeItemView extends ConstraintLayout implements Constant {

    @BindView(R.id.iv_item)
    CircleImageView slideItemImage;

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
            case DIALER:
                slideItemImage.setImageResource(RES_DIALER);
                break;
            case CAMERA:
                slideItemImage.setImageResource(RES_CAMERA);
                break;
            case C_ME:
                slideItemImage.setImageResource(RES_SEE_ME);
                break;
            case SOS:
                slideItemImage.setImageResource(RES_SOS);
                break;

        }
    }

    @OnClick(R.id.iv_item)
    public void onSlideItemClick(){
        slideItemImage.startAnimation(animScale);
        callback.onSlideItemClick(slideItem);
    }
}
