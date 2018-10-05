package com.uiu.kids.ui.slides.reminder;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.uiu.kids.Constant;
import com.uiu.kids.R;
import com.uiu.kids.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReminderItemView extends ConstraintLayout implements Constant {

    @BindView(R.id.rem_item)
    ImageView slideItemImage;

    @BindView(R.id.rem_lable)
    TextView itemLable;

    @BindView(R.id.rem_dateTime)
    TextView itemdateTimr;

    Animation animScale;

    private ReminderAdapterList.Callback callback;
    private ReminderEntity slideItem;

    public ReminderItemView(Context context) {
        super(context);
    }

    public ReminderItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ReminderItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this, this);
        animScale = AnimationUtils.loadAnimation(getContext(), R.anim.anim_focus);

    }

    public void setSlideItem(ReminderEntity item, ReminderAdapterList.Callback callback) {
        this.callback = callback;
        this.slideItem = item;

        if(slideItem!=null)
        {

                slideItemImage.setImageResource(R.mipmap.alarm_icon);
                itemLable.setText(slideItem.getTitle());
                itemdateTimr.setText(Util.formatDate(Util.DATE_FORMAT_4,Long.valueOf(slideItem.getTime())));

        }


    }




    @OnClick(R.id.container)
    public void onSlideItemClick() {

        findViewById(R.id.container).startAnimation(animScale);
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