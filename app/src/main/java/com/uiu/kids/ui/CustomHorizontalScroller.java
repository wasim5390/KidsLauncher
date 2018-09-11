package com.uiu.kids.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;

public class CustomHorizontalScroller extends HorizontalScrollView {

    public CustomHorizontalScroller(Context context, AttributeSet attrs,
                                   int defStyle) {
        super(context, attrs, defStyle);
    }

    public CustomHorizontalScroller(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomHorizontalScroller(Context context) {
        super(context);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        fullScroll(FOCUS_RIGHT);
    }
}
