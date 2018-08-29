package com.wiser.kids.ui.notification;

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
import com.wiser.kids.ui.home.contact.ContactEntity;
import com.wiser.kids.ui.message.MessageAdapterList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class NotificationItemView extends ConstraintLayout implements Constant {

    @BindView(R.id.sender_image)
    ImageView senderImage;

    @BindView(R.id.senderName)
    TextView tvSenderName;

    @BindView(R.id.notificationMsg)
    TextView tvMessage;

    @BindView(R.id.notificationTitle)
    TextView tvTitle;

    Animation animScale;

    private NotificationAdapterList.Callback callback;
    private NotificationEntity slideItem;

    public NotificationItemView(Context context) {
        super(context);
    }

    public NotificationItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NotificationItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this, this);
        animScale = AnimationUtils.loadAnimation(getContext(), R.anim.anim_scale);

    }

    public void setSlideItem(NotificationEntity item, NotificationAdapterList.Callback callback) {
        this.callback = callback;
        this.slideItem = item;
        if (item != null) {

            tvSenderName.setText(slideItem.getSenderName());
            tvMessage.setText(slideItem.getMessage());
            tvTitle.setText(slideItem.getTitle());
            Picasso.with(getContext()).load(slideItem.getSenderImage()).placeholder(RES_AVATAR ).into(senderImage);
        }
    }



    @OnClick(R.id.sender_image)
    public void onSlideItemClick() {
        senderImage.startAnimation(animScale);
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
