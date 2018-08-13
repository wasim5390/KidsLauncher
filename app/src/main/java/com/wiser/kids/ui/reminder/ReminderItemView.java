package com.wiser.kids.ui.reminder;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.wiser.kids.Constant;
import com.wiser.kids.R;
import com.wiser.kids.model.LinksEntity;
import com.wiser.kids.ui.favorite.links.FavoriteLinksAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

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
        animScale = AnimationUtils.loadAnimation(getContext(), R.anim.anim_scale);

    }

    public void setSlideItem(ReminderEntity item, ReminderAdapterList.Callback callback) {
        this.callback = callback;
        this.slideItem = item;

        if(slideItem!=null)
        {
            if(slideItem.isActiveReminder()) {

                slideItemImage.setImageResource(R.mipmap.alarm_icon);
                itemLable.setText(slideItem.getTitle());
                itemdateTimr.setText(slideItem.getTime());

            }
        }


    }




    @OnClick(R.id.rem_item)
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