package com.wiser.kids.ui.SOS;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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

public class SOSItemView  extends ConstraintLayout implements Constant {

    @BindView(R.id.sos_item)
    CircleImageView slideItemImage;

    @BindView(R.id.sos_lable)
    TextView itemLable;

    Animation animScale;

    private SOSListAdapter.Callback callback;
    private ContactEntity slideItem;

    public SOSItemView(Context context) {
        super(context);
    }

    public SOSItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SOSItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this,this);
        animScale = AnimationUtils.loadAnimation(getContext(),R.anim.anim_scale);

    }

    public void setSlideItem(ContactEntity item, SOSListAdapter.Callback callback){
        this.callback = callback;
        this.slideItem = item;
        if(item !=null) {
            if(item.getName()!=null) {

              //  slideItemImage.setEnabled(slideItem.hasAccess());
                this.setAlpha(slideItem.hasAccess()?1:0.65f);
                itemLable.setText(item.getName());
                Picasso.with(getContext()).load(slideItem.getPhotoUri()).placeholder(item.getName() != null ? RES_AVATAR : RES_ADD_NEW).into(slideItemImage);

            }
            else
            {
                itemLable.setText("Add New");
                slideItemImage.setImageResource(R.mipmap.ic_add_icon);
            }

        }
    }

    @OnClick(R.id.sos_item)
    public void onSlideItemClick(){
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