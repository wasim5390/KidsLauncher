package com.wiser.kids.ui.home.dialer.callhistory;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.wiser.kids.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class ListItemView extends ConstraintLayout {
    @BindView(R.id.call_row_avatar)
    public CircleImageView imageView;
    @BindView(R.id.call_row_display_name)
    public TextView tvName;
    @BindView(R.id.call_row_phone_number)
    public TextView tvPhone_num;
    @BindView(R.id.call_row_direction)
    public TextView tvDirection;
    @BindView(R.id.call_row_type_icon)
    public ImageView image_Direction_type;
    @BindView(R.id.call_row_time)
    public TextView tvTime;
    public ListItemView(Context context) {
        super(context);
    }
    public ListItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this,this);

    }
    public void setData()
    {

    }
    @OnClick(R.id.call_row_avatar)
    public void onIconClicked()
    {//icon pic click listner

    }
}
