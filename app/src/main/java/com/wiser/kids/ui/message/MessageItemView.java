package com.wiser.kids.ui.message;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.wiser.kids.Constant;
import com.wiser.kids.R;
import com.wiser.kids.ui.favorite.people.FavoritePeopleAdapter;
import com.wiser.kids.ui.home.contact.ContactEntity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class MessageItemView extends ConstraintLayout implements Constant {

    @BindView(R.id.profile_img)
    CircleImageView slideItemImage;

    @BindView(R.id.name)
    TextView itemName;

    @BindView(R.id.phn_no)
    TextView tvPhn;

    @BindView(R.id.select_contact)
    CheckBox checkBox;

    @BindView(R.id.message_item_layout)
    ConstraintLayout layout;

    Animation animScale;

    private MessageAdapterList.Callback callback;
    private ContactEntity slideItem;

    public MessageItemView(Context context) {
        super(context);
    }

    public MessageItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MessageItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this, this);
        animScale = AnimationUtils.loadAnimation(getContext(), R.anim.anim_scale);

    }

    public void setSlideItem(ContactEntity item, MessageAdapterList.Callback callback) {
        this.callback = callback;
        this.slideItem = item;
        if (item != null) {
            itemName.setText(item.getName());
            tvPhn.setText(item.getPhoneNumber().get(0).toString());
            Picasso.with(getContext()).load(slideItem.getPhotoUri()).placeholder(item.getName() != null ? RES_AVATAR : RES_ADD_NEW).into(slideItemImage);
        }
    }


    @OnClick(R.id.select_contact)
    public void onSlideItemClick() {

        if (checkBox.isChecked()) {
            layout.setBackgroundColor(getResources().getColor(R.color.hepler_color));
            callback.onSlideItemClick(slideItem, true);
        } else {
            layout.setBackgroundColor(getResources().getColor(R.color.transparent));
            callback.onSlideItemClick(slideItem, false);
        }

    }
}
