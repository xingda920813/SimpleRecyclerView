package com.xdandroid.simplerecyclerview.progress;

import android.content.*;
import android.util.*;
import android.view.*;
import android.widget.*;

import com.xdandroid.simplerecyclerview.*;

public class ProgressView extends ProgressBar {

    public ProgressView(Context context) {
        super(context);
    }

    public ProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ProgressView init() {
        FrameLayout.LayoutParams innerParams = new FrameLayout.LayoutParams(UIUtils.dp2px(getContext(), 40), UIUtils.dp2px(getContext(), 40));
        innerParams.setMargins(0, UIUtils.dp2px(getContext(), 6), 0, UIUtils.dp2px(getContext(), 6));
        innerParams.gravity = Gravity.CENTER;
        setLayoutParams(innerParams);
        setId(android.R.id.progress);
        return this;
    }
}
