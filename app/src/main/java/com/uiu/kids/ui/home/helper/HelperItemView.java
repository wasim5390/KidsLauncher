package com.uiu.kids.ui.home.helper;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.uiu.kids.Constant;
import com.uiu.kids.R;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HelperItemView extends ConstraintLayout implements Constant {

    @BindView(R.id.helper_profile_img)
    ImageView slideItemImage;

    @BindView(R.id.helper_name)
    TextView name;

    @BindView(R.id.helper_email)
    TextView tvPhoneNumber;

    @BindView(R.id.helper_item_layput)
    ConstraintLayout layout;

    @BindView(R.id.select_helper)
    CheckBox checkBox;

    Animation animScale;

    private HelperAdapterList.Callback callback;
    private HelperEntity slideItem;

    public HelperItemView(Context context) {
        super(context);
    }

    public HelperItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HelperItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this, this);
        animScale = AnimationUtils.loadAnimation(getContext(), R.anim.anim_scale);

    }

    public void setSlideItem(HelperEntity item, HelperAdapterList.Callback callback) {
        this.callback = callback;
        this.slideItem = item;
        if (item != null) {
            name.setText(slideItem.getFirst_name());
            tvPhoneNumber.setText(slideItem.getEmail());
            checkBox.setChecked(slideItem.isHelperSelected);
            if (slideItem.getImage_link().isEmpty()) {
                slideItemImage.setImageResource(R.drawable.avatar_male2);
            } else {
                Picasso.with(getContext()).load(slideItem.getImage_link()).placeholder(R.drawable.avatar_male2).into(slideItemImage);
            }
        }

    }

    @OnClick(R.id.select_helper)
    public void onSlideItemClick() {

        if (checkBox.isChecked())
        {
            layout.setBackgroundColor(getResources().getColor(R.color.hepler_color));
            callback.onSlideItemClick(slideItem,true);
        }
        else
        {
            layout.setBackgroundColor(getResources().getColor(R.color.transparent));
            callback.onSlideItemClick(slideItem,false);
        }

    }



}