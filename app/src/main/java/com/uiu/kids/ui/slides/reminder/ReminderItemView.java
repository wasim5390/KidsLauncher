package com.uiu.kids.ui.slides.reminder;

import android.content.Context;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.constraint.Guideline;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.uiu.kids.BaseFragment;
import com.uiu.kids.Constant;
import com.uiu.kids.R;
import com.uiu.kids.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.support.constraint.ConstraintLayout.LayoutParams.BOTTOM;

public class ReminderItemView extends ConstraintLayout implements Constant {

    @BindView(R.id.rem_item)
    ImageView slideItemImage;

    @BindView(R.id.rem_lable)
    TextView itemLable;

    @BindView(R.id.container)
    ConstraintLayout mConstraintLayout;

    @BindView(R.id.guidelineStart)
    Guideline guidelineStart;

    @BindView(R.id.guidelineEnd)
    Guideline guidelineEnd;

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
       // BaseFragment.setColorOnBtn(Color.parseColor("#edeecb"),findViewById(R.id.slideItemView));
        if(slideItem!=null)
        {

            slideItemImage.setImageResource(R.mipmap.alarm_icon);
            itemLable.setText(slideItem.getTitle());
            //  itemdateTimr.setText(Util.formatDate(Util.DATE_FORMAT_4,Long.valueOf(slideItem.getTime())));

        }


    }

    public void adjustView(int size){
        ConstraintSet set = new ConstraintSet();

        // You may want (optional) to start with the existing constraint,
        // so uncomment this.
        set.clone(mConstraintLayout);

        if(size>2) {
            set.connect(R.id.dynamicConstraintView, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 0);
            set.connect(R.id.dynamicConstraintView, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 0);
        }else{
            set.clear(R.id.dynamicConstraintView, ConstraintSet.START);
            set.clear(R.id.dynamicConstraintView, ConstraintSet.END);
            set.connect(R.id.dynamicConstraintView, ConstraintSet.START,guidelineStart.getId() , ConstraintSet.START, 0);
            set.connect(R.id.dynamicConstraintView, ConstraintSet.END, guidelineEnd.getId(), ConstraintSet.END, 0);

        }
        // Apply the changes
        set.applyTo(mConstraintLayout);
    }




    @OnClick(R.id.container)
    public void onSlideItemClick() {
        callback.onSlideItemClick(slideItem);
    }


}